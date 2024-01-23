package pl.gnarlybeatz.gnarlybeatzServer.emial;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public String send(EmailDetails emailDetails) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailDetails.getMsgBody(),true);
            helper.setTo("gnarlybeats444@gmail.com");
            helper.setSubject(emailDetails.getSubject() + " sent from: " + emailDetails.getSender());
            mailSender.send(mimeMessage);
            return "Mail Sent Successfully...";
        } catch (MessagingException e){
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }


//    String linkToPost = "https://pb.edu.pl/";
//        for (ForumUser viewer : mainPost.getViewers()) {
//        emailSender.send(viewer.getEmail(),  "News in: " + mainPost.getTitle(),
//                EmailTemplate.buildEmail(viewer.getUsername(),linkToPost));
//    }
}
