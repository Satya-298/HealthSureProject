package com.java.jsf.provider.daoImpl;

import java.util.Date;
import java.util.List;
import java.util.Collections;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.java.jsf.provider.dao.AppointmentDao;
import com.java.jsf.provider.model.Appointment;
import com.java.jsf.provider.model.AppointmentStatus;
import com.java.jsf.util.SessionHelper;

public class AppointmentDaoImpl implements AppointmentDao {

    private static final SessionFactory sf;

    static {
        sf = SessionHelper.getSessionFactory();
    }

    @Override
    public List<Appointment> getAllAppointments() {
        Session session = null;
        try {
            session = sf.openSession();
            Query query = session.createQuery("FROM Appointment");
            return query.list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Appointment> getAppointmentsByStatus(String status) {
        Session session = null;
        try {
            session = sf.openSession();
            Query query = session.createQuery("FROM Appointment WHERE status = :status");
            query.setParameter("status", AppointmentStatus.valueOf(status));
            return query.list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Appointment getAppointmentById(String appointmentId) {
        Session session = null;
        try {
            session = sf.openSession();
            return (Appointment) session.get(Appointment.class, appointmentId);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean approveAppointment(String appointmentId) {
        return updateStatus(appointmentId, AppointmentStatus.BOOKED);
    }

    @Override
    public boolean completeAppointment(String appointmentId) {
        return updateStatus(appointmentId, AppointmentStatus.COMPLETED);
    }

    @Override
    public boolean cancelAppointment(String appointmentId) {
        return updateStatus(appointmentId, AppointmentStatus.CANCELLED);
    }

    private boolean updateStatus(String appointmentId, AppointmentStatus newStatus) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();

            Appointment appt = (Appointment) session.get(Appointment.class, appointmentId);
            if (appt == null) return false;

            appt.setStatus(newStatus);
            Date now = new Date();

            if (newStatus == AppointmentStatus.BOOKED) {
                appt.setBookedAt(now);
            } else if (newStatus == AppointmentStatus.COMPLETED) {
                appt.setCompletedAt(now);
            } else if (newStatus == AppointmentStatus.CANCELLED) {
                appt.setCancelledAt(now);
            }

            session.update(appt);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorAndStatus(String doctorId, String status) {
        Session session = null;
        try {
            AppointmentStatus stat;
            try {
                stat = AppointmentStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                return Collections.emptyList(); // Java 8 compatible replacement for List.of()
            }

            session = sf.openSession();
            Query query = session.createQuery("FROM Appointment WHERE doctor.doctorId = :docId AND status = :status");
            query.setParameter("docId", doctorId);
            query.setParameter("status", stat);
            return query.list();
        } finally {
            if (session != null) session.close();
        }
    }
}
