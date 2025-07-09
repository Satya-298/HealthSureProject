package com.java.jsf.provider.controller;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.java.jsf.provider.dao.DoctorAvailabilityDao;
import com.java.jsf.provider.model.DoctorAvailability;
import com.java.jsf.provider.model.Doctors;
import com.java.jsf.provider.model.SlotType;

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

    private int currentPage = 0;
    private int pageSize = 5;

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
        if (availabilityByDateList == null) return true;
        return ((currentPage + 1) * pageSize) >= availabilityByDateList.size();
    }

    public boolean isPreviousButtonDisabled() {
        return currentPage == 0;
    }

    public List<DoctorAvailability> getPaginatedList() {
        if (availabilityByDateList == null) return null;

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, availabilityByDateList.size());

        if (start >= end) {
            currentPage = 0;
            start = 0;
            end = Math.min(pageSize, availabilityByDateList.size());
        }

        return availabilityByDateList.subList(start, end);
    }

    public String nextPage() {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
        }
        return null;
    }

    public String previousPage() {
        if (currentPage > 0) {
            currentPage--;
        }
        return null;
    }

    public void resetPagination() {
        currentPage = 0;
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
                            result = a1.getAvailabilityId().compareTo(a2.getAvailabilityId());
                            break;
                        case "availableDate":
                            result = a1.getAvailableDate().compareTo(a2.getAvailableDate());
                            break;
                        case "startTime":
                            result = a1.getStartTime().compareTo(a2.getStartTime());
                            break;
                        case "endTime":
                            result = a1.getEndTime().compareTo(a2.getEndTime());
                            break;
                        case "slotType":
                            result = a1.getSlotType().toString().compareTo(a2.getSlotType().toString());
                            break;
                        case "recurring":
                            result = Boolean.compare(a1.isRecurring(), a2.isRecurring());
                            break;
                        case "totalSlots":
                            result = Integer.compare(a1.getTotalSlots(), a2.getTotalSlots());
                            break;
                        case "notes":
                            result = a1.getNotes().compareTo(a2.getNotes());
                            break;
                        case "doctorId":
                            result = a1.getDoctor().getDoctorId().compareTo(a2.getDoctor().getDoctorId());
                            break;
                        default:
                            result = 0;
                    }
                    return sortAscending ? result : -result;
                }
            });
        }

        resetPagination();
        return null;
    }
    
    // Add Availability
    public String addAvailability() {
        FacesContext context = FacesContext.getCurrentInstance();

        String fieldId = "availabilityForm:doctorId";
        String dateFieldId = "availabilityForm:date";
        String startTimeFieldId = "availabilityForm:startTime";
        String endTimeFieldId = "availabilityForm:endTime";
        String slotTypeFieldId = "availabilityForm:slotType";
        String totalSlotsFieldId = "availabilityForm:totalSlots";
        String notesFieldId = "availabilityForm:notes";

        // Doctor ID Validation
        if (doctorId == null || doctorId.trim().isEmpty()) {
            context.addMessage(fieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID is required.", null));
            return null;
        }
        if (!doctorId.matches("D\\d{3}")) {
            context.addMessage(fieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Doctor ID format (e.g., D001).", null));
            return null;
        }

        // Doctor Existence
        Doctors doctor = availabilityDao.getDoctorById(doctorId);
        if (doctor == null || doctor.getProvider() == null) {
            context.addMessage(fieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID is invalid or not linked to any provider.", null));
            return null;
        }

        // Date Validation
        Date today = new Date();
        Date selectedDate = availability.getAvailableDate();
        if (selectedDate == null) {
            context.addMessage(dateFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the Available Date (yyyy-MM-dd)", null));
            return null;
        }
        if (removeTime(selectedDate).before(removeTime(today))) {
            context.addMessage(dateFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date should not be in past", null));
            return null;
        }

        // Start Time Validation
        Date startTime = availability.getStartTime();
        if (startTime == null) {
            context.addMessage(startTimeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Start Time (HH:mm)", null));
            return null;
        }
        if (removeTime(today).equals(removeTime(selectedDate))) {
            Date now = new Date();
            Date fullStartTime = mergeDateAndTime(selectedDate, startTime);
            if (fullStartTime.before(now)) {
                context.addMessage(startTimeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start time should not be in the past.", null));
                return null;
            }
        }

        // End Time Validation
        Date endTime = availability.getEndTime();
        if (endTime == null) {
            context.addMessage(endTimeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "End Time is required (HH:mm)", null));
            return null;
        }
        Date fullStart = mergeDateAndTime(selectedDate, startTime);
        Date fullEnd = mergeDateAndTime(selectedDate, endTime);
        if (fullEnd.before(fullStart)) {
            context.addMessage(endTimeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "End time should be after Start time.", null));
            return null;
        }

        // Slot Type Validation
        if (availability.getSlotType() == null || availability.getSlotType().toString().trim().isEmpty()) {
            context.addMessage(slotTypeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Slot Type is required.", null));
            return null;
        }

        // Recurring Setup
        if (SlotType.STANDARD.equals(availability.getSlotType())) {
            availability.setRecurring(true);
        } else if (SlotType.ADHOC.equals(availability.getSlotType())) {
            availability.setRecurring(false);
        }

        // Total Slots Validation
        Integer totalSlots = availability.getTotalSlots();
        if (totalSlots == null || totalSlots <= 0) {
            context.addMessage(totalSlotsFieldId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Total Slots.", null));
            return null;
        }

        long durationMillis = fullEnd.getTime() - fullStart.getTime();
        long durationMinutes = durationMillis / (60 * 1000);
        int maxAllowedSlots = (int) (durationMinutes / 10);  // 6 per hour

        if (totalSlots > maxAllowedSlots) {
            context.addMessage(totalSlotsFieldId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Total slots should not exceed " + maxAllowedSlots + " for a " + ((int)durationMinutes / 60) + " hour duration.", null));
            return null;
        }

        availability.setTotalSlots(totalSlots);

        // Notes Validation
        String notes = availability.getNotes();
        if (notes == null || notes.trim().length() < 10) {
            context.addMessage(notesFieldId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Notes must be at least 10 characters long.", null));
            return null;
        }
        if (notes.trim().length() > 100) {
            context.addMessage(notesFieldId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Notes must not exceed 100 characters.", null));
            return null;
        }

        // Save to DB
        availability.setDoctor(doctor);
        String newId = availabilityDao.generateAvailabilityId();
        availability.setAvailabilityId(newId);
        availabilityDao.addAvailability(availability);

        // Reset Form
        availability = new DoctorAvailability();
        availability.setDoctor(new Doctors());
        selectedDate = null;
        availabilityByDateList = null;
        doctorId = null;

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Availability added successfully.", null));
        return "availabilitySuccess";
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
        String dateFieldId = "date";

        // 1. Null check
        if (selectedDate == null) {
            context.addMessage(dateFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a date.", null));
            availabilityByDateList = null;
            return null;
        }

        // 2. Past date check
        Date today = removeTime(new Date());
        Date inputDate = removeTime(selectedDate);
        if (inputDate.before(today)) {
            context.addMessage(dateFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date should not be in the past.", null));
            availabilityByDateList = null;
            return null;
        }

        // 3. Fetch from DB
        availabilityByDateList = availabilityDao.getAvailabilityByDate(selectedDate);

        // 4. No result found
        if (availabilityByDateList == null || availabilityByDateList.isEmpty()) {
            context.addMessage(dateFieldId, new FacesMessage(FacesMessage.SEVERITY_WARN, "Availability Date Does Not Exist.", null));
            availabilityByDateList = null;
            return null;
        }

        message = null;
        resetPagination();
        return null;
    }


    
    
//    public String fetchAvailabilityByDate() {
//        if (selectedDate == null) {
//            message = "Please select a date.";
//            availabilityByDateList = null;
//            return null;
//        }
//
//        // Optimization: Skip re-fetching if same date is selected and results already exist
//        if (availabilityByDateList != null && !availabilityByDateList.isEmpty()) {
//            Date existingDate = availabilityByDateList.get(0).getAvailableDate();
//            if (existingDate != null && existingDate.equals(selectedDate)) {
//                return null; // skip re-fetch
//            }
//        }
//
//        availabilityByDateList = availabilityDao.getAvailabilityByDate(selectedDate);
//        message = null;
//        return null;
//    }
    

    
    // Update
    public String updateAvailability() {
        FacesContext context = FacesContext.getCurrentInstance();

        String dateFieldId = "availabilityForm:date";
        String startTimeFieldId = "availabilityForm:startTime";
        String endTimeFieldId = "availabilityForm:endTime";
        String slotTypeFieldId = "availabilityForm:slotType";
        String totalSlotsFieldId = "availabilityForm:totalSlots";
        String notesFieldId = "availabilityForm:notes";

        if (selectedAvailability == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No availability selected for update.", null));
            return null;
        }

        Date today = new Date();
        Date selectedDate = selectedAvailability.getAvailableDate();

        // Date Validation
        if (selectedDate == null) {
            context.addMessage(dateFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the Available Date (yyyy-MM-dd)", null));
            return null;
        }
        if (removeTime(selectedDate).before(removeTime(today))) {
            context.addMessage(dateFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date should not be in past", null));
            return null;
        }

        // Start Time
        Date startTime = selectedAvailability.getStartTime();
        if (startTime == null) {
            context.addMessage(startTimeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start Time is required.", null));
            return null;
        }

        // If selected date is today, startTime should be in future
        if (removeTime(selectedDate).equals(removeTime(today))) {
            Date now = new Date();
            Date fullStartTime = mergeDateAndTime(selectedDate, startTime);
            if (fullStartTime.before(now)) {
                context.addMessage(startTimeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start time should not be in the past.", null));
                return null;
            }
        }

        // End Time
        Date endTime = selectedAvailability.getEndTime();
        if (endTime == null) {
            context.addMessage(endTimeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "End Time is required.", null));
            return null;
        }

        Date fullStart = mergeDateAndTime(selectedDate, startTime);
        Date fullEnd = mergeDateAndTime(selectedDate, endTime);
        if (fullEnd.before(fullStart)) {
            context.addMessage(endTimeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "End time should be after Start time.", null));
            return null;
        }

        // Slot Type
        if (selectedAvailability.getSlotType() == null || selectedAvailability.getSlotType().toString().trim().isEmpty()) {
            context.addMessage(slotTypeFieldId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Slot Type is required.", null));
            return null;
        }

        // Recurring Logic
        if (SlotType.STANDARD.equals(selectedAvailability.getSlotType())) {
            selectedAvailability.setRecurring(true);
        } else if (SlotType.ADHOC.equals(selectedAvailability.getSlotType())) {
            selectedAvailability.setRecurring(false);
        }

        // Total Slots
        Integer totalSlots = selectedAvailability.getTotalSlots();
        if (totalSlots == null || totalSlots <= 0) {
            context.addMessage(totalSlotsFieldId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Total Slots.", null));
            return null;
        }

        long durationMillis = fullEnd.getTime() - fullStart.getTime();
        long durationMinutes = durationMillis / (60 * 1000);
        int maxAllowedSlots = (int) (durationMinutes / 10);

        if (totalSlots > maxAllowedSlots) {
            context.addMessage(totalSlotsFieldId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Total slots should not exceed " + maxAllowedSlots + " for a " + (durationMinutes / 60) + " hour duration.", null));
            return null;
        }

        // Notes
        String notes = selectedAvailability.getNotes();
        if (notes == null || notes.trim().length() < 10) {
            context.addMessage(notesFieldId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Notes must be at least 10 characters long.", null));
            return null;
        }
        if (notes.trim().length() > 100) {
            context.addMessage(notesFieldId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Notes must not exceed 100 characters.", null));
            return null;
        }

        // Final update to DB
        message = availabilityDao.updateAvailability(selectedAvailability);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Availability updated successfully.", null));
        return "listAvailabilityByDate";
    }


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
