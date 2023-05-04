package com.group_1.account;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * main
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 2:46 PM
 * Description: Main application for the authentication service
 */

@SpringBootApplication(scanBasePackages = {"com.group_1.account", "com.group_1.sharedAws", "com.group_1.sharedCognito", "com.group_1.sharedDynamoDB"})
@OpenAPIDefinition
public class AccountApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(AccountApplication.class, args);
    }
}