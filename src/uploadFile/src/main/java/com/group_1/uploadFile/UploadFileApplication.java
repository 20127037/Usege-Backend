package com.group_1.uploadFile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * com.group_1.uploadFile
 * Created by NhatLinh - 19127652
 * Date 4/19/2023 - 12:42 PM
 * Description: ...
 */
@SpringBootApplication(scanBasePackages = {"com.group_1.uploadFile", "com.group_1.sharedS3", "com.group_1.sharedAws"})
public class UploadFileApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(UploadFileApplication.class, args);
    }
}
