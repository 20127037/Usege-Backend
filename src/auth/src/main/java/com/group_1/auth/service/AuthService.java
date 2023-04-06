package com.group_1.auth.service;

import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;

/**
 * com.group_1.authService.service
 * Created by NhatLinh - 19127652
 * Date 4/4/2023 - 2:10 PM
 * Description: ...
 */
public interface AuthService {
    AuthenticationResultType login(String username, String password);
    AuthenticationResultType refresh(String token);
}

