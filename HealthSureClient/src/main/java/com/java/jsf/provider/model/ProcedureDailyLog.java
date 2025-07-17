package com.java.jsf.provider.model;
 
import java.io.Serializable;
import java.util.Date;
 
public class ProcedureDailyLog implements Serializable {
 
    private String logId;
    private MedicalProcedure medicalProcedure;  
    private Date logDate;
    private String vitals;     
    private String notes;      
    private Date createdAt;
 
    public ProcedureDailyLog() {
        this.medicalProcedure = new MedicalProcedure();
    }
 
    // Getters and Setters
    public String getLogId() {
        return logId;
    }
 
    public void setLogId(String logId) {
        this.logId = logId;
    }
 
    public MedicalProcedure getMedicalProcedure() {
        return medicalProcedure;
    }
 
    public void setMedicalProcedure(MedicalProcedure medicalProcedure) {
        this.medicalProcedure = medicalProcedure;
    }
 
    public Date getLogDate() {
        return logDate;
    }
 
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
 
    public String getVitals() {
        return vitals;
    }
 
    public void setVitals(String vitals) {
        this.vitals = vitals;
    }
 
    public String getNotes() {
        return notes;
    }
 
    public void setNotes(String notes) {
        this.notes = notes;
    }
 
    public Date getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
 
    @Override
    public String toString() {
        return "ProcedureDailyLog [logId=" + logId + ", medicalProcedure=" + medicalProcedure + ", logDate=" + logDate
                + ", vitals=" + vitals + ", notes=" + notes + ", createdAt=" + createdAt + "]";
    }
}