package com.java.jsf.provider.daoImpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.java.jsf.provider.dao.DoctorAvailabilityDao;
import com.java.jsf.provider.model.DoctorAvailability;
import com.java.jsf.util.SessionHelper;

public class DoctorAvailabilityDaoImpl implements DoctorAvailabilityDao {
	
	SessionFactory sf;
	Session session;
	
	@Override
    public String addAvailability(DoctorAvailability availability) {
        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        Transaction tx = session.beginTransaction();

        // Generate unique availability ID
        String newId = generateAvailabilityId();
        availability.setAvailabilityId(newId);

        session.save(availability);
        tx.commit();
        session.close();

        return "";
    }

    @Override
    public List<DoctorAvailability> getAvailabilityByDoctor(String doctorId) {
        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        Query query = session.getNamedQuery("AvailabilityByDoctor");
        query.setParameter("doctorId", doctorId);
        List<DoctorAvailability> list = query.list();
        session.close();
        return list;
    }

    // Helper method to generate unique availability ID
    public String generateAvailabilityId() {
    	Session session = null;
        try {
        	sf = SessionHelper.getSessionFactory();
            session = sf.openSession();
            Query query = session.getNamedQuery("AvailabilityId");
            String latestId = (String) query.uniqueResult();

            if (latestId == null) {
                return "A001";
            } else {
                int num = Integer.parseInt(latestId.substring(1));
                return "A" + String.format("%03d", num + 1);
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

}


