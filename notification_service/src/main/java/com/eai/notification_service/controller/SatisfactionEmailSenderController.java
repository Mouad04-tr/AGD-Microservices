package com.eai.notification_service.controller;

import com.eai.notification_service.service.SatisfactEmailSenderService;
import com.eai.openfeignservice.notification.EmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SatisfactionEmailSenderController {

    private final SatisfactEmailSenderService satisfactEmailSenderService;

    @PostMapping("/send-form-satisfaction")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        return satisfactEmailSenderService.sendEmail(emailRequest);
    }
}
