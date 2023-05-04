package com.group_1.file;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * com.group_1.uploadFile
 * Created by NhatLinh - 19127652
 * Date 4/19/2023 - 12:42 PM
 * Description: ...
 */
@SpringBootApplication(scanBasePackages = {"com.group_1.file", "com.group_1.sharedS3", "com.group_1.sharedAws",
        "com.group_1.sharedDynamoDB", "com.group_1.sharedOAuth2"})
@OpenAPIDefinition
public class FileApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(FileApplication.class, args);
    }
}
