package com.eai.securityservice.service;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.HOTPGenerator;
import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.notification.SmsSender;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.UserClient;
import com.eai.securityservice.dto.OtpPhoneRequest;
import com.eai.securityservice.model.Counter;
import com.eai.securityservice.model.History;
import com.eai.securityservice.model.Otp;
import com.eai.securityservice.outiles.enums.OtpGenerationStatusEnum;
import com.eai.securityservice.outiles.enums.StatusOTP;
import com.eai.securityservice.repository.CounterRepository;
import com.eai.securityservice.repository.HistoryRepository;
import com.eai.securityservice.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor
public class OtpPhoneService {

    private final OtpRepository otpRepository;
    private final HistoryRepository historyRepository;
    private final Counter counter;
    private final CounterRepository counterRepository;
    private final NotificationClient notificationClient;
    private final UserClient userClient;
    private static final byte[] SECRET_KEY_BYTES = "VV3KOX7UQJ4KYAKOHMZPPH3US4CJIMH6F3ZKNB5C2OOBQ6V2KIYHM27Q".getBytes();

    public String generateOtpPhone(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        String generatedOtp = generateOtp(counter.getCounter());
        History history = historyRepository.findTopByKeyPhoneAndNumPhoneOrderByDateGenerationDesc(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone());
        Otp otp = otpRepository.findByIdClient(otpPhoneRequest.getIdClient());
        SmsSender smsSender = SmsSender.builder()
                .keyPhone(otpPhoneRequest.getKeyPhone())
                .numPhone(otpPhoneRequest.getNumPhone())
                .codeOtpSms(generatedOtp)
                .build();
        System.out.println(generatedOtp);
        String isSent;
        if (history == null) {
            history = new History(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone(), counter.getCounter(), new Date());
            otp.setCounter(counter.getCounter());
            otp.setDateGeneration(new Date());
            otp.setAttempts(0);
            otp.setKeyPhone(otpPhoneRequest.getKeyPhone());
            otp.setNumPhone(otpPhoneRequest.getNumPhone());
            if(Objects.equals(notificationClient.sendOtpSms(smsSender), "01")){
                isSent =  OtpGenerationStatusEnum.SUCCESS.getLabel();
            }else{
                isSent =  OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
            }

        } else{
            if (history.getNumGeneration() < 5) {
                history.setCounter(counter.getCounter());
                history.setDateGeneration(new Date());
                history.incrementNumGeneration();
                otp.setCounter(counter.getCounter());
                otp.setKeyPhone(otpPhoneRequest.getKeyPhone());
                otp.setNumPhone(otpPhoneRequest.getNumPhone());
                otp.setDateGeneration(new Date());
                otp.setAttempts(0);
                if(Objects.equals(notificationClient.sendOtpSms(smsSender), "01")){
                    isSent =  OtpGenerationStatusEnum.SUCCESS.getLabel();
                }else{
                    isSent =  OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
                }



            } else if (history.getNumGeneration() == 5 && isPast30Minutes(history.getDateGeneration()) > 30) {
                history = new History(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone(), counter.getCounter(), new Date());
                otp.setCounter(counter.getCounter());
                otp.setDateGeneration(new Date());
                otp.setAttempts(0);
                otp.setKeyPhone(otpPhoneRequest.getKeyPhone());
                otp.setNumPhone(otpPhoneRequest.getNumPhone());
                if(Objects.equals(notificationClient.sendOtpSms(smsSender), "01")){
                    isSent =  OtpGenerationStatusEnum.SUCCESS.getLabel();
                }else{
                    isSent =  OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
                }
            }
            else {
                isSent = OtpGenerationStatusEnum.MAX_GENERATED_OTP_ERROR.getLabel();
            }
        }
        otpRepository.save(otp);
        historyRepository.save(history);
        counter.incrementCounter();
        counterRepository.save(counter);
        return isSent;
    }
    public String compareOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {

        Otp otp = otpRepository.findByKeyPhoneAndNumPhone(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone());
        if (isPast30Minutes(otp.getDateGeneration())<30) {
            if (otp.getAttempts()<3){
                Boolean isOtpValid = verifyOtp(otpPhoneRequest.getUserInput(), otp.getCounter());
                otp.incrementAttempt();
                otpRepository.save(otp);
                if (isOtpValid) {
                    ClientRequest clientRequest = ClientRequest.builder()
                            .idClient(otpPhoneRequest.getIdClient())
                            .indicatifTel(otpPhoneRequest.getKeyPhone())
                            .numTel(otpPhoneRequest.getNumPhone()).build();
                    String idClient = userClient.addPhone(clientRequest);
                    return StatusOTP.VALID.getLabel();
                } else {
                    return StatusOTP.INVALID.getLabel();
                }
            } else {
                return StatusOTP.EXPIRED_ATTEMPT.getLabel();
            }
        } else {
            return StatusOTP.TIMEOUT.getLabel();
        }

    }

    public boolean compareOtp( String userInput , Integer counter) {
        HOTPGenerator hotp = new HOTPGenerator.Builder(SECRET_KEY_BYTES)
                .withPasswordLength(8)
                .withAlgorithm(HMACAlgorithm.SHA256)
                .build();
        return hotp.verify(userInput, counter);
    }




    public Boolean verifyOtp(String input, Integer counter){
        return compareOtp(input, counter);
    }

    private long isPast30Minutes(Date date) {
        long diffInMilliseconds = new Date().getTime() - date.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds);
    }


    public String generateOtp(Integer counter) {

        HOTPGenerator hotp = new HOTPGenerator.Builder(SECRET_KEY_BYTES)
                .withPasswordLength(8)
                .withAlgorithm(HMACAlgorithm.SHA256)
                .build();
        String code = hotp.generate(counter);

        return code;

    }
}