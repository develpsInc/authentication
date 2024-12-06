//package LIVTech.authentication.authentication.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.MailSendException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.util.logging.Logger;
//
//@Service
//@RequiredArgsConstructor
//public class EmailNotification {
//
//    private static final Logger LOGGER = Logger.getLogger(EmailNotification.class.getName());
//
//
//    private JavaMailSender javaMailSender;
//
//    @Value("${spring.mail.username}")
//    private String fromEmailId;
//
//    public void sendNotification(String to, String subject, String text) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(fromEmailId);
//            message.setTo(to);
//            message.setSubject(subject);
//            message.setText(text);
//
//            javaMailSender.send(message);
//            LOGGER.info("Email sent successfully to " + to);
//        } catch (MailSendException e) {
//            LOGGER.severe("Failed to send email to " + to + ": " + e.getMessage());
//        } catch (Exception e) {
//            LOGGER.severe("An error occurred while sending email to " + to + ": " + e.getMessage());
//        }
//    }
//}
