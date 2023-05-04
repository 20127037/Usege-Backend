package com.group_1.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * com.group_1.authService
 * Created by NhatLinh - 19127652
 * Date 4/4/2023 - 2:46 PM
 * Description: ...
 */
@SpringBootApplication(scanBasePackages = {"com.group_1.auth", "com.group_1.sharedCognito", "com.group_1.sharedAws"})
@OpenAPIDefinition
public class AuthApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(AuthApplication.class, args);
    }
}