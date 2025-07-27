package com.infinite.ejb.provider.beans;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import com.infinite.ejb.provider.model.DoctorAvailability;

@Remote
public interface DoctorAvailabilityJdbcBeanRemote {
    String addAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException;
    List<DoctorAvailability> getAvailabilityByDoctor(String doctorId) throws ClassNotFoundException, SQLException;
    List<DoctorAvailability> getAvailabilityByDate(Date selectedDate) throws ClassNotFoundException, SQLException;
    String updateAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException;
	boolean isDoctorUnderProvider(String doctorId) throws ClassNotFoundException, SQLException;

}
