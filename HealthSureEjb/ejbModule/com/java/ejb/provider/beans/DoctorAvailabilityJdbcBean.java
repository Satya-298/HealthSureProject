package com.java.ejb.provider.beans;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.java.ejb.provider.dao.DoctorAvailabilityDao;
import com.java.ejb.provider.daoImpl.DoctorAvailabilityDaoImpl;
import com.java.ejb.provider.model.DoctorAvailability;

@Stateless
@Remote(DoctorAvailabilityJdbcBeanRemote.class)
public class DoctorAvailabilityJdbcBean implements DoctorAvailabilityJdbcBeanRemote {

    static DoctorAvailabilityDao daoImpl;

    static {
        daoImpl = new DoctorAvailabilityDaoImpl();
    }

    public DoctorAvailabilityJdbcBean() {
        // Default constructor
    }

    @Override
    public String addAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException {
        return daoImpl.addAvailability(availability);
    }

    @Override
    public List<DoctorAvailability> getAvailabilityByDoctor(String doctorId) throws ClassNotFoundException, SQLException {
        return daoImpl.getAvailabilityByDoctor(doctorId);
    }

    @Override
    public List<DoctorAvailability> getAvailabilityByDate(Date selectedDate) throws ClassNotFoundException, SQLException {
        return daoImpl.getAvailabilityByDate(selectedDate);
    }

    @Override
    public String updateAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException {
        return daoImpl.updateAvailability(availability);
    }

	@Override
	public boolean isDoctorUnderProvider(String doctorId) throws ClassNotFoundException, SQLException {
        return daoImpl.isDoctorUnderProvider(doctorId);
	}
}
