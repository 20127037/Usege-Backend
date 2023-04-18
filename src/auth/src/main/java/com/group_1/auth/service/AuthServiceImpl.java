package com.group_1.auth.service;

import com.group_1.auth.dto.ResponseTokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;

import java.util.HashMap;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${amazon.aws.cognito.client-id}")
    private String cognitoClientId;
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;
    public AuthServiceImpl(CognitoIdentityProviderClient cognitoIdentityProviderClient) {
        this.cognitoIdentityProviderClient = cognitoIdentityProviderClient;
    }

    public ResponseTokenDto login(String username, String password)
    {
        HashMap<String, String> authParams = new HashMap<>();
        authParams.put(USERNAME, username);
        authParams.put(PASSWORD, password);
        InitiateAuthResponse response = cognitoIdentityProviderClient.initiateAuth(b -> {
            b.clientId(cognitoClientId)
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .authParameters(authParams);
        });
        return processResponseToken(response.authenticationResult());
    }

    private ResponseTokenDto processResponseToken(AuthenticationResultType authResult)
    {
        GetUserResponse getUserResponse = cognitoIdentityProviderClient.getUser(b -> b.accessToken(authResult.accessToken()));
        return ResponseTokenDto.builder()
                .userId(getUserResponse.username())
                .accessToken(authResult.accessToken())
                .idToken(authResult.idToken())
                .refreshToken(authResult.refreshToken())
                .expiresIn(authResult.expiresIn())
                .build();
    }

    public ResponseTokenDto refresh(String token)
    {
        HashMap<String, String> authParams = new HashMap<>();
        authParams.put(REFRESH_TOKEN, token);
        InitiateAuthResponse response = cognitoIdentityProviderClient.initiateAuth(b -> b.clientId(cognitoClientId)
                .authFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                .authParameters(authParams));
        return processResponseToken(response.authenticationResult());
    }
}
