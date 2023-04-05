package com.group_1.auth.controller;

import com.group_1.auth.dto.LoginRequestDto;
import com.group_1.auth.dto.ResponseTokenDto;
import com.group_1.auth.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;

import javax.servlet.ServletContext;
import java.net.URI;

/**
 * com.group_1.authService.controller
 * Created by NhatLinh - 19127652
 * Date 4/4/2023 - 2:10 PM
 * Description: ...
 */
@RestController
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ServletContext context;
    @PostMapping
    public ResponseEntity<ResponseTokenDto> login(@RequestBody LoginRequestDto loginRequest)
    {
        AuthenticationResultType authResult = authService.login(loginRequest.email(), loginRequest.password());
        if (authResult == null)
        {
            log.error("Failed to login {}", loginRequest.email());
            return ResponseEntity.internalServerError().build();
        }
        ResponseTokenDto token = ResponseTokenDto.builder()
                .accessToken(authResult.accessToken())
                .idToken(authResult.idToken())
                .refreshToken(authResult.refreshToken())
                .expiresIn(authResult.expiresIn())
                .build();
        return ResponseEntity.created(URI.create(context.getContextPath()))
                .body(token);
    }

    //grant_type=refresh_token&refresh_token=tGzv3JOkF0XG5Qx2TlKWIA
    @PutMapping
    public ResponseEntity<ResponseTokenDto> refresh(@RequestParam("refresh_token") String refreshToken)
    {
        AuthenticationResultType refreshResult = authService.refresh(refreshToken);
        if (refreshResult == null)
        {
            log.error("Failed to get new token {}", refreshToken);
            return ResponseEntity.internalServerError().build();
        }
        ResponseTokenDto token = ResponseTokenDto.builder()
                .accessToken(refreshResult.accessToken())
                .idToken(refreshResult.idToken())
                .refreshToken(refreshResult.refreshToken())
                .expiresIn(refreshResult.expiresIn())
                .build();
        return ResponseEntity.created(URI.create(context.getContextPath()))
                .body(token);
    }
}
