package com.java.ejb.provider.model;

import java.io.Serializable;

public class Doctors implements Serializable{
    
    private static final long serialVersionUID = 1L;

	private String doctorId;
    private String providerId;
    private String doctorName;
    private String qualification;
    private String specialization;
    private String licenseNo;
    private String email;
    private String address;
    private Gender gender;
    private DoctorType type;
    private DoctorStatus doctorStatus;

	public String getDoctorId() {
		return doctorId;
	}


	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}


	public String getProviderId() {
		return providerId;
	}


	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}


	public String getDoctorName() {
		return doctorName;
	}


	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}


	public String getQualification() {
		return qualification;
	}


	public void setQualification(String qualification) {
		this.qualification = qualification;
	}


	public String getSpecialization() {
		return specialization;
	}


	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}


	public String getLicenseNo() {
		return licenseNo;
	}


	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public DoctorType getType() {
		return type;
	}


	public void setType(DoctorType type) {
		this.type = type;
	}


	public DoctorStatus getDoctorStatus() {
		return doctorStatus;
	}


	public void setDoctorStatus(DoctorStatus doctorStatus) {
		this.doctorStatus = doctorStatus;
	}


	public Doctors(String doctorId, String providerId, String doctorName, String qualification, String specialization,
			String licenseNo, String email, String address, Gender gender, DoctorType type, DoctorStatus doctorStatus) {
		this.doctorId = doctorId;
		this.providerId = providerId;
		this.doctorName = doctorName;
		this.qualification = qualification;
		this.specialization = specialization;
		this.licenseNo = licenseNo;
		this.email = email;
		this.address = address;
		this.gender = gender;
		this.type = type;
		this.doctorStatus = doctorStatus;
	}


	public Doctors() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
