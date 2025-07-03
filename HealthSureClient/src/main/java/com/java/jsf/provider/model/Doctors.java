package com.java.jsf.provider.model;

import java.util.Set;

public class Doctors {
    private String doctorId;
    private Providers provider;
    private String doctorName;
    private String qualification;
    private String specialization;
    private String licenseNo;
    private String email;
    private String address;
    private Gender gender;
    private DoctorType type;
    private DoctorStatus doctorStatus;

    private Set<DoctorAvailability> availabilities;

	
	public String getDoctorId() {
		return doctorId;
	}


	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}


	public Providers getProvider() {
		return provider;
	}


	public void setProvider(Providers provider) {
		this.provider = provider;
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


	public Set<DoctorAvailability> getAvailabilities() {
		return availabilities;
	}


	public void setAvailabilities(Set<DoctorAvailability> availabilities) {
		this.availabilities = availabilities;
	}

	public Doctors(String doctorId, Providers provider, String doctorName, String qualification, String specialization,
			String licenseNo, String email, String address, Gender gender, DoctorType type, DoctorStatus doctorStatus,
			Set<DoctorAvailability> availabilities) {
		this.doctorId = doctorId;
		this.provider = provider;
		this.doctorName = doctorName;
		this.qualification = qualification;
		this.specialization = specialization;
		this.licenseNo = licenseNo;
		this.email = email;
		this.address = address;
		this.gender = gender;
		this.type = type;
		this.doctorStatus = doctorStatus;
		this.availabilities = availabilities;
	}


	public Doctors() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
