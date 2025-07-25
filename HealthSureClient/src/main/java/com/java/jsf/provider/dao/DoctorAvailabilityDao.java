package com.java.jsf.provider.dao;

import com.java.jsf.provider.model.DoctorAvailability;
import com.java.jsf.provider.model.Doctors;

import java.util.Date;
import java.util.List;

public interface DoctorAvailabilityDao {

	String addAvailability(DoctorAvailability availability);
    List<DoctorAvailability> getAvailabilityByDoctor(String doctorId);
    List<DoctorAvailability> getAvailabilityByDate(Date selectedDate);
    String updateAvailability(DoctorAvailability availability);
	String generateAvailabilityId();
	Doctors getDoctorById(String doctorId);
	boolean existsAvailability(String doctorId, Date availableDate);
//	boolean deleteAvailabilityById(String availabilityId);


}
