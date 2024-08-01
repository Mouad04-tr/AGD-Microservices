package com.eai.notification_service.service;

import com.eai.notification_service.outils.enums.EmailStatus;
import com.eai.openfeignservice.notification.EmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class SatisfactEmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public String sendEmail(EmailRequest emailRequest) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(emailRequest.getText(), true);
            mailSender.send(message);

            return EmailStatus.SUCCESSFUL.getLabel();
        } catch (Exception e) {
            e.printStackTrace();
            return EmailStatus.ERROR.getLabel();
        }
    }
}
