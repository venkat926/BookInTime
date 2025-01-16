package org.kvn.BookInTime.consumer;

import lombok.extern.slf4j.Slf4j;
import org.kvn.BookInTime.dto.response.UserCreationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCreationNotification {

    @Autowired
    private SimpleMailMessage mailMessage;

    @Autowired
    private JavaMailSender mailSender;

    public void sendNotification(UserCreationResponseDTO responseDTO) {
        log.info("send notification to user on successful registration {}", responseDTO.getName());

        mailMessage.setFrom("noreply@bookintime.com");
        mailMessage.setTo(responseDTO.getEmail());
        mailMessage.setSubject("User Registration Successful");
        mailMessage.setText(String.format("""
            Dear %s,
    
            Congratulations on successfully registering to the BookInTime platform!
    
            We're excited to have you onboard. You can now explore various movies, book tickets, 
            and enjoy a seamless entertainment experience.
    
            If you have any questions or need assistance, feel free to reach out to us at 
            support@bookintime.com.
    
            Thank you for choosing BookInTime.
    
            Best regards,
            The BookInTime Team
            """, responseDTO.getName())
        );

        try {
            mailSender.send(mailMessage);
        } catch (MailParseException e) {
            log.error("Email parsing error : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("failed to send notification to user on successful registration: {} : {}", responseDTO.getName(), e.getMessage());
            throw e;
        }

        log.info("sent notification to user on successful registration {}", responseDTO.getName());
    }
}
