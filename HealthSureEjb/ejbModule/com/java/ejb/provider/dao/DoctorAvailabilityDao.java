package com.java.ejb.provider.dao;

import com.java.ejb.provider.model.DoctorAvailability;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface DoctorAvailabilityDao {

	String addAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException;
    List<DoctorAvailability> getAvailabilityByDoctor(String doctorId) throws ClassNotFoundException, SQLException;
    List<DoctorAvailability> getAvailabilityByDate(Date selectedDate) throws ClassNotFoundException, SQLException;
    String updateAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException;
	boolean isDoctorUnderProvider(String doctorId) throws ClassNotFoundException, SQLException;

}
