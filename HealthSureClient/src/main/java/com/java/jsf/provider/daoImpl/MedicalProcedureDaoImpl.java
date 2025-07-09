package com.java.jsf.provider.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.java.jsf.provider.dao.MedicalProcedureDao;
import com.java.jsf.provider.model.MedicalProcedure;
import com.java.jsf.util.SessionHelper;

public class MedicalProcedureDaoImpl implements MedicalProcedureDao {
	
	SessionFactory sf;
	Session session;
	
	@Override
	public List<MedicalProcedure> searchByHid(String hid) {
	    sf = SessionHelper.getSessionFactory();
	    session = sf.openSession();
	    Query query = session.createQuery("FROM MedicalProcedure WHERE recipient.hId = :hid");
	    query.setParameter("hid", hid);
	    List<MedicalProcedure> result = query.list();
	    session.close();
	    return result;
	}

	@Override
	public List<MedicalProcedure> searchByName(String namePart) {
		sf = SessionHelper.getSessionFactory();
	    session = sf.openSession();
	    Query query = session.createQuery(
	    		"FROM MedicalProcedure WHERE lower(recipient.firstName) LIKE :name OR lower(recipient.lastName) LIKE :name"
	    		);
	    query.setParameter("name", namePart.toLowerCase() + "%");
	    List<MedicalProcedure> result = query.list();
	    session.close();
	    return result;
	}

	@Override
	public List<MedicalProcedure> searchByMobile(String mobile) {
		sf = SessionHelper.getSessionFactory();
	    session = sf.openSession();
	    Query query = session.createQuery("FROM MedicalProcedure WHERE recipient.mobile = :mobile");
	    query.setParameter("mobile", mobile);
	    List<MedicalProcedure> result = query.list();
	    session.close();
	    return result;
	}
	
	@Override
	public List<MedicalProcedure> searchByNameStartsWith(String prefix) {
		sf = SessionHelper.getSessionFactory();
	    session = sf.openSession();
	    String hql = "FROM MedicalProcedure mp WHERE LOWER(mp.recipient.firstName) LIKE :name OR LOWER(mp.recipient.lastName) LIKE :name";
	    Query query = session.createQuery(hql);
	    query.setParameter("name", prefix.toLowerCase() + "%");
	    return query.list();
	}

	@Override
	public List<MedicalProcedure> searchByNameContains(String substring) {
		sf = SessionHelper.getSessionFactory();
	    session = sf.openSession();
	    String hql = "FROM MedicalProcedure mp WHERE LOWER(mp.recipient.firstName) LIKE :name OR LOWER(mp.recipient.lastName) LIKE :name";
	    Query query = session.createQuery(hql);
	    query.setParameter("name", "%" + substring.toLowerCase() + "%");
	    return query.list();
	}




}
