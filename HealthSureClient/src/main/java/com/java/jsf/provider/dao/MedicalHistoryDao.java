package com.java.jsf.provider.dao;

import java.util.List;

import com.java.jsf.provider.model.MedicalProcedure;
import com.java.jsf.provider.model.Prescription;

public interface MedicalHistoryDao {
	 
	List<MedicalProcedure> searchByHid(String doctorId, String hid, String procedureType);
    List<MedicalProcedure> searchByNameStartsWith(String doctorId, String prefix, String procedureType);
    List<MedicalProcedure> searchByNameContains(String doctorId, String substring, String procedureType);
    List<MedicalProcedure> searchByMobile(String doctorId, String mobile, String procedureType);
    List<MedicalProcedure> searchByDoctorIdWithDetails(String doctorId, String procedureType);
    boolean doctorExists(String doctorId);
    Prescription getPrescriptionWithDetails(String prescriptionId);
	MedicalProcedure getProcedureWithLogs(String procedureId);

    
}
