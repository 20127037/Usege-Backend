package com.group_1.authenticationService.controller;

import com.group_1.authenticationService.dto.CreateAccountRequestDto;
import com.group_1.authenticationService.dto.CreateAccountResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.group_1.authenticationService.service.AccountService;

import javax.servlet.ServletContext;
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
    private final ServletContext context;
    @PostMapping
    public ResponseEntity<CreateAccountResponseDto> createAccount(@RequestBody CreateAccountRequestDto requestDto)
    {
        CreateAccountResponseDto response = accountService.createAccount(requestDto);
        log.info("{} ({}) has been created", response.email(), response.userId());
        return ResponseEntity.created(URI.create(context.getContextPath() + "/" + response.userId())).body(response);
    }

    @PostMapping("auth")
    public void testAuth()
    {

    }
}
