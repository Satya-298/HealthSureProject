package com.java.jsf.provider.controller;

import java.util.List;

import com.java.jsf.provider.dao.MedicalProcedureDao;
import com.java.jsf.provider.daoImpl.MedicalProcedureDaoImpl;
import com.java.jsf.provider.model.MedicalProcedure;

public class MedicalProcedureController {

    private String searchKey;
    private List<MedicalProcedure> searchResults;
    private MedicalProcedureDao medicalProcedureDao;
    private MedicalProcedure medicalProcedure;


    public MedicalProcedureController() {
    	medicalProcedureDao = new MedicalProcedureDaoImpl();
    }

    public String searchProcedures() {
        if (searchKey == null || searchKey.trim().isEmpty()) {
            searchResults = null;
            return null; 
        }

        searchResults = medicalProcedureDao.searchMedicalProcedure(searchKey.trim());
        return null; 
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<MedicalProcedure> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<MedicalProcedure> searchResults) {
        this.searchResults = searchResults;
    }

    public MedicalProcedureDao getMedicalProcedureDao() {
        return medicalProcedureDao;
    }

    public void setMedicalProcedureDao(MedicalProcedureDao medicalProcedureDao) {
        this.medicalProcedureDao = medicalProcedureDao;
    }

	public MedicalProcedure getMedicalProcedure() {
		return medicalProcedure;
	}

	public void setMedicalProcedure(MedicalProcedure medicalProcedure) {
		this.medicalProcedure = medicalProcedure;
	}
    
}
