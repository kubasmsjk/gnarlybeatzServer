package pl.gnarlybeatz.gnarlybeatzServer.emial;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectRequestInterface;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectValidator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ObjectValidator<ObjectRequestInterface> emailValidator;

    @Value("${spring.mail.username}")
    private String adminEmail;

    public String send(EmailRequest request) {
        emailValidator.validate(request);
        try {
            createContactMeEmailDetails(request);
            return "Email sent successfully.";
        } catch (MessagingException e) {
            return "Email not delivered.";
        }
    }

    @Async
    protected void createContactMeEmailDetails(EmailRequest emailRequest) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(emailRequest.getMsgBody(), true);
        helper.setTo(adminEmail);
        helper.setSubject(emailRequest.getSubject() + " sent from: " + emailRequest.getSender());
        mailSender.send(mimeMessage);
    }

    @Async
    public void createCheckoutEmailDetails(String receiver, List<File> pdfContent) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(receiver);
        helper.setSubject("License purchase from the Gnarlybeatz website.");
        helper.setText("Thank you for buying my beat.");
        pdfContent.forEach(file -> {
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            try {
                helper.addAttachment(file.getName(), fileSystemResource, "application/pdf");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });


        mailSender.send(mimeMessage);
    }
}
