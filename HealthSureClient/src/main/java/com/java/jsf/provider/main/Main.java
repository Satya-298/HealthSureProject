package com.java.jsf.provider.main;

import java.util.List;

import com.java.jsf.provider.dao.AppointmentDao;
import com.java.jsf.provider.daoImpl.AppointmentDaoImpl;
import com.java.jsf.provider.model.Appointment;

public class Main {

    public static void main(String[] args) {

        AppointmentDao dao = new AppointmentDaoImpl();

        System.out.println("----- All Appointments -----");
        List<Appointment> all = dao.getAllAppointments();
        for (Appointment appt : all) {
            System.out.println(appt.getAppointmentId() + " - " + appt.getStatus());
        }

        System.out.println("\n----- PENDING Appointments -----");
        List<Appointment> pending = dao.getAppointmentsByStatus("PENDING");
        for (Appointment appt : pending) {
            System.out.println(appt.getAppointmentId() + " - " + appt.getStatus());
        }

        System.out.println("\n----- Approving Appointment APPT106 -----");
        boolean approved = dao.approveAppointment("APPT106");
        System.out.println("Approved? " + approved);

        System.out.println("\n----- Completing Appointment APPT108 -----");
        boolean completed = dao.completeAppointment("APPT108");
        System.out.println("Completed? " + completed);

        System.out.println("\n----- Cancelling Appointment APPT107 -----");
        boolean cancelled = dao.cancelAppointment("APPT107");
        System.out.println("Cancelled? " + cancelled);

//        System.out.println("\n----- Appointment by ID: APPT108 -----");
//        Appointment appt = dao.getAppointmentById("APPT108");
//        System.out.println(appt.getAppointmentId() + " - " + appt.getStatus());
    }
}
