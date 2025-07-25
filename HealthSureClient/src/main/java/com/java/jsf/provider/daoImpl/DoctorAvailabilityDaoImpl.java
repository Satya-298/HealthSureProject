package com.java.jsf.provider.daoImpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.java.jsf.provider.dao.DoctorAvailabilityDao;
import com.java.jsf.provider.model.DoctorAvailability;
import com.java.jsf.provider.model.Doctors;
import com.java.jsf.util.SessionHelper;

public class DoctorAvailabilityDaoImpl implements DoctorAvailabilityDao {
	
	SessionFactory sf;
	Session session;
	
	@Override
    public String addAvailability(DoctorAvailability availability) {
        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.save(availability);
        tx.commit();
        session.close();

        return "";
    }

    @Override
    public List<DoctorAvailability> getAvailabilityByDoctor(String doctorId) {
        if (doctorId == null || doctorId.trim().isEmpty()) 
        	return null;

        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        Query query = session.getNamedQuery("AvailabilityByDoctor");
        query.setParameter("doctorId", doctorId);
        List<DoctorAvailability> list = query.list();
        session.close();
        return list;
    }

    @Override
    public String generateAvailabilityId() {
    	Session session = null;
        try {
        	sf = SessionHelper.getSessionFactory();
            session = sf.openSession();
            Query query = session.getNamedQuery("AvailabilityId");
            String latestId = (String) query.uniqueResult();

            if (latestId == null) {
                return "AVAIL001";
            } else {
                int num = Integer.parseInt(latestId.substring(5));
                return "AVAIL" + String.format("%03d", num + 1);
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<DoctorAvailability> getAvailabilityByDate(Date selectedDate) {
    	sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        Query query = session.getNamedQuery("AvailabilityDate");
        query.setParameter("selectedDate", selectedDate);
        List<DoctorAvailability> list = query.list();
        session.close();
        return list;
    }

	@Override
	public String updateAvailability(DoctorAvailability availability) {
		System.out.println("in update method");
		sf = SessionHelper.getSessionFactory();
		session = sf.openSession();
	    Transaction tx = session.beginTransaction();
	    session.update(availability);
	    tx.commit();
	    session.close();
		System.out.println("executed update method");

	    return "";
	}

	@Override
    public Doctors getDoctorById(String doctorId) {
        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        Doctors doctor = (Doctors) session.get(Doctors.class, doctorId);
        session.close();
        return doctor;
    }
	
	@Override
	public boolean existsAvailability(String doctorId, Date availableDate) {
	    sf = SessionHelper.getSessionFactory();
	    session = sf.openSession();

	    String hql = "SELECT COUNT(*) FROM DoctorAvailability a " +
	                 "WHERE a.doctor.doctorId = :doctorId " +
	                 "AND a.availableDate = :availableDate";

	    Query query = session.createQuery(hql);
	    query.setParameter("doctorId", doctorId);
	    query.setParameter("availableDate", availableDate);

	    Long count = (Long) query.uniqueResult();

	    session.close();

	    return count != null && count > 0;
	}
	
//	@Override
//	public boolean deleteAvailabilityById(String availabilityId) {
//	    sf = SessionHelper.getSessionFactory();
//	    session = sf.openSession();
//	    Transaction tx = null;
//	    try {
//	        tx = session.beginTransaction();
//	        DoctorAvailability availability = (DoctorAvailability) session.get(DoctorAvailability.class, availabilityId);
//
//	        if (availability != null) {
//	            session.delete(availability);
//	            tx.commit();
//	            return true;
//	        } else {
//	            if (tx != null) tx.rollback();
//	            return false;
//	        }
//	    } catch (Exception e) {
//	        if (tx != null) tx.rollback();
//	        e.printStackTrace();
//	        return false;
//	    } finally {
//	        if (session != null) session.close();
//	    }
//	}
}


