package com.java.jsf.provider.daoImpl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.java.jsf.provider.dao.MedicalHistoryDao;
import com.java.jsf.provider.model.MedicalProcedure;
import com.java.jsf.provider.model.Prescription;
import com.java.jsf.util.SessionHelper;

public class MedicalHistoryDaoImpl implements MedicalHistoryDao {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = SessionHelper.getSessionFactory();
    }

    private static final String BASE_HQL =
        "SELECT DISTINCT p FROM MedicalProcedure p " +
        "LEFT JOIN FETCH p.prescription pr " +
        "LEFT JOIN FETCH pr.prescribedMedicines " +
        "LEFT JOIN FETCH pr.tests " +
        "LEFT JOIN FETCH p.logs ";

    @Override
    public List<MedicalProcedure> searchByHid(String doctorId, String hid) {
        Session session = sessionFactory.openSession();
        String hql = BASE_HQL + 
            "WHERE p.recipient.hId = :hid AND p.doctor.doctorId = :doctorId";
        Query query = session.createQuery(hql);
        query.setParameter("hid", hid);
        query.setParameter("doctorId", doctorId);
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<MedicalProcedure> searchByName(String doctorId, String namePart) {
        Session session = sessionFactory.openSession();
        String hql = BASE_HQL + 
            "WHERE p.doctor.doctorId = :doctorId AND " +
            "(LOWER(p.recipient.firstName) LIKE :name OR LOWER(p.recipient.lastName) LIKE :name)";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        query.setParameter("name", namePart.toLowerCase() + "%");
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<MedicalProcedure> searchByMobile(String doctorId, String mobile) {
        Session session = sessionFactory.openSession();
        String hql = BASE_HQL + 
            "WHERE p.doctor.doctorId = :doctorId AND p.recipient.mobile = :mobile";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        query.setParameter("mobile", mobile);
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<MedicalProcedure> searchByNameStartsWith(String doctorId, String prefix) {
        Session session = sessionFactory.openSession();
        String hql = BASE_HQL + 
            "WHERE p.doctor.doctorId = :doctorId AND " +
            "(LOWER(p.recipient.firstName) LIKE :prefix OR LOWER(p.recipient.lastName) LIKE :prefix)";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        query.setParameter("prefix", prefix.toLowerCase() + "%");
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<MedicalProcedure> searchByNameContains(String doctorId, String substring) {
        Session session = sessionFactory.openSession();
        String hql = BASE_HQL + 
            "WHERE p.doctor.doctorId = :doctorId AND " +
            "(LOWER(p.recipient.firstName) LIKE :substr OR LOWER(p.recipient.lastName) LIKE :substr)";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        query.setParameter("substr", "%" + substring.toLowerCase() + "%");
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<MedicalProcedure> getAllProceduresByDoctor(String doctorId) {
        Session session = sessionFactory.openSession();
        String hql = BASE_HQL + 
            "WHERE p.doctor.doctorId = :doctorId";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public boolean doctorExists(String doctorId) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT 1 FROM Doctors d WHERE d.doctorId = :doctorId";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        boolean exists = query.uniqueResult() != null;
        session.close();
        return exists;
    }
    
    @Override
    public List<Prescription> getPrescriptionsByProcedureId(String procedureId) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT DISTINCT p FROM Prescription p " +
                     "LEFT JOIN FETCH p.prescribedMedicines " +
                     "LEFT JOIN FETCH p.tests " +
                     "WHERE p.procedure.procedureId = :procedureId";
        Query query = session.createQuery(hql);
        query.setParameter("procedureId", procedureId);
        List<Prescription> result = query.list();
        session.close();
        return result;
    }
}
