package com.java.jsf.provider.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;

import com.java.jsf.provider.dao.AppointmentDao;
import com.java.jsf.provider.dao.MedicalHistoryDao;
import com.java.jsf.provider.daoImpl.MedicalHistoryDaoImpl;
import com.java.jsf.provider.dto.AppointmentSlip;
import com.java.jsf.provider.model.Appointment;
import com.java.jsf.util.MailSend;

public class AppointmentController {

    private AppointmentDao appointmentDao;
    private MedicalHistoryDao medicalHistoryDao = new MedicalHistoryDaoImpl();

    private List<Appointment> allAppointments;
    private List<Appointment> searchResults;

    private String doctorId;
    private String status;

    // Sorting & Pagination
    private String sortColumn = "";
    private boolean sortAscending = true;
    private int pageSize = 5;
    private int currentPage = 1;
    private int totalPages;


    // ------------------ Load Appointments by Status ------------------

    public String loadAppointmentsByStatus() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (doctorId == null || doctorId.trim().isEmpty()) {
        	context.addMessage("appointmentForm:doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor Id Required", null));
        	return null;
        }
        
        doctorId = doctorId.trim().toUpperCase();
        
        if (!doctorId.matches("DOC\\d{3}$")) {
        	context.addMessage("appointmentForm:doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID must be in format DOC001.", null));
        	return null;
    	}
        
        if (!medicalHistoryDao.doctorExists(doctorId)) {
        	context.addMessage("appointmentForm:doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Doctor Id doesn't exist", null));
        	return null;
        }
        
        if (status == null || status.trim().isEmpty()) {
            context.addMessage("appointmentForm:status", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Appointment Status required.", null));
            allAppointments = null;
            return null;
        }

        allAppointments = appointmentDao.getAppointmentsByDoctorAndStatus(doctorId.trim(), status.trim());
        sortColumn = "";
        currentPage = 1;

        if (allAppointments == null || allAppointments.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "No appointments found for the selected doctor and status.", null));
        }

        return null;
    }

    // ------------------ Dropdown Change Handler ------------------

    public void statusChanged(ValueChangeEvent event) {
        status = (String) event.getNewValue();
        loadAppointmentsByStatus();
    }

    // ------------------ Approve (Book) a Pending Appointment ------------------

    public String approve(String appointmentId) {
        FacesContext context = FacesContext.getCurrentInstance();
        Appointment appt = appointmentDao.getAppointmentById(appointmentId);
        
        if (appt == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Appointment not found", null));
            return null;
        }

        if (appt.getStart() == null || appt.getEnd() == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Appointment has no valid time slots, Cannot book.", null));
            return null;
        }

        boolean success = appointmentDao.markAppointmentAsBooked(appointmentId, new Timestamp(System.currentTimeMillis()));
        if (success) {
            try {
                ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
                
                // Format dates properly
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                
                String date = dateFormat.format(appt.getStart());
                String timeRange = timeFormat.format(appt.getStart()) + " - " + timeFormat.format(appt.getEnd());
                
                // Get provider name from doctor's provider relationship
                String providerName = appt.getDoctor().getProvider() != null 
                    ? appt.getDoctor().getProvider().getProviderName() 
                    : "Our Clinic";
                
                AppointmentSlip slip = new AppointmentSlip(
                    appt.getRecipient().getFirstName() + " " + appt.getRecipient().getLastName(),
                    appointmentId,
                    providerName,
                    servletContext.getInitParameter("providerEmail"),
                    servletContext.getInitParameter("contactNumber"),
                    appt.getDoctor().getDoctorName(),
                    appt.getDoctor().getSpecialization(),
                    date,
                    appt.getSlotNo(),
                    timeRange
                );
                
                String subject = "Appointment Confirmed - " + appt.getDoctor().getDoctorName();
                MailSend.sendMail(appt.getRecipient().getEmail(), subject, MailSend.appointmentConfirmation(slip));
                
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Appointment booked successfully. Confirmation email sent to " + appt.getRecipient().getEmail(), null));
            } catch (Exception e) {
                System.err.println("Error sending confirmation email: " + e.getMessage());
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Appointment booked, but failed to send confirmation email. Error: " + e.getMessage(), null));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Failed to update appointment status in database", null));
        }

        loadAppointmentsByStatus();
        return null;
    }
    // ------------------ Cancel a Pending Appointment ------------------

    public String cancel(String appointmentId) {
        FacesContext context = FacesContext.getCurrentInstance();
        Appointment appt = appointmentDao.getAppointmentById(appointmentId);
        
        if (appt == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Appointment not found", null));
            return null;
        }

        boolean success = appointmentDao.cancelPendingAppointmentByDoctor(appointmentId, new Timestamp(System.currentTimeMillis()));
        if (success) {
            try {
                ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
                
                // Format dates properly with null checks
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                
                String date = appt.getStart() != null ? dateFormat.format(appt.getStart()) : "N/A";
                String timeRange = (appt.getStart() != null && appt.getEnd() != null) 
                    ? timeFormat.format(appt.getStart()) + " - " + timeFormat.format(appt.getEnd())
                    : "N/A";
                
                // Get provider name from doctor's provider relationship
                String providerName = appt.getDoctor().getProvider() != null 
                    ? appt.getDoctor().getProvider().getProviderName() 
                    : "Our Clinic";
                
                AppointmentSlip slip = new AppointmentSlip(
                    appt.getRecipient().getFirstName() + " " + appt.getRecipient().getLastName(),
                    appointmentId,
                    providerName,
                    servletContext.getInitParameter("providerEmail"),
                    servletContext.getInitParameter("contactNumber"),
                    appt.getDoctor().getDoctorName(),
                    appt.getDoctor().getSpecialization(),
                    date,
                    appt.getSlotNo(),
                    timeRange
                );
                
                String subject = "Appointment Cancelled - " + appt.getDoctor().getDoctorName();
                MailSend.sendMail(appt.getRecipient().getEmail(), subject, MailSend.appointmentCancellation(slip));
                
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Appointment cancelled successfully. Notification sent to " + appt.getRecipient().getEmail(), null));
            } catch (Exception e) {
                System.err.println("Error sending cancellation email: " + e.getMessage());
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Appointment cancelled, but failed to send notification. Error: " + e.getMessage(), null));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Failed to update appointment status in database", null));
        }

        loadAppointmentsByStatus();
        return null;
    }

    // ------------------ Sorting Logic ------------------

    private void sortAppointments() {
        if (allAppointments != null) {
            Collections.sort(allAppointments, new Comparator<Appointment>() {
                public int compare(Appointment a1, Appointment a2) {
                    int result = 0;
                    switch (sortColumn) {
                        case "appointmentId":
                            result = safeCompare(a1.getAppointmentId(), a2.getAppointmentId());
                            break;
                        case "hId":
                            result = safeCompare(a1.getRecipient().gethId(), a2.getRecipient().gethId());
                            break;
                        case "appointmentDate":
                            result = safeCompare(a1.getStart(), a2.getStart());
                            break;
                        case "status":
                            result = safeCompare(a1.getStatus(), a2.getStatus());
                            break;
                        case "start":
                            result = safeCompare(a1.getStart(), a2.getStart());
                            break;
                        case "end":
                            result = safeCompare(a1.getEnd(), a2.getEnd());
                            break;
                        case "recipientName":
                            String name1 = "";
                            String name2 = "";
                            if (a1.getRecipient() != null) {
                                name1 = (a1.getRecipient().getFirstName() != null ? a1.getRecipient().getFirstName() : "") +
                                        (a1.getRecipient().getLastName() != null ? a1.getRecipient().getLastName() : "");
                            }
                            if (a2.getRecipient() != null) {
                                name2 = (a2.getRecipient().getFirstName() != null ? a2.getRecipient().getFirstName() : "") +
                                        (a2.getRecipient().getLastName() != null ? a2.getRecipient().getLastName() : "");
                            }
                            result = name1.compareToIgnoreCase(name2);
                            break;
                        default:
                            result = 0;
                    }
                    return sortAscending ? result : -result;
                }

                private <T extends Comparable<T>> int safeCompare(T o1, T o2) {
                    if (o1 == null && o2 == null) return 0;
                    if (o1 == null) return -1;
                    if (o2 == null) return 1;
                    return o1.compareTo(o2);
                }
            });
        }

        resetPagination();
    }


    public boolean isAscDisabled(String column) {
        return column.equals(this.sortColumn) && this.sortAscending;
    }

    public boolean isDescDisabled(String column) {
        return column.equals(this.sortColumn) && !this.sortAscending;
    }

    public void sortByAsc(String column) {
        this.sortColumn = column;
        this.sortAscending = true;
        sortAppointments();
    }

    public void sortByDesc(String column) {
        this.sortColumn = column;
        this.sortAscending = false;
        sortAppointments();
    }

    public void resetPagination() {
        currentPage = 1;
    }

    // ------------------ Pagination ------------------

    public List<Appointment> getPaginatedAppointments() {
    	
        if (allAppointments == null || allAppointments.isEmpty()) {
        	totalPages = 0;
        	
        	return Collections.emptyList();
        }
        totalPages = (int) Math.ceil((double) allAppointments.size() / pageSize);
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, allAppointments.size());
        return allAppointments.subList(start, end);
    }

    public boolean isNextButtonDisabled() {
        return currentPage >= getTotalPages();
    }

    public boolean isPreviousButtonDisabled() {
        return currentPage <= 1;
    }

    public String nextPage() {
        if (currentPage < getTotalPages()) {
            currentPage++;
        }
        return null;
    }

    public String previousPage() {
        if (currentPage > 1) {
            currentPage--;
        }
        return null;
    }
    
    public void goToFirstPage() {
        currentPage = 1;
    }
 
    public void goToLastPage() {
        currentPage = getTotalPages();
    }
    
    // ------------------ Load Completed Appointments ------------------

    public String loadCompletedAppointments() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            searchResults = appointmentDao.getCompletedAppointments();
            if (searchResults == null || searchResults.isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "No completed appointments found.", null));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Loaded " + searchResults.size() + " completed appointment(s).", null));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Failed to fetch completed appointments.", null));
            e.printStackTrace();
        }

        return null;
    }

    // ------------------ Load All Appointments ------------------

    public String loadAllAppointments() {
        allAppointments = appointmentDao.getAllAppointments();
        currentPage = 1;
        return null;
    }

    public int getTotalRecords() {
        return (allAppointments != null) ? allAppointments.size() : 0;
    }

//    private List<Appointment> allAppointments;
//    private List<Appointment> searchResults;
//
//    private String doctorId;
//    private String status;
//
//    // Sorting & Pagination
//    private String sortColumn = "";
//    private boolean sortAscending = true;
//    private int pageSize = 5;
//    private int currentPage = 1;
//    private int totalPages;
    
    //Reset Form
    
    public String resetForm() {
    	this.doctorId = null;
    	this.status = null;
    	this.currentPage = 0;
    	this.allAppointments = null;
    	this.searchResults = null;
    	
    	return null;
    }
    // ------------------ Getters and Setters ------------------

    public List<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public void setAllAppointments(List<Appointment> allAppointments) {
        this.allAppointments = allAppointments;
    }

    public AppointmentDao getAppointmentDao() {
        return appointmentDao;
    }

    public void setAppointmentDao(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Appointment> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<Appointment> searchResults) {
        this.searchResults = searchResults;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
    
    
}
