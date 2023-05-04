package com.group_1.account.controller;

import com.group_1.account.dto.AccountRequestDto;
import com.group_1.account.dto.ConfirmForgetPasswordDto;
import com.group_1.account.service.AccountService;
import com.group_1.sharedDynamoDB.model.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

/**
 * controller
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 3:31 PM
 * Description: ...
 */
@RestController("account")
@AllArgsConstructor
@Tag(name = "Account", description = "Create/Confirm account")
@Slf4j
public class UserAccountController {
    private final AccountService accountService;

    @Operation(summary = "Create account")
    @PostMapping
    public ResponseEntity<UserInfo> createAccount(HttpServletRequest request, @RequestBody AccountRequestDto requestDto)
    {
        UserInfo response = accountService.createAccount(requestDto);
        log.info("{} ({}) has been created", response.getEmail(), response.getUserId());
        return ResponseEntity.created(URI.create(request.getRequestURI() + "/" + response.getUserId())).body(response);
    }

    @Operation(summary = "Confirm account")
    @PutMapping("{id}")
    public ResponseEntity<Object> confirmAccount(@PathVariable String id, @RequestParam(value = "code", defaultValue = "") String confirmCode)
    {
        if (accountService.confirmAccount(id, confirmCode))
        {
            log.info("{} has been confirmed by code {}", id, confirmCode);
            return ResponseEntity.ok().build();
        }
        log.error("{} has failed to confirm by code {}", id, confirmCode);
        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Resend confirm code for an account")
    @PostMapping("confirmCode/{id}")
    public ResponseEntity<UserInfo> sendConfirmCode(HttpServletRequest request, @PathVariable String id)
    {
        accountService.sendConfirmCode(id);
        return ResponseEntity.created(URI.create(request.getRequestURI())).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable String id)
    {
        accountService.removeAccount(id);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Send forget password code to the mailbox")
    @PostMapping("forget/{id}")
    public ResponseEntity<Object> forgetPassword(HttpServletRequest request, @PathVariable String id)
    {
        accountService.forgetPassword(id);
        return ResponseEntity.created(URI.create(request.getRequestURI())).build();
    }

    @Operation(summary = "Confirm code to change the passsword")
    @PutMapping("forget/{id}")
    public ResponseEntity<Object> confirmForgetPassword(@PathVariable String id, @RequestBody ConfirmForgetPasswordDto confirm)
    {
        if (accountService.confirmForgetPassword(id, confirm.confirmCode(), confirm.newPassword()))
            return ResponseEntity.ok().build();
        log.error("{} has failed to confirm by code {}", id, confirm);
        return ResponseEntity.internalServerError().build();
    }
}
