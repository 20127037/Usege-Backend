package com.group_1.sharedCognito.config;

import com.group_1.sharedAws.config.AwsClientConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

/**
 * config
 * Created by NhatLinh - 19127652
 * Date 3/25/2023 - 2:14 PM
 * Description: ...
 */
@Configuration
@AllArgsConstructor
public class CognitoIdentityConfig {
    private final AwsClientConfig awsClientConfig;

    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClient(){
        CognitoIdentityProviderClient client = CognitoIdentityProviderClient.builder()
                .region(awsClientConfig.region())
                .credentialsProvider(awsClientConfig.credentialsProvider())
                .build();
        return client;
    }
}

