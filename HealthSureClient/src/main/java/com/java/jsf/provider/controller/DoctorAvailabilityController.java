package com.java.jsf.provider.controller;

import java.util.Date;
import java.util.List;

import com.java.jsf.provider.dao.DoctorAvailabilityDao;
import com.java.jsf.provider.model.DoctorAvailability;
import com.java.jsf.provider.model.Doctors;

public class DoctorAvailabilityController {

    private DoctorAvailability availability;
    private DoctorAvailabilityDao availabilityDao;
    private String doctorId;
    private String message;
    private List<DoctorAvailability> availabilityList;
    private Date selectedDate; 
    private List<DoctorAvailability> availabilityByDateList;

    private DoctorAvailability selectedAvailability;

    // Constructor to initialize nested Doctor object
    public DoctorAvailabilityController() {
        availability = new DoctorAvailability();
        availability.setDoctor(new Doctors());  
    }
    
    //Add Availability
    public String addAvailability() {
        if (doctorId == null || doctorId.trim().isEmpty()) {
            message = "Doctor ID is required.";
            return "error";
        }

        // Set doctor object into availability
        Doctors doc = new Doctors();
        doc.setDoctorId(doctorId);
        availability.setDoctor(doc);

        // Save using DAO
        message = availabilityDao.addAvailability(availability);
        return "availabilitySuccess"; // Or a navigation string
    }
    
    public String fetchAvailability() {
        this.availabilityList = getAvailabilityList();
        return null; // Stay on same page
    }
    
    //Fetch Availability by Date
    public String fetchAvailabilityByDate() {
        if (selectedDate == null) {
            message = "Please select a date.";
            availabilityByDateList = null;
            return null;
        }
        availabilityByDateList = availabilityDao.getAvailabilityByDate(selectedDate);
        message = null;
        return null;
    }

    
    
    public String updateAvailability() {
        message = availabilityDao.updateAvailability(selectedAvailability);
        return "listAvailabilityByDate";  
    }
    
    
    public List<DoctorAvailability> getAvailabilityList() {
        if (doctorId == null || doctorId.trim().isEmpty()) {
            return null;
        }
        return availabilityDao.getAvailabilityByDoctor(doctorId);
    }
    
    public void setAvailabilityList(List<DoctorAvailability> availabilityList) {
		this.availabilityList = availabilityList;
	}
    
	public List<DoctorAvailability> getAvailabilityByDateList() {
		return availabilityByDateList;
	}

	public void setAvailabilityByDateList(List<DoctorAvailability> availabilityByDateList) {
		this.availabilityByDateList = availabilityByDateList;
	}
	
	public DoctorAvailability getSelectedAvailability() {
        return selectedAvailability;
    }

    public void setSelectedAvailability(DoctorAvailability selectedAvailability) {
        this.selectedAvailability = selectedAvailability;
    }


	// Getters and setters
	
	
    public DoctorAvailability getAvailability() {
        return availability;
    }

    public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public void setAvailability(DoctorAvailability availability) {
        this.availability = availability;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public DoctorAvailabilityDao getAvailabilityDao() {
        return availabilityDao;
    }

    public void setAvailabilityDao(DoctorAvailabilityDao availabilityDao) {
        this.availabilityDao = availabilityDao;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
















































//package com.java.jsf.provider.controller;
//
//import java.sql.SQLException;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.naming.NamingException;
//
//import com.java.ejb.provider.beans.DoctorAvailabilityEjbImpl;
//import com.java.ejb.provider.model.DoctorAvailability;
//
//public class DoctorAvailabilityController {
//
//    private DoctorAvailability ejbAvailability;
//    private DoctorAvailability ejbSelectedAvailability;
//    private List<DoctorAvailability> ejbAvailabilityList;
//    private List<DoctorAvailability> ejbAvailabilityByDateList;
//
//    private DoctorAvailabilityEjbImpl availabilityEjbImpl;
//
//    private String doctorId;
//    private String message;
//    private Date selectedDate;
//    private List<DoctorAvailability> paginatedList;
//    private int currentPage = 0;
//    private int pageSize = 5;
//    private String sortColumn = "availableDate";
//    private boolean ascending = true;
//    private String lastAddedAvailabilityId;
//
//    public DoctorAvailabilityController() {
//        ejbAvailability = new DoctorAvailability();
//        availabilityEjbImpl = new DoctorAvailabilityEjbImpl();
//    }
//
//    public String addAvailabilityOnly() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        boolean hasError = false;
//
//        // Basic required field validations
//        if (doctorId == null || doctorId.trim().isEmpty()) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID is required.", null));
//            hasError = true;
//        }
//
//        if (ejbAvailability.getAvailableDate() == null) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Available Date is required.", null));
//            hasError = true;
//        }
//
//        if (ejbAvailability.getStartTime() == null) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start Time is required.", null));
//            hasError = true;
//        }
//
//        if (ejbAvailability.getEndTime() == null) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "End Time is required.", null));
//            hasError = true;
//        }
//
//        if (ejbAvailability.getTotalSlots() <= 0) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Total Slots must be greater than 0.", null));
//            hasError = true;
//        }
//
//        if (ejbAvailability.getNotes() == null || ejbAvailability.getNotes().trim().length() < 5) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Notes must be at least 5 characters long.", null));
//            hasError = true;
//        }
//
//        if (hasError) return null;
//
//        // Doctor-provider relation check
//        try {
//            if (!availabilityEjbImpl.isDoctorUnderProvider(doctorId)) {
//                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor is not under any registered provider.", null));
//                return null;
//            }
//        } catch (Exception e) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation error: " + e.getMessage(), null));
//            return null;
//        }
//
//        // Time-based validations
//        Date now = new Date();
//
//        if (ejbAvailability.getStartTime().before(now)) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start time must not be in the past.", null));
//            return null;
//        }
//
//        if (!ejbAvailability.getEndTime().after(ejbAvailability.getStartTime())) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "End time must be after start time.", null));
//            return null;
//        }
//
//        // Assign doctor ID and persist
//        ejbAvailability.setDoctorId(doctorId);
//
//        try {
//            lastAddedAvailabilityId = availabilityEjbImpl.addAvailability(ejbAvailability);
//            message = "Availability added with ID: " + lastAddedAvailabilityId;
//        } catch (Exception e) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding availability: " + e.getMessage(), null));
//            return null;
//        }
//
//        fetchAvailability();
//        return "availabilitySuccess";
//    }
//
//
//    public String fetchAvailabilityByDate() {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        if (selectedDate == null) {
//            message = "Please select a date.";
//            ejbAvailabilityByDateList = null;
//            context.addMessage("selectedDate", new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
//            return null;
//        }
//
//        try {
//            ejbAvailabilityByDateList = availabilityEjbImpl.getAvailabilityByDate(selectedDate);
//            if (ejbAvailabilityByDateList.isEmpty()) {
//                message = "No availability found for selected date.";
//                context.addMessage("selectedDate", new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
//            } else {
//                message = null;
//            }
//        } catch (Exception e) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error fetching availability: " + e.getMessage(), null));
//        }
//
//        return null;
//    }
//
//    public String fetchAvailability() {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        if (doctorId == null || doctorId.trim().isEmpty()) {
//            message = "Please enter a Doctor ID.";
//            ejbAvailabilityList = null;
//            context.addMessage("doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
//            return null;
//        }
//
//        try {
//            ejbAvailabilityList = availabilityEjbImpl.getAvailabilityByDoctor(doctorId);
//            sortAndPaginate();
//            message = ejbAvailabilityList.isEmpty() ? "No records found." : null;
//        } catch (Exception e) {
//            message = "Error fetching data: " + e.getMessage();
//            ejbAvailabilityList = null;
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
//        }
//
//        return null;
//    }
//
//    public void sortBy(String column) {
//        if (sortColumn.equals(column)) {
//            ascending = !ascending;
//        } else {
//            sortColumn = column;
//            ascending = true;
//        }
//        sortAndPaginate();
//    }
//
//    private void sortAndPaginate() {
//        if (ejbAvailabilityList == null) return;
//
//        Comparator<DoctorAvailability> comparator;
//        switch (sortColumn) {
//            case "availableDate":
//                comparator = Comparator.comparing(DoctorAvailability::getAvailableDate);
//                break;
//            case "startTime":
//                comparator = Comparator.comparing(DoctorAvailability::getStartTime);
//                break;
//            case "endTime":
//                comparator = Comparator.comparing(DoctorAvailability::getEndTime);
//                break;
//            case "totalSlots":
//                comparator = Comparator.comparing(DoctorAvailability::getTotalSlots);
//                break;
//            default:
//                comparator = Comparator.comparing(DoctorAvailability::getAvailabilityId);
//        }
//
//        if (!ascending) comparator = comparator.reversed();
//        Collections.sort(ejbAvailabilityList, comparator);
//        updatePaginatedList();
//    }
//
//    public void nextPage() {
//        if ((currentPage + 1) * pageSize < ejbAvailabilityList.size()) {
//            currentPage++;
//            updatePaginatedList();
//        }
//    }
//
//    public void prevPage() {
//        if (currentPage > 0) {
//            currentPage--;
//            updatePaginatedList();
//        }
//    }
//
//    private void updatePaginatedList() {
//        int fromIndex = currentPage * pageSize;
//        int toIndex = Math.min(fromIndex + pageSize, ejbAvailabilityList.size());
//        paginatedList = ejbAvailabilityList.subList(fromIndex, toIndex);
//    }
//
//    public String updateAvailability() {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        if (ejbSelectedAvailability == null || ejbSelectedAvailability.getAvailabilityId() == null) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Please select an availability record to update.", null));
//            return null;
//        }
//
//        try {
//            availabilityEjbImpl.updateAvailability(ejbSelectedAvailability);
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
//                    "Availability updated successfully.", null));
//            return "listAvailabilityByDate";
//
//        } catch (NamingException | ClassNotFoundException | SQLException e) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Update failed: " + e.getMessage(), null));
//            return null;
//        }
//    }
//
//    // Getters and Setters
//    public DoctorAvailability getEjbAvailability() {
//        return ejbAvailability;
//    }
//
//    public void setEjbAvailability(DoctorAvailability ejbAvailability) {
//        this.ejbAvailability = ejbAvailability;
//    }
//
//    public DoctorAvailability getEjbSelectedAvailability() {
//        return ejbSelectedAvailability;
//    }
//
//    public void setEjbSelectedAvailability(DoctorAvailability ejbSelectedAvailability) {
//        this.ejbSelectedAvailability = ejbSelectedAvailability;
//    }
//
//    public List<DoctorAvailability> getEjbAvailabilityList() {
//        return ejbAvailabilityList;
//    }
//
//    public void setEjbAvailabilityList(List<DoctorAvailability> ejbAvailabilityList) {
//        this.ejbAvailabilityList = ejbAvailabilityList;
//    }
//
//    public List<DoctorAvailability> getEjbAvailabilityByDateList() {
//        return ejbAvailabilityByDateList;
//    }
//
//    public void setEjbAvailabilityByDateList(List<DoctorAvailability> ejbAvailabilityByDateList) {
//        this.ejbAvailabilityByDateList = ejbAvailabilityByDateList;
//    }
//
//    public DoctorAvailabilityEjbImpl getAvailabilityEjbImpl() {
//        return availabilityEjbImpl;
//    }
//
//    public void setAvailabilityEjbImpl(DoctorAvailabilityEjbImpl availabilityEjbImpl) {
//        this.availabilityEjbImpl = availabilityEjbImpl;
//    }
//
//    public String getDoctorId() {
//        return doctorId;
//    }
//
//    public void setDoctorId(String doctorId) {
//        this.doctorId = doctorId;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Date getSelectedDate() {
//        return selectedDate;
//    }
//
//    public void setSelectedDate(Date selectedDate) {
//        this.selectedDate = selectedDate;
//    }
//
//    public List<DoctorAvailability> getPaginatedList() {
//        return paginatedList;
//    }
//
//    public void setPaginatedList(List<DoctorAvailability> paginatedList) {
//        this.paginatedList = paginatedList;
//    }
//
//    public int getCurrentPage() {
//        return currentPage;
//    }
//
//    public void setCurrentPage(int currentPage) {
//        this.currentPage = currentPage;
//    }
//
//    public int getPageSize() {
//        return pageSize;
//    }
//
//    public void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }
//
//    public String getSortColumn() {
//        return sortColumn;
//    }
//
//    public void setSortColumn(String sortColumn) {
//        this.sortColumn = sortColumn;
//    }
//
//    public boolean isAscending() {
//        return ascending;
//    }
//
//    public void setAscending(boolean ascending) {
//        this.ascending = ascending;
//    }
//
//    public String getLastAddedAvailabilityId() {
//        return lastAddedAvailabilityId;
//    }
//
//    public void setLastAddedAvailabilityId(String lastAddedAvailabilityId) {
//        this.lastAddedAvailabilityId = lastAddedAvailabilityId;
//    }
//}
