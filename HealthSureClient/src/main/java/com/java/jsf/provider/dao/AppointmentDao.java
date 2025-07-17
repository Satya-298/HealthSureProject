package com.java.jsf.provider.dao;

import java.util.List;
import com.java.jsf.provider.model.Appointment;
import com.java.jsf.provider.model.AppointmentStatus;

public interface AppointmentDao {

    List<Appointment> getAllAppointments();

    List<Appointment> getAppointmentsByStatus(String completed);

    Appointment getAppointmentById(String appointmentId);

	List<Appointment> getAppointmentsByDoctorAndStatus(String doctorId, String status);

	boolean approveAppointment(String appointmentId);

	boolean cancelAppointment(String appointmentId);

	boolean completeAppointment(String appointmentId);
    
    
}
