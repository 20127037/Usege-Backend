package com.group_1.auth.service;

import com.group_1.auth.dto.ResponseTokenDto;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;

/**
 * com.group_1.authService.service
 * Created by NhatLinh - 19127652
 * Date 4/4/2023 - 2:10 PM
 * Description: ...
 */
public interface AuthService {
    ResponseTokenDto login(String username, String password);
    ResponseTokenDto refresh(String token);
}

