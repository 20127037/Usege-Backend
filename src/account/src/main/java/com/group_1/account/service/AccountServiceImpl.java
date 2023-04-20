package com.group_1.account.service;

import com.group_1.account.dto.AccountRequestDto;
import com.group_1.sharedDynamoDB.model.StoragePlan;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.PaymentHistoryRepository;
import com.group_1.sharedDynamoDB.repository.StoragePlanRepository;
import com.group_1.sharedDynamoDB.repository.UserFileDbRepository;
import com.group_1.sharedDynamoDB.repository.UserRepository;
import com.group_1.utilities.MemoryUtilities;
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

    private final StoragePlanRepository storagePlanRepository;
    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final UserFileDbRepository userFileDbRepository;
    private final UserRepository userRepository;


    public AccountServiceImpl(CognitoIdentityProviderClient cognitoIdentityProviderClient,
                              StoragePlanRepository storagePlanRepository,
                              PaymentHistoryRepository paymentHistoryRepository, UserFileDbRepository userFileDbRepository, UserRepository userRepository) {
        this.cognitoIdentityProviderClient = cognitoIdentityProviderClient;
        this.storagePlanRepository = storagePlanRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.userFileDbRepository = userFileDbRepository;
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
        StoragePlan basicPlan = storagePlanRepository.getBasicPlan();
        UserInfo created = UserInfo
                .builder()
                .userId(response.userSub())
                .email(email)
                .imgCount(0L)
                .albumCount(0L)
                .usedSpace(0L)
                .maxSpace(MemoryUtilities.gbToKb(basicPlan.getMaximumSpaceInGB()))
                .plan(basicPlan.getName())
                .planOrder(basicPlan.getOrder())
                .purchasedPlanDate(now.toString())
                .build();
        userRepository.saveRecord(created);
        return created;
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
        AdminGetUserResponse user = cognitoIdentityProviderClient.adminGetUser(b
                -> b.userPoolId(cognitoPoolId).username(username));
        if (user.userStatus() == UserStatusType.UNCONFIRMED)
            throw UserNotConfirmedException.builder().build();
        cognitoIdentityProviderClient.forgotPassword(b -> b
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
        paymentHistoryRepository.deleteRecordById(user.username());
        userFileDbRepository.deleteRecordById(user.username());
    }
}
