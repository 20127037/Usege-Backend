package com.group_1.account.controller;

import com.group_1.account.dto.AccountRequestDto;
import com.group_1.account.dto.ConfirmForgetPasswordDto;
import com.group_1.account.service.AccountService;
import com.group_1.sharedAws.model.UserInfo;
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
@RestController
@AllArgsConstructor
@Slf4j
public class UserAccountController {
    private final AccountService accountService;
    @PostMapping
    public ResponseEntity<UserInfo> createAccount(HttpServletRequest request, @RequestBody AccountRequestDto requestDto)
    {
        UserInfo response = accountService.createAccount(requestDto);
        log.info("{} ({}) has been created", response.getEmail(), response.getUserId());
        return ResponseEntity.created(URI.create(request.getRequestURI() + "/" + response.getUserId())).body(response);
    }
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


    @PostMapping("forget/{id}")
    public ResponseEntity<Object> forgetPassword(HttpServletRequest request, @PathVariable String id)
    {
        accountService.forgetPassword(id);
        return ResponseEntity.created(URI.create(request.getRequestURI())).build();
    }

    @PutMapping("forget/{id}")
    public ResponseEntity<Object> confirmForgetPassword(@PathVariable String id, @RequestBody ConfirmForgetPasswordDto confirm)
    {
        if (accountService.confirmForgetPassword(id, confirm.confirmCode(), confirm.newPassword()))
            return ResponseEntity.ok().build();
        log.error("{} has failed to confirm by code {}", id, confirm);
        return ResponseEntity.internalServerError().build();
    }
}
