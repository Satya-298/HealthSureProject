package com.java.jsf.provider.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import com.java.jsf.provider.dao.AppointmentDao;
import com.java.jsf.provider.model.Appointment;

public class AppointmentController {

    private AppointmentDao appointmentDao;
    private List<Appointment> allAppointments;

    private String doctorId;
    private String status;
    private String statusMessage;

    // Load appointments by selected status
    public String loadAppointmentsByStatus() {
        if (doctorId == null || doctorId.trim().isEmpty() || status == null || status.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor ID and Status are required.", null));
            allAppointments = null;
            return null;
        }

        allAppointments = appointmentDao.getAppointmentsByDoctorAndStatus(doctorId.trim(), status);
        return null;
    }

    // Listener triggered by dropdown change
    public void statusChanged(ValueChangeEvent event) {
        status = (String) event.getNewValue();
        loadAppointmentsByStatus();
    }

    // Approve a pending appointment only if it has valid time slots
    public String approve(String appointmentId) {
        Appointment appt = appointmentDao.getAppointmentById(appointmentId);

        if (appt.getStart() == null || appt.getEnd() == null) {
            statusMessage = "Appointment " + appointmentId + " cannot be booked without a valid start and end time.";
            return null;
        }

        boolean success = appointmentDao.approveAppointment(appointmentId);
        statusMessage = success
            ? "Appointment " + appointmentId + " booked successfully."
            : "Failed to book appointment " + appointmentId + ".";

        loadAppointmentsByStatus();
        return null;
    }

    // Cancel only if no start and end time exists
    public String cancel(String appointmentId) {
        Appointment appt = appointmentDao.getAppointmentById(appointmentId);

        if (appt.getStart() != null || appt.getEnd() != null) {
            statusMessage = "Appointment " + appointmentId + " cannot be cancelled because it already has a start or end time.";
            return null;
        }

        boolean success = appointmentDao.cancelAppointment(appointmentId);
        statusMessage = success
            ? "Appointment " + appointmentId + " cancelled successfully."
            : "Failed to cancel appointment " + appointmentId + ".";

        loadAppointmentsByStatus();
        return null;
    }

    // Mark an appointment as completed
    public String complete(String appointmentId) {
        boolean success = appointmentDao.completeAppointment(appointmentId);
        statusMessage = success
            ? "Appointment " + appointmentId + " completed successfully."
            : "Failed to complete appointment " + appointmentId + ".";

        loadAppointmentsByStatus();
        return null;
    }

    // Load all appointments regardless of status
    public String loadAllAppointments() {
        allAppointments = appointmentDao.getAllAppointments();
        return null;
    }

    // -------------------- Getters and Setters --------------------

    public List<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public void setAllAppointments(List<Appointment> allAppointments) {
        this.allAppointments = allAppointments;
    }

    public AppointmentDao getAppointmentDao() {
        return appointmentDao;
    }

    public void setAppointmentDao(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
