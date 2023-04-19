package com.group_1.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(scanBasePackages = {"com.group_1.file", "com.group_1.sharedAws", "com.group_1.sharedCognito", "com.group_1.sharedDynamoDB"})
@SpringBootApplication
public class FileApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(FileApplication.class, args);
    }
}