package com.group_1.account.service;

import com.group_1.account.dto.AccountRequestDto;
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
    @Value("${amazon.aws.cognito.pool-id}")
    private String cognitoPoolId;

    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;
    private final UserRepository userRepository;


    public AccountServiceImpl(CognitoIdentityProviderClient cognitoIdentityProviderClient,
                              UserRepository userRepository) {
        this.cognitoIdentityProviderClient = cognitoIdentityProviderClient;
        this.userRepository = userRepository;
    }

    public UserInfo createAccount(AccountRequestDto createAccountRequestDto)
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
        return UserInfo.builder()
                .userId(response.userSub())
                .email(email).build();
    }

    @Override
    public boolean confirmAccount(String username, String confirmCode) {
        ConfirmSignUpResponse response = cognitoIdentityProviderClient.confirmSignUp(b -> b
                    .clientId(cognitoClientId)
                    .username(username)
                    .confirmationCode(confirmCode));
        return response != null;
    }

    @Override
    public void sendConfirmCode(String username) {
        cognitoIdentityProviderClient.resendConfirmationCode(b -> b
                .clientId(cognitoClientId)
                .username(username));
    }


    @Override
    public void forgetPassword(String username) {
        ForgotPasswordResponse response = cognitoIdentityProviderClient.forgotPassword(b -> b
                .clientId((cognitoClientId))
                .username(username));
    }

    @Override
    public boolean confirmForgetPassword(String username, String confirmCode, String newPassword) {
        ConfirmForgotPasswordResponse response = cognitoIdentityProviderClient.confirmForgotPassword(b -> b
                .clientId((cognitoClientId))
                .username(username)
                .confirmationCode(confirmCode)
                .password(newPassword));
        return true;
    }

    @Override
    public void removeAccount(String username) {
        AdminGetUserResponse user = cognitoIdentityProviderClient.adminGetUser(b -> b.userPoolId(cognitoPoolId).username(username));
        if (user == null)
            return;
        cognitoIdentityProviderClient.adminDisableUser(b -> b.userPoolId(cognitoPoolId).username(user.username()));
        cognitoIdentityProviderClient.adminDeleteUser(b -> b.userPoolId(cognitoPoolId).username(user.username()));
        userRepository.deleteRecordById(user.username());
    }

}
