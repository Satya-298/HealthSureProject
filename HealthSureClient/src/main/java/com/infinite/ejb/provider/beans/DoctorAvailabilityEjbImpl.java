package com.infinite.ejb.provider.beans;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import com.infinite.ejb.provider.model.DoctorAvailability;

public class DoctorAvailabilityEjbImpl {

    static DoctorAvailabilityJdbcBeanRemote remote;

    static {
        try {
            remote = DoctorAvailabilityRemoteHelper.lookupRemoteStatelessDoctorAvailability();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String addAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException, NamingException {
        
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("availabilityId", availability.getAvailabilityId());  // if generated externally
        sessionMap.put("doctorId", availability.getDoctorId());
        sessionMap.put("availableDate", availability.getAvailableDate());

        remote.addAvailability(availability);
        return "Doctor Availability Added Successfully";
    }

    public List<DoctorAvailability> getAvailabilityByDoctor(String doctorId)
            throws ClassNotFoundException, SQLException, NamingException {
        return remote.getAvailabilityByDoctor(doctorId);
    }

    public List<DoctorAvailability> getAvailabilityByDate(Date selectedDate) throws ClassNotFoundException, SQLException, NamingException {
        return remote.getAvailabilityByDate(selectedDate);
    }

    public String updateAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException, NamingException {
        
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("updatedAvailabilityId", availability.getAvailabilityId());
        sessionMap.put("updatedDate", availability.getAvailableDate());

        remote.updateAvailability(availability);
        return "Doctor Availability Updated Successfully";
    }
    
    public boolean isDoctorUnderProvider(String doctorId)
            throws ClassNotFoundException, SQLException, NamingException {
        return remote.isDoctorUnderProvider(doctorId);
    }
}
