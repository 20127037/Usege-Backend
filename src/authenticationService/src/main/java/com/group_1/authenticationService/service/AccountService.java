package com.group_1.authenticationService.service;

import com.group_1.authenticationService.dto.CreateAccountRequestDto;
import com.group_1.authenticationService.dto.CreateAccountResponseDto;

/**
 * service
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 2:49 PM
 * Description: ...
 */

public interface AccountService {
    CreateAccountResponseDto createAccount(CreateAccountRequestDto createAccountRequestDto);
}