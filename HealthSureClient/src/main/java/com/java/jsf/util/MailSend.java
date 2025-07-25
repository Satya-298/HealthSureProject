package com.java.jsf.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.java.jsf.provider.dto.AppointmentSlip;

public class MailSend {
    public static String sendInfo(String toEmail, String subject, String data) {
        // Existing implementation remains unchanged
        String from = "prasanna.trainer@gmail.com";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("prasanna.vsp80@gmail.com", "soqdhechjkcchkgl");
            }
        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(data);

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");
            return "Mail Send Successfully...";
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return mex.getMessage();
        }
    }

    public static String sendMail(String toEmail, String subject, String htmlContent) {
        // Existing implementation remains unchanged
        String from = "infinitehealthsure@gmail.com";
        String host = "smtp.gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "xrascqydsfthxttk");
            }
        });

        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html");
            Transport.send(message);
            return "Mail Sent Successfully...";
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return "Error: " + mex.getMessage();
        }
    }

    // Existing appointment request template remains unchanged
    public static String appointmentRequest(AppointmentSlip apSli) {
        String htmlContent = "<html><body style='font-family:Arial, sans-serif;'>"
                + "<h2 style='color:#2E86C1;'>Your appointment request has been received </h2>" + "<p>Dear "
                + apSli.getPatientName() + ",</p>" + "<p>Thank you for booking your appointment with <strong>"
                + apSli.getProviderName() + "</strong>.</p>"
                + "<table style='border-collapse: collapse; width: 100%; margin-top: 10px;'>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Appointment ID</td><td style='padding: 8px; border: 1px solid #ddd;'>"
                + apSli.getAppointmentId() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Doctor</td><td style='padding: 8px; border: 1px solid #ddd;'>"
                + apSli.getDoctorName() + " (" + apSli.getDoctorSpecialization() + ")</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Date</td><td style='padding: 8px; border: 1px solid #ddd;'>"
                + apSli.getDate() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Time</td><td style='padding: 8px; border: 1px solid #ddd;'>Slot "
                + apSli.getSlotNo() + " - " + apSli.getTiming() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Provider Contact</td><td style='padding: 8px; border: 1px solid #ddd;'>"
                + apSli.getProviderEmail() + " | " + apSli.getProviderNumber() + "</td></tr>" + "</table>"
                + "<p style='margin-top: 20px;'>You will receive a confirmation once the provider reviews and approves your request.\r\n"
                + "\r\n" + "Thank you for choosing Infinite HealthSure.</p>"
                + "<p style='margin-top: 20px;'>Please arrive 15 minutes early and bring any necessary documents.</p>"
                + "<hr></body></html>";
        return htmlContent;
    }

    // Existing cancellation template remains unchanged
    public static String appointmentCancellation(AppointmentSlip apSli) {
        String htmlContent = "<html><body style='font-family:Arial, sans-serif;'>"
                + "<h2 style='color:#C0392B;'>Your appointment has been cancelled</h2>" + "<p>Dear "
                + apSli.getPatientName() + ",</p>" + "<p>Your appointment with <strong>" + apSli.getDoctorName()
                + "</strong> at <strong>" + apSli.getProviderName() + "</strong> has been successfully cancelled.</p>"
                + "<table style='border-collapse: collapse; width: 100%; margin-top: 10px;'>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Appointment ID</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getAppointmentId() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Date</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getDate() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Time</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>Slot " + apSli.getSlotNo() + " - "
                + apSli.getTiming() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Provider Contact</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getProviderEmail() + " | "
                + apSli.getProviderNumber() + "</td></tr>" + "</table>"
                + "<p style='margin-top: 20px;'>If this was a mistake or you want to reschedule, please contact us or book a new appointment.</p>"
                + "<p>Thank you,<br>Infinite HealthSure Team</p>" + "<hr></body></html>";
        return htmlContent;
    }

    // NEW METHOD: For booking confirmation email
    public static String appointmentConfirmation(AppointmentSlip apSli) {
        String htmlContent = "<html><body style='font-family:Arial, sans-serif;'>"
                + "<h2 style='color:#27AE60;'>Your appointment has been confirmed</h2>"
                + "<p>Dear " + apSli.getPatientName() + ",</p>"
                + "<p>Your appointment with <strong>" + apSli.getDoctorName() + "</strong> at <strong>" 
                + apSli.getProviderName() + "</strong> has been confirmed.</p>"
                + "<table style='border-collapse: collapse; width: 100%; margin-top: 10px;'>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Appointment ID</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getAppointmentId() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Doctor</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getDoctorName() 
                + " (" + apSli.getDoctorSpecialization() + ")</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Date</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getDate() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Time</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>Slot " + apSli.getSlotNo() + " - " 
                + apSli.getTiming() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Provider Contact</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getProviderEmail() + " | " 
                + apSli.getProviderNumber() + "</td></tr>"
                + "</table>"
                + "<p style='margin-top: 20px;'>Your appointment is now confirmed. Please arrive 15 minutes early "
                + "and bring any necessary documents or insurance information.</p>"
                + "<p>If you need to reschedule or cancel, please contact us at least 24 hours in advance.</p>"
                + "<p>Thank you,<br>Infinite HealthSure Team</p>"
                + "<hr></body></html>";
        return htmlContent;
    }

    // NEW METHOD: For doctor-initiated cancellation email
    public static String doctorCancellation(AppointmentSlip apSli) {
        String htmlContent = "<html><body style='font-family:Arial, sans-serif;'>"
                + "<h2 style='color:#C0392B;'>Important: Your appointment has been cancelled</h2>"
                + "<p>Dear " + apSli.getPatientName() + ",</p>"
                + "<p>We regret to inform you that your appointment with <strong>" + apSli.getDoctorName() 
                + "</strong> at <strong>" + apSli.getProviderName() + "</strong> has been cancelled.</p>"
                + "<table style='border-collapse: collapse; width: 100%; margin-top: 10px;'>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Appointment ID</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getAppointmentId() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Doctor</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getDoctorName() 
                + " (" + apSli.getDoctorSpecialization() + ")</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Original Date</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getDate() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Original Time</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>Slot " + apSli.getSlotNo() + " - " 
                + apSli.getTiming() + "</td></tr>"
                + "</table>"
                + "<p style='margin-top: 20px;'>We sincerely apologize for any inconvenience this may cause. "
                + "Please contact our office to reschedule your appointment at your earliest convenience.</p>"
                + "<p>For assistance, please contact us at:<br>"
                + "Email: " + apSli.getProviderEmail() + "<br>"
                + "Phone: " + apSli.getProviderNumber() + "</p>"
                + "<p>Thank you for your understanding,<br>Infinite HealthSure Team</p>"
                + "<hr></body></html>";
        return htmlContent;
    }
    
    // NEW METHOD: For procedure completion email
    public static String procedureCompletion(AppointmentSlip apSli, String procedureName) {
        String htmlContent = "<html><body style='font-family:Arial, sans-serif;'>"
                + "<h2 style='color:#27AE60;'>Your procedure has been completed</h2>"
                + "<p>Dear " + apSli.getPatientName() + ",</p>"
                + "<p>We're pleased to inform you that your <strong>" + procedureName + "</strong> procedure with Dr. "
                + apSli.getDoctorName() + " has been successfully completed.</p>"
                + "<table style='border-collapse: collapse; width: 100%; margin-top: 10px;'>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Procedure</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'><strong>" + procedureName + "</strong></td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Appointment ID</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getAppointmentId() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Doctor</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getDoctorName() 
                + " (" + apSli.getDoctorSpecialization() + ")</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Date</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getDate() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Time</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getTiming() + "</td></tr>"
                + "<tr><td style='padding: 8px; border: 1px solid #ddd;'>Provider Contact</td>"
                + "<td style='padding: 8px; border: 1px solid #ddd;'>" + apSli.getProviderEmail() + " | " 
                + apSli.getProviderNumber() + "</td></tr>"
                + "</table>"
                + "<p style='margin-top: 20px;'>Please follow any post-procedure instructions provided by your doctor.</p>"
                + "<p>If you experience any unusual symptoms or have concerns about your recovery, "
                + "please contact our office immediately.</p>"
                + "<p>We appreciate you choosing our healthcare services and wish you a speedy recovery.</p>"
                + "<p>Sincerely,<br>The Healthcare Team<br>" + apSli.getProviderName() + "</p>"
                + "<hr></body></html>";
        return htmlContent;
    }
}