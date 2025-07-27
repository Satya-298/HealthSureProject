package com.infinite.jsf.provider.main;

import com.infinite.jsf.provider.daoImpl.MedicalHistoryDaoImpl;
import com.infinite.jsf.provider.model.MedicalProcedure;
import com.infinite.jsf.provider.model.ProcedureDailyLog;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        MedicalHistoryDaoImpl dao = new MedicalHistoryDaoImpl();

        // Replace with an actual procedure ID from your DB that has logs
        String procedureId = "PROC018";

        // Fetch procedure with logs using DAO
        MedicalProcedure procedure = dao.getProcedureWithLogs(procedureId);

        if (procedure == null) {
            System.out.println("❌ No procedure found with ID: " + procedureId);
            return;
        }

        // Print procedure details
        System.out.println("✅ Procedure ID: " + procedure.getProcedureId());
        System.out.println("Diagnosis: " + procedure.getDiagnosis());
        System.out.println("Recommendations: " + procedure.getRecommendations());

        // Fetch logs (check your getter name: getProcedureLogs() or getLogs())
        Set<ProcedureDailyLog> logs = procedure.getLogs(); // <-- Make sure this method exists

        if (logs == null || logs.isEmpty()) {
            System.out.println("ℹ️ No logs found for this procedure.");
        } else {
            System.out.println("📋 Logs found: " + logs.size());
            for (ProcedureDailyLog log : logs) {
                System.out.println("---------------------------------");
                System.out.println("🗓️ Log Date: " + log.getLogDate());
                System.out.println("❤️ Vitals: " + log.getVitals());
                System.out.println("📝 Notes: " + log.getNotes());
            }
        }
    }
}
