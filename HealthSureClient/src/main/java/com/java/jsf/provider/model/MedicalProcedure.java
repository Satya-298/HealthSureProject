package com.java.jsf.provider.model;

import java.io.Serializable;
import java.util.Date;

import com.java.jsf.recipient.model.Recipient;

public class MedicalProcedure implements Serializable {
	
	private String procedureId;
    private Appointment appointment; 
    private Recipient recipient;
    private Providers provider;
    private Doctors doctor;
    private Date procedureDate;
    private String diagnosis;
    private String recommendations;
    private Date fromDate;
    private Date toDate;
    private Date createdAt;
    
	public String getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	public Recipient getRecipient() {
		return recipient;
	}
	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}
	public Providers getProvider() {
		return provider;
	}
	public void setProvider(Providers provider) {
		this.provider = provider;
	}
	public Doctors getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctors doctor) {
		this.doctor = doctor;
	}
	public Date getProcedureDate() {
		return procedureDate;
	}
	public void setProcedureDate(Date procedureDate) {
		this.procedureDate = procedureDate;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getRecommendations() {
		return recommendations;
	}
	public void setRecommendations(String recommendations) {
		this.recommendations = recommendations;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public MedicalProcedure(String procedureId, Appointment appointment, Recipient recipient, Providers provider,
			Doctors doctor, Date procedureDate, String diagnosis, String recommendations, Date fromDate, Date toDate,
			Date createdAt) {
		this.procedureId = procedureId;
		this.appointment = appointment;
		this.recipient = recipient;
		this.provider = provider;
		this.doctor = doctor;
		this.procedureDate = procedureDate;
		this.diagnosis = diagnosis;
		this.recommendations = recommendations;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.createdAt = createdAt;
	}
	
	public MedicalProcedure() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
