package com.infinite.jsf.provider.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.infinite.jsf.provider.dao.DoctorAvailabilityDao;
import com.infinite.jsf.provider.model.DoctorAvailability;
import com.infinite.jsf.provider.model.Doctors;
import com.infinite.jsf.provider.model.SlotType;

public class DoctorAvailabilityController {

    private DoctorAvailability availability;
    private DoctorAvailabilityDao availabilityDao;
    private String doctorId;
    private String message;
    private List<DoctorAvailability> availabilityList;
    private Date selectedDate;
    private List<DoctorAvailability> availabilityByDateList;
    private DoctorAvailability selectedAvailability;
    private DoctorAvailability backupAvailability;

    private String sortColumn = "";
    private boolean sortAscending = true;

    private int currentPage = 1;
    private int pageSize = 5;

    private int totalRecords;

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        if (availabilityByDateList == null || availabilityByDateList.isEmpty()) return 1;
        return (int) Math.ceil((double) availabilityByDateList.size() / pageSize);
    }

    public boolean isNextButtonDisabled() {
        return currentPage >= getTotalPages();
    }

    public boolean isPreviousButtonDisabled() {
        return currentPage <= 1;
    }

    public List<DoctorAvailability> getPaginatedList() {
        if (availabilityByDateList == null) return null;

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, availabilityByDateList.size());

        if (start >= end) {
            currentPage = 1;
            start = 0;
            end = Math.min(pageSize, availabilityByDateList.size());
        }

        return availabilityByDateList.subList(start, end);
    }


    public void goToFirstPage() {
        currentPage = 1;
    }
 
    public void goToLastPage() {
        currentPage = getTotalPages();
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

    public void resetPagination() {
        currentPage = 1;
    }

    public String sortBy(String column) {
        if (column.equals(this.sortColumn)) {
            this.sortAscending = !this.sortAscending;
        } else {
            this.sortColumn = column;
            this.sortAscending = true;
        }

        if (availabilityByDateList != null) {
            Collections.sort(availabilityByDateList, new Comparator<DoctorAvailability>() {
                public int compare(DoctorAvailability a1, DoctorAvailability a2) {
                    int result = 0;
                    switch (column) {
                        case "availabilityId":
                            result = safeCompare(a1.getAvailabilityId(), a2.getAvailabilityId());
                            break;
                        case "availableDate":
                            result = safeCompare(a1.getAvailableDate(), a2.getAvailableDate());
                            break;
                        case "startTime":
                            result = safeCompare(a1.getStartTime(), a2.getStartTime());
                            break;
                        case "endTime":
                            result = safeCompare(a1.getEndTime(), a2.getEndTime());
                            break;
                        case "slotType":
                            String slotType1 = a1.getSlotType() != null ? a1.getSlotType().toString() : "";
                            String slotType2 = a2.getSlotType() != null ? a2.getSlotType().toString() : "";
                            result = slotType1.compareToIgnoreCase(slotType2);
                            break;
                        case "recurring":
                            result = Boolean.compare(a1.isRecurring(), a2.isRecurring());
                            break;
                        case "totalSlots":
                            result = Integer.compare(a1.getTotalSlots(), a2.getTotalSlots());
                            break;
                        case "notes":
                            result = safeCompare(a1.getNotes(), a2.getNotes());
                            break;
                        case "doctorName":
                            String name1 = (a1.getDoctor() != null && a1.getDoctor().getDoctorName() != null)
                                           ? a1.getDoctor().getDoctorName() : "";
                            String name2 = (a2.getDoctor() != null && a2.getDoctor().getDoctorName() != null)
                                           ? a2.getDoctor().getDoctorName() : "";
                            result = name1.compareToIgnoreCase(name2);
                            break;
                        case "specialization":
                            String spec1 = (a1.getDoctor() != null && a1.getDoctor().getSpecialization() != null)
                                           ? a1.getDoctor().getSpecialization() : "";
                            String spec2 = (a2.getDoctor() != null && a2.getDoctor().getSpecialization() != null)
                                           ? a2.getDoctor().getSpecialization() : "";
                            result = spec1.compareToIgnoreCase(spec2);
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
        return null;
    }
    
    //Add Availability
    public String addAvailability() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean isValid = true;

        context.getExternalContext().getFlash().setKeepMessages(true);

        // Normalize the doctor ID (trim whitespace and convert to uppercase)
        if (doctorId != null) {
            doctorId = doctorId.trim().toUpperCase();
        }

        if (doctorId == null || doctorId.trim().isEmpty()) {
            context.addMessage("availabilityForm:doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID is required.", null));
            isValid = false;
        } else if (!doctorId.matches("DOC\\d{3}")) {
            context.addMessage("availabilityForm:doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Doctor ID format (e.g., DOC001).", null));
            isValid = false;
        }

        // Rest of your validation logic remains the same...
        Doctors doctor = availabilityDao.getDoctorById(doctorId);
        if (doctor == null || doctor.getProvider() == null) {
            context.addMessage("availabilityForm:doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID is invalid or not linked to any provider.", null));
            isValid = false;
        }

        Date today = new Date();
        Date selectedDate = availability.getAvailableDate();
        if (selectedDate == null) {
            context.addMessage("availabilityForm:date", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the Available Date (mm-dd-yyyy)", null));
            isValid = false;
        } else {
            Date todayNoTime = removeTime(today);
            Date selectedNoTime = removeTime(selectedDate);

            if (selectedNoTime.before(todayNoTime)) {
                context.addMessage("availabilityForm:date", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date should not be in the past", null));
                isValid = false;
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(todayNoTime);
            cal.add(Calendar.DAY_OF_MONTH, 30);
            Date maxAllowedDate = cal.getTime();

            if (selectedNoTime.after(maxAllowedDate)) {
                String maxDateStr = new SimpleDateFormat("yyyy-MM-dd").format(maxAllowedDate);
                context.addMessage("availabilityForm:date", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date should not exceed " + maxDateStr, null));
                isValid = false;
            }
        }

        Date startTime = availability.getStartTime();
        if (startTime == null) {
            context.addMessage("availabilityForm:startTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Start Time (HH:mm)", null));
            isValid = false;
        }

        Date endTime = availability.getEndTime();
        if (endTime == null) {
            context.addMessage("availabilityForm:endTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "End Time is required (HH:mm)", null));
            isValid = false;
        }

        if (selectedDate != null && startTime != null && endTime != null) {
            Date fullStart = mergeDateAndTime(selectedDate, startTime);
            Date fullEnd = mergeDateAndTime(selectedDate, endTime);

            if (removeTime(selectedDate).equals(removeTime(today))) {
                if (fullStart.before(today)) {
                    context.addMessage("availabilityForm:startTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start time should not be in the past.", null));
                    isValid = false;
                }
            }

            if (fullEnd.before(fullStart)) {
                context.addMessage("availabilityForm:endTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "End time should be after Start time.", null));
                isValid = false;
            }

            int durationMinutes = (int) ((fullEnd.getTime() - fullStart.getTime()) / (60 * 1000));
            if (durationMinutes > 1440) {
                context.addMessage("availabilityForm:endTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "The availability range must not exceed 24 hours.", null));
                isValid = false;
            }

            int totalSlots = availability.getTotalSlots() != 0 ? availability.getTotalSlots() : 1;
            if (totalSlots > durationMinutes / 10) {
                context.addMessage("availabilityForm:totalSlots", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Total slots should not exceed " + (durationMinutes / 10) + " for a " + (durationMinutes / 60) + " hour duration.", null));
                isValid = false;
            }

        }

        if (availability.getSlotType() == null || availability.getSlotType().toString().trim().isEmpty()) {
            context.addMessage("availabilityForm:slotType", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Slot Type is required.", null));
            isValid = false;
        }

        if (availability.getTotalSlots() <= 0) {
            context.addMessage("availabilityForm:totalSlots", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Total slots must not be 0.", null));
            isValid = false;
        } else if (availability.getTotalSlots() > 1000) {
            context.addMessage("availabilityForm:totalSlots", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Total slots must not exceed 1000.", null));
            isValid = false;
        }

        if (!isValid) {
            this.message = "Validation Failed";
            return null;
        }

        try {
            availability.setDoctor(doctor);
            availability.setAvailabilityId(availabilityDao.generateAvailabilityId());
            availability.setRecurring(SlotType.STANDARD.equals(availability.getSlotType()));
            availabilityDao.addAvailability(availability);

            availability = new DoctorAvailability();
            availability.setDoctor(new Doctors());
            availabilityByDateList = null;
            doctorId = null;

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Availability Added Successfully...", null));
            this.message = "Availability Added Successfully...";
            return null;
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to add availability due to a system error.", null));
            this.message = "Addition Failed";
            return null;
        }
    }




    
    private Date removeTime(Date date) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    private Date mergeDateAndTime(Date datePart, Date timePart) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(datePart);

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(timePart);

        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, 0);
        dateCal.set(Calendar.MILLISECOND, 0);

        return dateCal.getTime();
    }




//    // Fetch by Doctor ID
//    public String fetchAvailability() {
//        if (doctorId == null || doctorId.trim().isEmpty()) {
//            message = "Doctor ID is required.";
//            availabilityList = null;
//            return null;
//        }
//
//        // Always fetch fresh data
//        availabilityList = availabilityDao.getAvailabilityByDoctor(doctorId);
//        message = null;
//        return null;
//    }


    // Fetch by Date
    public String fetchAvailabilityByDate() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (selectedDate == null) {
            context.addMessage("availabilityForm:date", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a date.", null));
            availabilityByDateList = null;
            return null;
        }

        Date today = removeTime(new Date());
        Date inputDate = removeTime(selectedDate);
        if (inputDate.before(today)) {
            context.addMessage("availabilityForm:date", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date should not be in the past.", null));
            availabilityByDateList = null;
            return null;
        }

        availabilityByDateList = availabilityDao.getAvailabilityByDate(selectedDate);
        totalRecords = (availabilityByDateList != null) ? availabilityByDateList.size() : 0;

        if (totalRecords == 0) {
            context.addMessage("availabilityForm:date", new FacesMessage(FacesMessage.SEVERITY_WARN, "Availability Date Does Not Exist.", null));
            availabilityByDateList = null;
            return null;
        }

        message = null;
        resetPagination(); // sets currentPage = 1
        return null;
    }

    
    // Update
    public String updateAvailability() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (selectedAvailability == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No availability selected for update.", null));
            return null;
        }

        Date today = new Date();
        Date selectedDate = selectedAvailability.getAvailableDate();

        // Date Validation
        if (selectedDate == null) {
            context.addMessage("availabilityForm:date", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the Available Date", null));
            return null;
        }
        if (removeTime(selectedDate).before(removeTime(today))) {
            context.addMessage("availabilityForm:date", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date should not be in past", null));
            return null;
        }

        // Start Time
        Date startTime = selectedAvailability.getStartTime();
        if (startTime == null) {
            context.addMessage("availabilityForm:startTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start Time is required.", null));
            return null;
        }

        // If selected date is today, startTime should be in future
        if (removeTime(selectedDate).equals(removeTime(today))) {
            Date now = new Date();
            Date fullStartTime = mergeDateAndTime(selectedDate, startTime);
            if (fullStartTime.before(now)) {
                context.addMessage("availabilityForm:startTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start time should not be in the past.", null));
                return null;
            }
        }

        // End Time
        Date endTime = selectedAvailability.getEndTime();
        if (endTime == null) {
            context.addMessage("availabilityForm:endTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "End Time is required.", null));
            return null;
        }

        Date fullStart = mergeDateAndTime(selectedDate, startTime);
        Date fullEnd = mergeDateAndTime(selectedDate, endTime);
        if (fullEnd.before(fullStart)) {
            context.addMessage("availabilityForm:endTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "End time should be after Start time.", null));
            return null;
        }

        // Slot Type
        if (selectedAvailability.getSlotType() == null || selectedAvailability.getSlotType().toString().trim().isEmpty()) {
            context.addMessage("availabilityForm:slotType", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Slot Type is required.", null));
            return null;
        }

        // Recurring Logic
//        if (SlotType.STANDARD.equals(selectedAvailability.getSlotType())) {
//            selectedAvailability.setRecurring(true);
//        } else if (SlotType.ADHOC.equals(selectedAvailability.getSlotType())) {
//            selectedAvailability.setRecurring(false);
//        }

        // Total Slots
        Integer totalSlots = selectedAvailability.getTotalSlots();
        if (totalSlots == null || totalSlots <= 0) {
            context.addMessage("availabilityForm:totalSlots",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Total Slots.", null));
            return null;
        }

        long durationMillis = fullEnd.getTime() - fullStart.getTime();
        long durationMinutes = durationMillis / (60 * 1000);
        int maxAllowedSlots = (int) (durationMinutes / 10);

        if (totalSlots > maxAllowedSlots) {
            context.addMessage("availabilityForm:totalSlots",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Total slots should not exceed " + maxAllowedSlots + " for a " + (durationMinutes / 60) + " hour duration.", null));
            return null;
        }

        // Final update to DB
        message = availabilityDao.updateAvailability(selectedAvailability);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Availability updated successfully.", null));
        return "listAvailabilityByDate";
    }
    
//    //Delete
//    public String deleteAvailability(String availabilityId) {
//        boolean deleted = availabilityDao.deleteAvailabilityById(availabilityId);
//
//        if (deleted) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(FacesMessage.SEVERITY_INFO, "Availability deleted successfully.", null));
//            getAvailabilityList(); // Refresh list
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deletion failed. Availability not found.", null));
//        }
//
//        return null;
//    }

    

    // Reset Full Form
    public String resetForm() {
        this.availability = new DoctorAvailability();
        this.availability.setDoctor(new Doctors());
        this.doctorId = null;
        this.selectedDate = null;
        this.message = null;
        this.availabilityList = null;
        this.availabilityByDateList = null;
        this.selectedAvailability = null;
        this.backupAvailability = null;

        return "addAvailability?faces-redirect=true";
    }
    
    // Reset Full Form
    public String resetAddFormBackButton() {
        this.availability = new DoctorAvailability();
        this.availability.setDoctor(new Doctors());
        this.doctorId = null;
        this.selectedDate = null;
        this.message = null;
        this.availabilityList = null;
        this.availabilityByDateList = null;
        this.selectedAvailability = null;
        this.backupAvailability = null;

        return "menu?faces-redirect=true";
    }
    
    public String resetSearchDateForm() {
        this.availability = new DoctorAvailability();
        this.availability.setDoctor(new Doctors());
        this.doctorId = null;
        this.selectedDate = null;
        this.message = null;
        this.availabilityList = null;
        this.availabilityByDateList = null;
        this.selectedAvailability = null;
        this.backupAvailability = null;

        return "listAvailabilityByDate?faces-redirect=true";
    }




    // Reset Update
    public String resetUpdateForm() {
        if (backupAvailability != null && selectedAvailability != null) {
            selectedAvailability.setAvailableDate(backupAvailability.getAvailableDate());
            selectedAvailability.setStartTime(backupAvailability.getStartTime());
            selectedAvailability.setEndTime(backupAvailability.getEndTime());
            selectedAvailability.setSlotType(backupAvailability.getSlotType());
            selectedAvailability.setRecurring(backupAvailability.isRecurring());
            selectedAvailability.setTotalSlots(backupAvailability.getTotalSlots());
            selectedAvailability.setNotes(backupAvailability.getNotes());
            selectedAvailability.setDoctor(backupAvailability.getDoctor());
        }
        message = null;
        return null;
    }

    public void setSelectedAvailability(DoctorAvailability selectedAvailability) {
        this.selectedAvailability = selectedAvailability;

        if (selectedAvailability != null) {
            this.backupAvailability = new DoctorAvailability();
            backupAvailability.setAvailabilityId(selectedAvailability.getAvailabilityId());
            backupAvailability.setAvailableDate(selectedAvailability.getAvailableDate());
            backupAvailability.setStartTime(selectedAvailability.getStartTime());
            backupAvailability.setEndTime(selectedAvailability.getEndTime());
            backupAvailability.setSlotType(selectedAvailability.getSlotType());
            backupAvailability.setRecurring(selectedAvailability.isRecurring());
            backupAvailability.setTotalSlots(selectedAvailability.getTotalSlots());
            backupAvailability.setNotes(selectedAvailability.getNotes());
            backupAvailability.setDoctor(selectedAvailability.getDoctor());
        }
    }

    // Getters and Setters

    
    
    public DoctorAvailability getAvailability() {
    	if (availability.getAvailabilityId() == null && availabilityDao != null) {
    	    String newId = availabilityDao.generateAvailabilityId();
    	    availability.setAvailabilityId(newId);
    	}
        return availability;
    }

	public DoctorAvailability getBackupAvailability() {
		return backupAvailability;
	}

	public void setBackupAvailability(DoctorAvailability backupAvailability) {
		this.backupAvailability = backupAvailability;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public boolean isSortAscending() {
		return sortAscending;
	}

	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}

	public void setAvailability(DoctorAvailability availability) {
        this.availability = availability;
    }

    public DoctorAvailabilityDao getAvailabilityDao() {
        return availabilityDao;
    }

    public void setAvailabilityDao(DoctorAvailabilityDao availabilityDao) {
        this.availabilityDao = availabilityDao;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<DoctorAvailability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailabilityList(List<DoctorAvailability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
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
