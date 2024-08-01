package com.eai.satisfactionservice.service;

import com.eai.openfeignservice.notification.EmailRequest;
import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.user.ClientsForSatisfaction;
import com.eai.openfeignservice.user.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
public class SatisfactionService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private NotificationClient notificationClient;
    @Autowired
    private TemplateEngine templateEngine;

    private static final String SUBJECT = "Formulaire de satisfaction";
    private static final String TEMPLATE_URL = "http://localhost:9095/agd/satisfaction-service/api/form";

    public void sendSatisfactionForms() {
        List<ClientsForSatisfaction> clients = userClient.getClientsForSatisf();

        for (ClientsForSatisfaction client : clients) {
            String htmlContent = generateHtmlContent(client);
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(client.getEmail());
            emailRequest.setSubject(SUBJECT);
            emailRequest.setText(htmlContent);

            notificationClient.sendEmail(emailRequest);
        }
    }
    private String generateHtmlContent(ClientsForSatisfaction client) {
        String link = String.format("%s?email=%s&nom=%s&prenom=%s", TEMPLATE_URL, client.getEmail(), client.getNom(), client.getPrenom());

        Context context = new Context();
        context.setVariable("link", link);

        return templateEngine.process("satisfaction_email_template", context);
    }
}
