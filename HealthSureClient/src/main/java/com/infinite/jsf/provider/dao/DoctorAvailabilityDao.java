package com.infinite.jsf.provider.dao;

import java.util.Date;
import java.util.List;

import com.infinite.jsf.provider.model.DoctorAvailability;
import com.infinite.jsf.provider.model.Doctors;

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
