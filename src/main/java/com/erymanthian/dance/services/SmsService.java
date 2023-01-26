package com.erymanthian.dance.services;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.azure.communication.phonenumbers.*;
import com.azure.communication.phonenumbers.models.*;
import com.azure.core.http.rest.*;
import com.azure.core.util.Context;
import com.azure.core.util.polling.LongRunningOperationStatus;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import com.azure.identity.*;

import java.io.*;

@Configuration
public class SmsService {
    @Bean
    public ApplicationRunner runner() {
        return args -> {
            String endpoint = "https://sms-auth.communication.azure.com";

            PhoneNumbersClient phoneNumberClient = new PhoneNumbersClientBuilder()
                    .endpoint(endpoint)
                    .credential(new DefaultAzureCredentialBuilder()
                            .build())
                    .buildClient();
        };
    }
}
