package com.group_1.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * main
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 6:08 PM
 * Description: Main entry for the master service
 */
@SpringBootApplication(scanBasePackages = {"com.group_1.master", "com.group_1.sharedDynamoDB", "com.group_1.sharedAws"})
public class MasterApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(MasterApplication.class, args);
    }
}
