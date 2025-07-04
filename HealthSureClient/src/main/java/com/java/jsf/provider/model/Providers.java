package com.java.jsf.provider.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Providers implements Serializable {
    private String providerId;
    private String providerName;
    private String hospitalName;
    private String email;
    private String password;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private LoginStatus status;
    private Date createdAt;

    private Set<Doctors> doctors;
    private Set<MedicalProcedure> medicalProcedure;


	public String getProviderId() {
		return providerId;
	}


	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}


	public String getProviderName() {
		return providerName;
	}


	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}


	public String getHospitalName() {
		return hospitalName;
	}


	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public LoginStatus getStatus() {
		return status;
	}


	public void setStatus(LoginStatus status) {
		this.status = status;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public Set<Doctors> getDoctors() {
		return doctors;
	}


	public void setDoctors(Set<Doctors> doctors) {
		this.doctors = doctors;
	}
	
	public Set<MedicalProcedure> getMedicalProcedure() {
		return medicalProcedure;
	}

	public void setMedicalProcedure(Set<MedicalProcedure> medicalProcedure) {
		this.medicalProcedure = medicalProcedure;
	}

	public Providers(String providerId, String providerName, String hospitalName, String email, String password,
			String address, String city, String state, String zipCode, LoginStatus status, Date createdAt,
			Set<Doctors> doctors, Set<MedicalProcedure> medicalProcedure) {
		this.providerId = providerId;
		this.providerName = providerName;
		this.hospitalName = hospitalName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.status = status;
		this.createdAt = createdAt;
		this.doctors = doctors;
		this.medicalProcedure = medicalProcedure;
	}

	public Providers() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
