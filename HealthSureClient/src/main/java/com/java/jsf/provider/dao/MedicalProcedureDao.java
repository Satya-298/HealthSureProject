package com.java.jsf.provider.dao;

import java.util.List;

import com.java.jsf.provider.model.MedicalProcedure;

public interface MedicalProcedureDao {
	
	 List<MedicalProcedure> searchByHid(String hid);

	 List<MedicalProcedure> searchByName(String namePart);

	 List<MedicalProcedure> searchByMobile(String mobile);
	 
	 List<MedicalProcedure> searchByNameStartsWith(String prefix);

	 List<MedicalProcedure> searchByNameContains(String substring);


}
