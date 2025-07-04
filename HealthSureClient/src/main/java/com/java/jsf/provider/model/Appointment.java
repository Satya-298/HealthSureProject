package com.java.jsf.provider.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.java.jsf.recipient.model.Recipient;

public class Appointment implements Serializable {

    private String appointmentId;
    private Doctors doctor;
    private Recipient recipient;
    private DoctorAvailability availability;
    private Providers provider;
    private Date requestedAt;
    private Date bookedAt;
    private Date cancelledAt;
    private Date completedAt;
    private AppointmentStatus status;
    private String notes;
    
    private Set<MedicalProcedure> medicalProcedure;

    
	public String getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Doctors getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctors doctor) {
		this.doctor = doctor;
	}
	public Recipient getRecipient() {
		return recipient;
	}
	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}
	public DoctorAvailability getAvailability() {
		return availability;
	}
	public void setAvailability(DoctorAvailability availability) {
		this.availability = availability;
	}
	public Providers getProvider() {
		return provider;
	}
	public void setProvider(Providers provider) {
		this.provider = provider;
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
	
	public Set<MedicalProcedure> getMedicalProcedure() {
		return medicalProcedure;
	}
	public void setMedicalProcedure(Set<MedicalProcedure> medicalProcedure) {
		this.medicalProcedure = medicalProcedure;
	}
	
	public Appointment(String appointmentId, Doctors doctor, Recipient recipient, DoctorAvailability availability,
			Providers provider, Date requestedAt, Date bookedAt, Date cancelledAt, Date completedAt,
			AppointmentStatus status, String notes, Set<MedicalProcedure> medicalProcedure) {
		this.appointmentId = appointmentId;
		this.doctor = doctor;
		this.recipient = recipient;
		this.availability = availability;
		this.provider = provider;
		this.requestedAt = requestedAt;
		this.bookedAt = bookedAt;
		this.cancelledAt = cancelledAt;
		this.completedAt = completedAt;
		this.status = status;
		this.notes = notes;
		this.medicalProcedure = medicalProcedure;
	}
	
	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}

}
