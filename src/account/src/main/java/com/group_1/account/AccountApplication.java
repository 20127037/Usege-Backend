package com.group_1.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * main
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 2:46 PM
 * Description: Main application for the authentication service
 */

@SpringBootApplication(scanBasePackages = {"com.group_1.account", "com.group_1.sharedAws"})
public class AccountApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(AccountApplication.class, args);
    }
}