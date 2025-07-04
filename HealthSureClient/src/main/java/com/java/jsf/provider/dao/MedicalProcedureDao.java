package com.java.jsf.provider.dao;

import java.util.List;

import com.java.jsf.provider.model.MedicalProcedure;

public interface MedicalProcedureDao {
	
	 List<MedicalProcedure> searchMedicalProcedure(String hidOrNameOrPhone);

}
