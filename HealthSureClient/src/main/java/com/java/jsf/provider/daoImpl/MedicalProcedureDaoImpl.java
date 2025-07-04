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
	public List<MedicalProcedure> searchMedicalProcedure(String searchKey) {
	    sf = SessionHelper.getSessionFactory();
	    session = sf.openSession();
	    Transaction tx = session.beginTransaction();

	    // Get the named query defined in hbm.xml
	    Query query = session.getNamedQuery("searchMedicalProcedure");
	    
	    // Set parameters correctly
	    query.setParameter("searchKey", searchKey);
	    query.setParameter("searchLike", "%" + searchKey + "%");

	    List<MedicalProcedure> resultList = query.list();
	    
	    tx.commit();
	    session.close();
	    return resultList;
	}


}
