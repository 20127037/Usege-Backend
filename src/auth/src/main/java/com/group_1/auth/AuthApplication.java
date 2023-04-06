package com.group_1.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * com.group_1.authService
 * Created by NhatLinh - 19127652
 * Date 4/4/2023 - 2:46 PM
 * Description: ...
 */
@SpringBootApplication(scanBasePackages = {"com.group_1.auth", "com.group_1.sharedAws"})
public class AuthApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(AuthApplication.class, args);
    }
}