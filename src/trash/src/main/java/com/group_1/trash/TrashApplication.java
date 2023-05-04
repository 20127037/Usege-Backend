package com.group_1.trash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * main
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 6:08 PM
 * Description: Main entry for the master service
 */
@SpringBootApplication(scanBasePackages = {"com.group_1.trash", "com.group_1.sharedDynamoDB", "com.group_1.sharedAws", "com.group_1.sharedOAuth2", "com.group_1.amqp"})
public class TrashApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(TrashApplication.class, args);
    }
}
