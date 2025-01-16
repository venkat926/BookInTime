package org.kvn.BookInTime.consumer;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TicketBookingNotification {

    @Autowired
    private SimpleMailMessage mailMessage;

    @Autowired
    private JavaMailSender mailSender;

    public void sendTicketBookedNotification(Ticket ticket) {
        log.info("sendTicketBookedNotification ticket={}", ticket);
        mailMessage.setFrom("noreply@bookintime.com");
        mailMessage.setTo(ticket.getUser().getName());
        mailMessage.setSubject("Ticket Booked Successful");
        mailMessage.setText(String.format("""
            Dear %s,
    
            Thank you for booking your tickets with BookInTime!
    
            Here are your booking details:
    
            Movie: %s
            Show Time: %s
            Theater: %s
    
            Please make sure to arrive at the theater at least 15 minutes before the show starts. 
            If you have any questions or need assistance, feel free to reach out to us at 
            support@bookintime.com.
    
            Enjoy the show!
    
            Best regards,
            The BookInTime Team
            """, ticket.getUser().getName(), ticket.getShow().getMovie().getTitle(),
                ticket.getShow().getShowTiming(), ticket.getShow().getTheater().getName() )
        );

        try {
            mailSender.send(mailMessage);
        } catch (MailParseException e) {
            log.error("Email parsing error : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("failed to send notification to user on booing ticket: {} :", e.getMessage());
            throw e;
        }

        log.info("sentTicketBookedNotification ticket={}", ticket);
    }

    public void sendTicketCancelledNotification(Ticket ticket) {
        log.info("sendTicketCancelledNotification ticket={}", ticket);
        mailMessage.setFrom("noreply@bookintime.com");
        mailMessage.setTo(ticket.getUser().getName());
        mailMessage.setSubject("Ticket Cancelled");
        mailMessage.setText(String.format("""
        Dear %s,

        We have processed your request to cancel your ticket booking successfully.

        Here are the details of your cancelled booking:

        Movie: %s
        Show Time: %s
        Theater: %s

        If you have any questions or need further assistance, please feel free to contact us at 
        support@bookintime.com. 

        Thank you for using BookInTime, and we hope to serve you again in the future.

        Best regards,
        The BookInTime Team
        """, ticket.getUser().getName(), ticket.getShow().getMovie().getTitle(),
                ticket.getShow().getShowTiming(), ticket.getShow().getTheater().getName() ) );

        try {
            mailSender.send(mailMessage);
        } catch (MailParseException e) {
            log.error("Email parsing error : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("failed to send notification to user on cancelling ticket: {} :", e.getMessage());
            throw e;
        }
        log.info("sentTicketCancelledNotification ticket={}", ticket);

    }
}
