package com.erymanthian.dance.services;

import com.azure.communication.sms.SmsClient;
import com.azure.communication.sms.SmsClientBuilder;
import com.azure.communication.sms.models.SmsSendResult;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsService implements CommunicationService {

    public static final String AZURE_PHONE_NUMBER = "+18332061522";
    private final SmsClient smsClient;

    public SmsService() {
        String endpoint = "endpoint=https://sms-auth.communication.azure.com/;accesskey=3oEyuPHH/5Oo5sfXk48vGrtQqKUkpF51+NSYooH1uu7zr3u5fXKjW6l0n/k2v56nj7bTJ6VuCWcBOt7FGpHS4w==";
        this.smsClient = new SmsClientBuilder().connectionString(endpoint).buildClient();

    }


    @Override
    public void sendMessage(String phoneNumber, String code) {
        SmsSendResult result = smsClient.send(AZURE_PHONE_NUMBER, phoneNumber, "Your Dancer Society Verification Code is: " + code);
    }
}
