package com.infinite.jsf.provider.daoImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.infinite.jsf.provider.dao.AppointmentDao;
import com.infinite.jsf.provider.model.Appointment;
import com.infinite.jsf.provider.model.AppointmentStatus;
import com.infinite.jsf.util.SessionHelper;

public class AppointmentDaoImpl implements AppointmentDao {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = SessionHelper.getSessionFactory();
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorAndStatus(String doctorId, String status) {
        Session session = sessionFactory.openSession();
        String hql = "FROM Appointment a WHERE a.doctor.doctorId = :doctorId AND a.status = :status";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        query.setParameter("status", AppointmentStatus.valueOf(status.toUpperCase()));
        List<Appointment> result = query.list();
        session.close();
        return result;
    }

    @Override
    public boolean markAppointmentAsBooked(String appointmentId, Timestamp bookedAt) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "UPDATE Appointment a SET a.status = :status, a.bookedAt = :bookedAt WHERE a.appointmentId = :appointmentId AND a.status = :pendingStatus";
        Query query = session.createQuery(hql);
        query.setParameter("status", AppointmentStatus.BOOKED);
        query.setParameter("bookedAt", bookedAt);
        query.setParameter("appointmentId", appointmentId);
        query.setParameter("pendingStatus", AppointmentStatus.PENDING);
        int updated = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return updated > 0;
    }

    @Override
    public boolean cancelPendingAppointmentByDoctor(String appointmentId, Timestamp cancelledAt) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "UPDATE Appointment a SET a.status = :status, a.cancelledAt = :cancelledAt WHERE a.appointmentId = :appointmentId AND a.status = :pendingStatus";
        Query query = session.createQuery(hql);
        query.setParameter("status", AppointmentStatus.CANCELLED);
        query.setParameter("cancelledAt", cancelledAt);
        query.setParameter("appointmentId", appointmentId);
        query.setParameter("pendingStatus", AppointmentStatus.PENDING);
        int updated = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return updated > 0;
    }

	@Override
	public Appointment getAppointmentById(String appointmentId) {
		Session session = sessionFactory.openSession();
	    Appointment appointment = null;
	    try {
	        appointment = (Appointment) session.get(Appointment.class, appointmentId);
	    } finally {
	        session.close();
	    }
	    return appointment;
	}
	
	@Override
	public List<Appointment> getAllAppointments() {
	    Session session = sessionFactory.openSession();
	    String hql = "FROM Appointment a JOIN FETCH a.availability JOIN FETCH a.doctor";
	    Query query = session.createQuery(hql);
	    List<Appointment> list = query.list();
	    session.close();
	    return list;
	}


	@Override
	public List<Appointment> getCompletedAppointments() {
	    Session session = sessionFactory.openSession();
	    String hql = "FROM Appointment a " +
	                 "JOIN FETCH a.recipient " +
	                 "JOIN FETCH a.availability " +
	                 "JOIN FETCH a.doctor " +
	                 "WHERE a.status = :status";
	    Query query = session.createQuery(hql);
	    query.setParameter("status", AppointmentStatus.COMPLETED);
	    List<Appointment> list = query.list();
	    session.close();
	    return list;
	}
	
}
