package com.group_1.authenticationService.service;

import com.group_1.authenticationService.dto.CreateAccountRequestDto;
import com.group_1.authenticationService.dto.CreateAccountResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

/**
 * service
 * Created by NhatLinh - 19127652
 * Date 3/25/2023 - 2:28 PM
 * Description: ...
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Value("${amazon.aws.cognito.client-id}")
    private String cognitoClientId;

    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;

    public AccountServiceImpl(CognitoIdentityProviderClient cognitoIdentityProviderClient) {
        this.cognitoIdentityProviderClient = cognitoIdentityProviderClient;
    }

    public CreateAccountResponseDto createAccount(CreateAccountRequestDto createAccountRequestDto)
    {
        AttributeType userAttrs = AttributeType.builder()
                .name("email")
                .value(createAccountRequestDto.email())
                .build();
        SignUpResponse response = cognitoIdentityProviderClient.signUp(builder -> builder
                    .clientId(cognitoClientId)
                    .username(createAccountRequestDto.email())
                    .userAttributes(userAttrs)
                    .password(createAccountRequestDto.password()));
        return new CreateAccountResponseDto(response.userSub(), createAccountRequestDto.email());
    }
}
