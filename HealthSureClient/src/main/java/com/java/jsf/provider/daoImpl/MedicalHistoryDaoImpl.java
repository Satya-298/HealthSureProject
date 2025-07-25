package com.java.jsf.provider.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.java.jsf.provider.dao.MedicalHistoryDao;
import com.java.jsf.provider.model.MedicalProcedure;
import com.java.jsf.provider.model.Prescription;
import com.java.jsf.provider.model.ProcedureType;
import com.java.jsf.util.SessionHelper;

public class MedicalHistoryDaoImpl implements MedicalHistoryDao {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = SessionHelper.getSessionFactory();
    }

    @Override
    public List<MedicalProcedure> searchByHid(String doctorId, String hid, String type) {
        Session session = sessionFactory.openSession();
        String hql = "FROM MedicalProcedure p WHERE p.recipient.hId = :hid AND p.doctor.doctorId = :doctorId AND p.type = :type";
        Query query = session.createQuery(hql);
        query.setParameter("hid", hid);
        query.setParameter("doctorId", doctorId);
        ProcedureType procedureType = ProcedureType.valueOf(type.trim().toUpperCase());
        query.setParameter("type", procedureType);
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<MedicalProcedure> searchByNameStartsWith(String doctorId, String prefix, String type) {
        Session session = sessionFactory.openSession();
        String hql = "FROM MedicalProcedure p WHERE p.doctor.doctorId = :doctorId AND p.type = :type AND " +
                "(LOWER(p.recipient.firstName) LIKE :prefix OR LOWER(p.recipient.lastName) LIKE :prefix)";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        ProcedureType procedureType = ProcedureType.valueOf(type.trim().toUpperCase());

        query.setParameter("type", procedureType);
        query.setParameter("prefix", prefix.toLowerCase() + "%");
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<MedicalProcedure> searchByNameContains(String doctorId, String substring, String type) {
        Session session = sessionFactory.openSession();
        String hql = "FROM MedicalProcedure p WHERE p.doctor.doctorId = :doctorId AND p.type = :type AND " +
                "(LOWER(p.recipient.firstName) LIKE :substr OR LOWER(p.recipient.lastName) LIKE :substr)";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        ProcedureType procedureType = ProcedureType.valueOf(type.trim().toUpperCase());
        query.setParameter("type", procedureType);
        query.setParameter("substr", "%" + substring.toLowerCase() + "%");
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<MedicalProcedure> searchByMobile(String doctorId, String mobile, String type) {
        Session session = sessionFactory.openSession();
        String hql = "FROM MedicalProcedure p WHERE p.doctor.doctorId = :doctorId AND p.type = :type AND p.recipient.mobile = :mobile";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        ProcedureType procedureType = ProcedureType.valueOf(type.trim().toUpperCase());
        query.setParameter("type", procedureType);
        query.setParameter("mobile", mobile);
        List<MedicalProcedure> result = query.list();
        session.close();
        return result;
    }

    @Override
    public List<MedicalProcedure> searchByDoctorIdWithDetails(String doctorId, String type) {
        Session session = sessionFactory.openSession();
        String hql = "FROM MedicalProcedure p WHERE p.doctor.doctorId = :doctorId AND p.type = :type";
        Query query = session.createQuery(hql);
        query.setParameter("doctorId", doctorId);
        ProcedureType procedureType = ProcedureType.valueOf(type.trim().toUpperCase());
        query.setParameter("type", procedureType);
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
    public Prescription getPrescriptionWithDetails(String prescriptionId) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT DISTINCT p FROM Prescription p " +
                     "LEFT JOIN FETCH p.prescribedMedicines " +
                     "LEFT JOIN FETCH p.tests " +
                     "WHERE p.prescriptionId = :prescId";
        Query query = session.createQuery(hql);
        query.setParameter("prescId", prescriptionId);
        Prescription result = (Prescription) query.uniqueResult();
        session.close();
        return result;
    }
    
    @Override
    public MedicalProcedure getProcedureWithLogs(String procedureId) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT DISTINCT m FROM MedicalProcedure m " +
                     "LEFT JOIN FETCH m.logs " +
                     "WHERE m.procedureId = :procId";
        Query query = session.createQuery(hql);
        query.setParameter("procId", procedureId);
        MedicalProcedure result = (MedicalProcedure) query.uniqueResult();
        session.close();
        return result;
    }

}
