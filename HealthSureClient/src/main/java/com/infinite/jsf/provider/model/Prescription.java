package com.infinite.jsf.provider.model;
 
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.infinite.jsf.recipient.model.Recipient;
 
public class Prescription implements Serializable{
 
    private String prescriptionId;
 
    // Foreign key object mappings
    private MedicalProcedure medicalProcedure;    // mapped from procedure_id
    private Recipient recipient;           // mapped from h_id
    private Providers provider;             // mapped from provider_id
    private Doctors doctor;                 // mapped from doctor_id
 
    private Date writtenOn;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    
    private Set<PrescribedMedicines> prescribedMedicines;
    private Set<ProcedureTest> tests;
 
    public String getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(String prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public MedicalProcedure getMedicalProcedure() {
		return medicalProcedure;
	}

	public void setMedicalProcedure(MedicalProcedure medicalProcedure) {
		this.medicalProcedure = medicalProcedure;
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

	public Date getWrittenOn() {
		return writtenOn;
	}

	public void setWrittenOn(Date writtenOn) {
		this.writtenOn = writtenOn;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Set<PrescribedMedicines> getPrescribedMedicines() {
		return prescribedMedicines;
	}

	public void setPrescribedMedicines(Set<PrescribedMedicines> prescribedMedicines) {
		this.prescribedMedicines = prescribedMedicines;
	}

	public Set<ProcedureTest> getTests() {
		return tests;
	}

	public void setTests(Set<ProcedureTest> tests) {
		this.tests = tests;
	}

	public Prescription() {
        this.medicalProcedure = new MedicalProcedure();
        this.recipient = new Recipient();
        this.provider = new Providers();
        this.doctor = new Doctors();
    }
    
    @Override
    public String toString() {
        return "Prescription [" +
                "prescriptionId=" + prescriptionId +
                ", procedureId=" + (medicalProcedure != null ? medicalProcedure.getProcedureId() : null) +
                ", recipientId=" + (recipient != null ? recipient.gethId() : null) +
                ", providerId=" + (provider != null ? provider.getProviderId() : null) +
                ", doctorId=" + (doctor != null ? doctor.getDoctorId() : null) +
                ", writtenOn=" + writtenOn +
                ", createdAt=" + createdAt +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", prescribedMedicineCount=" + (prescribedMedicines != null ? prescribedMedicines.size() : 0) +
                ", testCount=" + (tests != null ? tests.size() : 0) +
                "]";
    }
}