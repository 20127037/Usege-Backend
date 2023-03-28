package com.group_1.authenticationService.service;

import com.group_1.authenticationService.dto.CreateAccountRequestDto;
import com.group_1.authenticationService.dto.CreateAccountResponseDto;
import com.group_1.sharedAws.model.UserInfo;
import com.group_1.sharedAws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.time.LocalDateTime;

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
    private final UserRepository userRepository;

    public AccountServiceImpl(CognitoIdentityProviderClient cognitoIdentityProviderClient,
                              UserRepository userRepository) {
        this.cognitoIdentityProviderClient = cognitoIdentityProviderClient;
        this.userRepository = userRepository;
    }

    public CreateAccountResponseDto createAccount(CreateAccountRequestDto createAccountRequestDto)
    {
        String email = createAccountRequestDto.email();
        AttributeType userAttrs = AttributeType.builder()
                .name("email")
                .value(email)
                .build();
        LocalDateTime now = LocalDateTime.now();
        SignUpResponse response = cognitoIdentityProviderClient.signUp(builder -> builder
                    .clientId(cognitoClientId)
                    .username(createAccountRequestDto.email())
                    .userAttributes(userAttrs)
                    .password(createAccountRequestDto.password()));
        userRepository.saveRecord(UserInfo
                .builder()
                .userId(response.userSub())
                .email(email)
                .storedFileCount(0L)
                .usedSpace(0L)
                .maxSpace(0L)
                .storagePackId("default")
                .purchasedPackDate(now.toString())
                .build());
        return new CreateAccountResponseDto(response.userSub(), email);
    }
}
