package com.se2030.BoatSafariManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendAssignmentNotification(String toEmail, String staffName,
                                           String role, int bookingId, String tripDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("New Booking Assignment - Booking #" + bookingId);

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "You have been assigned to a new booking as %s.\n\n" +
                            "Booking Details:\n" +
                            "Booking ID: %d\n" +
                            "Trip Information: %s\n\n" +
                            "Please log in to the system for more details.\n\n" +
                            "Best regards,\n" +
                            "Boat Safari Management System",
                    staffName, role, bookingId, tripDetails
            );

            message.setText(emailBody);
            mailSender.send(message);

            logger.info("Assignment notification sent to {} for booking {}", toEmail, bookingId);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", toEmail, e.getMessage());
            // Don't throw exception - email failure shouldn't break the assignment
        }
    }
}