package com.java.jsf.provider.dao;

import java.util.List;

import com.java.jsf.provider.model.MedicalProcedure;
import com.java.jsf.provider.model.Prescription;

public interface MedicalHistoryDao {
	 
	 List<MedicalProcedure> searchByHid(String doctorId, String hid);
	 
	 List<MedicalProcedure> searchByName(String doctorId, String namePart);
	 
	 List<MedicalProcedure> searchByMobile(String doctorId, String mobile);

	 List<MedicalProcedure> searchByNameStartsWith(String doctorId, String prefix);

	 List<MedicalProcedure> searchByNameContains(String doctorId, String substring);

	 List<MedicalProcedure> getAllProceduresByDoctor(String doctorId);
	 
	 boolean doctorExists(String doctorId);

	 List<Prescription> getPrescriptionsByProcedureId(String procedureId);
	 
}
