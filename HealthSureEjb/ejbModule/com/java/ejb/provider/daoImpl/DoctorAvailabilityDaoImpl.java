package com.java.ejb.provider.daoImpl;

import com.java.ejb.util.ConnectionHelper;
import com.java.ejb.provider.dao.DoctorAvailabilityDao;
import com.java.ejb.provider.model.DoctorAvailability;
import com.java.ejb.provider.model.SlotType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoctorAvailabilityDaoImpl implements DoctorAvailabilityDao {

    Connection connection;
    PreparedStatement pst;

    @Override
    public String addAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException {
        connection = ConnectionHelper.getConnection();
        String availabilityId = generateAvailabilityId();
        availability.setAvailabilityId(availabilityId);

        String cmd = "INSERT INTO Doctor_availability "
                + "(availability_id, doctor_id, available_date, start_time, end_time, slot_type, is_recurring, notes, total_slots) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        pst = connection.prepareStatement(cmd);
        pst.setString(1, availabilityId);
        pst.setString(2, availability.getDoctorId());
        pst.setDate(3, new java.sql.Date(availability.getAvailableDate().getTime()));
        pst.setTime(4, new java.sql.Time(availability.getStartTime().getTime()));
        pst.setTime(5, new java.sql.Time(availability.getEndTime().getTime()));
        pst.setString(6, availability.getSlotType().toString());
        pst.setBoolean(7, availability.isRecurring());
        pst.setString(8, availability.getNotes());
        pst.setInt(9, availability.getTotalSlots());
        pst.executeUpdate();

        return "Doctor Availability added...";
    }

    @Override
    public List<DoctorAvailability> getAvailabilityByDoctor(String doctorId) throws ClassNotFoundException, SQLException {
        connection = ConnectionHelper.getConnection();
        String cmd = "SELECT * FROM Doctor_availability WHERE doctor_id = ?";
        pst = connection.prepareStatement(cmd);
        pst.setString(1, doctorId);
        ResultSet rs = pst.executeQuery();
        List<DoctorAvailability> list = new ArrayList<>();
        while (rs.next()) {
            DoctorAvailability da = new DoctorAvailability();
            da.setAvailabilityId(rs.getString("availability_id"));
            da.setDoctorId(rs.getString("doctor_id"));
            da.setAvailableDate(rs.getDate("available_date"));
            da.setStartTime(rs.getTime("start_time"));
            da.setEndTime(rs.getTime("end_time"));
            da.setSlotType(SlotType.valueOf(rs.getString("slot_type")));
            da.setRecurring(rs.getBoolean("is_recurring"));
            da.setNotes(rs.getString("notes"));
            da.setTotalSlots(rs.getInt("total_slots"));
            da.setCreatedAt(rs.getTimestamp("created_at"));
            da.setUpdatedAt(rs.getTimestamp("updated_at"));
            list.add(da);
        }
        return list;
    }

    @Override
    public List<DoctorAvailability> getAvailabilityByDate(Date selectedDate) throws ClassNotFoundException, SQLException {
        connection = ConnectionHelper.getConnection();
        String cmd = "SELECT * FROM Doctor_availability WHERE available_date = ?";
        pst = connection.prepareStatement(cmd);
        pst.setDate(1, new java.sql.Date(selectedDate.getTime()));
        ResultSet rs = pst.executeQuery();
        List<DoctorAvailability> list = new ArrayList<>();
        while (rs.next()) {
            DoctorAvailability da = new DoctorAvailability();
            da.setAvailabilityId(rs.getString("availability_id"));
            da.setDoctorId(rs.getString("doctor_id"));
            da.setAvailableDate(rs.getDate("available_date"));
            da.setStartTime(rs.getTime("start_time"));
            da.setEndTime(rs.getTime("end_time"));
            da.setSlotType(SlotType.valueOf(rs.getString("slot_type")));
            da.setRecurring(rs.getBoolean("is_recurring"));
            da.setNotes(rs.getString("notes"));
            da.setTotalSlots(rs.getInt("total_slots"));
            da.setCreatedAt(rs.getTimestamp("created_at"));
            da.setUpdatedAt(rs.getTimestamp("updated_at"));
            list.add(da);
        }
        return list;
    }

    @Override
    public String updateAvailability(DoctorAvailability availability) throws ClassNotFoundException, SQLException {
        connection = ConnectionHelper.getConnection();
        String cmd = "UPDATE Doctor_availability SET available_date = ?, start_time = ?, end_time = ?, slot_type = ?, "
                + "is_recurring = ?, notes = ?, total_slots = ?, updated_at = ? WHERE availability_id = ?";
        pst = connection.prepareStatement(cmd);
        pst.setDate(1, new java.sql.Date(availability.getAvailableDate().getTime()));
        pst.setTime(2, new java.sql.Time(availability.getStartTime().getTime()));
        pst.setTime(3, new java.sql.Time(availability.getEndTime().getTime()));
        pst.setString(4, availability.getSlotType().toString());
        pst.setBoolean(5, availability.isRecurring());
        pst.setString(6, availability.getNotes());
        pst.setInt(7, availability.getTotalSlots());
        pst.setTimestamp(8, new Timestamp(availability.getUpdatedAt().getTime()));
        pst.setString(9, availability.getAvailabilityId());
        pst.executeUpdate();
        return "Doctor Availability updated...";
    }

    public String generateAvailabilityId() throws ClassNotFoundException, SQLException {
        connection = ConnectionHelper.getConnection();
        String cmd = "SELECT MAX(availability_id) FROM Doctor_availability";
        pst = connection.prepareStatement(cmd);
        ResultSet rs = pst.executeQuery();
        if (rs.next() && rs.getString(1) != null) {
            String latest = rs.getString(1);
            int num = Integer.parseInt(latest.substring(1));
            return "A" + String.format("%03d", num + 1);
        }
        return "A001";
    }
    
    @Override
    public boolean isDoctorUnderProvider(String doctorId) throws ClassNotFoundException, SQLException {
        Connection con = ConnectionHelper.getConnection();
        PreparedStatement pst = con.prepareStatement(
            "SELECT provider_id FROM Doctors WHERE doctor_id = ?");
        
        pst.setString(1, doctorId);
        ResultSet rs = pst.executeQuery();

        boolean exists = false;
        if (rs.next()) {
            String providerId = rs.getString("provider_id");
            exists = (providerId != null && !providerId.trim().isEmpty());
        }

        rs.close();
        pst.close();
        con.close();

        return exists;
    }

}
