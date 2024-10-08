package com.eai.securityservice.controller;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.securityservice.dto.OtpEmailCompareResponse;
import com.eai.securityservice.service.OtpEmailService;
import com.eai.securityservice.dto.OtpEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp_email")
@RequiredArgsConstructor
public class OtpEmailController {
    private final OtpEmailService otpEmailService;

    @PostMapping("/generate")
    public String generateEmailOtp(@RequestBody OtpEmailRequest otpEmailRequest ) {
        return otpEmailService.generateOtpEmail(otpEmailRequest);
    }


    @PostMapping("/compare")
    public OtpEmailCompareResponse compareOtp(@RequestBody ClientRequest otpEmailRequest) {
        return otpEmailService.CompareOtpEmailResponse(otpEmailRequest);
    }
}

