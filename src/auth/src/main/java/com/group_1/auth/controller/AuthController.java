package com.group_1.auth.controller;

import com.group_1.auth.dto.LoginRequestDto;
import com.group_1.auth.dto.ResponseTokenDto;
import com.group_1.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@Tag(name = "Authenticator controller", description = "Get/refresh an access token")
public class AuthController {

    private final AuthService authService;
    private final ServletContext context;
    @PostMapping
    @Operation(summary = "Get new access token from an email password pair")
    public ResponseEntity<ResponseTokenDto> login(@RequestBody LoginRequestDto loginRequest)
    {
        ResponseTokenDto authResult = authService.login(loginRequest.email(), loginRequest.password());
        if (authResult == null)
        {
            log.error("Failed to login {}", loginRequest.email());
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.created(URI.create(context.getContextPath()))
                .body(authResult);
    }

    //grant_type=refresh_token&refresh_token=tGzv3JOkF0XG5Qx2TlKWIA
    @PutMapping
    @Operation(summary = "Get new access token from an existing refresh token")
    public ResponseEntity<ResponseTokenDto> refresh(@RequestParam("refresh_token") String refreshToken)
    {
        ResponseTokenDto refreshResult = authService.refresh(refreshToken);
        if (refreshResult == null)
        {
            log.error("Failed to get new token {}", refreshToken);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.created(URI.create(context.getContextPath()))
                .body(refreshResult);
    }
}
