package com.java.ejb.provider.model;

import java.io.Serializable;
import java.util.Date;


public class Appointment implements Serializable{

    private static final long serialVersionUID = 1L;

    private String appointmentId;
    private String doctorId;
    private String recipientId;
    private String availabilityId;
    private String providerId;
    private Date requestedAt;
    private Date bookedAt;
    private Date cancelledAt;
    private Date completedAt;
    private AppointmentStatus status;
    private String notes;
    
	public String getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}
	public String getAvailabilityId() {
		return availabilityId;
	}
	public void setAvailabilityId(String availabilityId) {
		this.availabilityId = availabilityId;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public Date getRequestedAt() {
		return requestedAt;
	}
	public void setRequestedAt(Date requestedAt) {
		this.requestedAt = requestedAt;
	}
	public Date getBookedAt() {
		return bookedAt;
	}
	public void setBookedAt(Date bookedAt) {
		this.bookedAt = bookedAt;
	}
	public Date getCancelledAt() {
		return cancelledAt;
	}
	public void setCancelledAt(Date cancelledAt) {
		this.cancelledAt = cancelledAt;
	}
	public Date getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}
	public AppointmentStatus getStatus() {
		return status;
	}
	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Appointment(String appointmentId, String doctorId, String recipientId, String availabilityId,
			String providerId, Date requestedAt, Date bookedAt, Date cancelledAt, Date completedAt,
			AppointmentStatus status, String notes) {
		this.appointmentId = appointmentId;
		this.doctorId = doctorId;
		this.recipientId = recipientId;
		this.availabilityId = availabilityId;
		this.providerId = providerId;
		this.requestedAt = requestedAt;
		this.bookedAt = bookedAt;
		this.cancelledAt = cancelledAt;
		this.completedAt = completedAt;
		this.status = status;
		this.notes = notes;
	}
	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}

}
