package com.java.ejb.provider.model;

import java.io.Serializable;
import java.util.Date;

public class DoctorAvailability implements Serializable {
		
    private String availabilityId;
    private String doctorId;
    private Date availableDate;
    private Date startTime;
    private Date endTime;
    private SlotType slotType;
    private boolean isRecurring;
    private String notes;
    private int totalSlots;
    private Date createdAt;
    private Date updatedAt;
    

	public String getAvailabilityId() {
		return availabilityId;
	}

	public void setAvailabilityId(String availabilityId) {
		this.availabilityId = availabilityId;
	}
	
	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public Date getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(Date availableDate) {
		this.availableDate = availableDate;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public SlotType getSlotType() {
		return slotType;
	}

	public void setSlotType(SlotType slotType) {
		this.slotType = slotType;
	}

	public boolean isRecurring() {
	    return isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
	    this.isRecurring = isRecurring;
	}


	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getTotalSlots() {
		return totalSlots;
	}

	public void setTotalSlots(int totalSlots) {
		this.totalSlots = totalSlots;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public DoctorAvailability(String availabilityId, String doctorId, Date availableDate, Date startTime, Date endTime,
			SlotType slotType, boolean isRecurring, String notes, int totalSlots, Date createdAt, Date updatedAt) {
		this.availabilityId = availabilityId;
		this.doctorId = doctorId;
		this.availableDate = availableDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.slotType = slotType;
		this.isRecurring = isRecurring;
		this.notes = notes;
		this.totalSlots = totalSlots;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public DoctorAvailability() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	   
    
}
