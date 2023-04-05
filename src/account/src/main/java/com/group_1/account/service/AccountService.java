package com.group_1.account.service;

import com.group_1.account.dto.AccountRequestDto;
import com.group_1.sharedAws.model.UserInfo;

/**
 * service
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 2:49 PM
 * Description: ...
 */

public interface AccountService {
    UserInfo createAccount(AccountRequestDto createAccountRequestDto);
    boolean confirmAccount(String username, String confirmCode);
    void sendConfirmCode(String username);
    void forgetPassword(String username);
    boolean confirmForgetPassword(String username, String confirmCode, String newPassword);
    void removeAccount(String username);
}