package com.group_1.payment;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * com.group_1.payment
 * Created by NhatLinh - 19127652
 * Date 4/20/2023 - 3:07 PM
 * Description: ...
 */
@SpringBootApplication(scanBasePackages = {"com.group_1.payment", "com.group_1.sharedDynamoDB",
        "com.group_1.sharedAws", "com.group_1.sharedOAuth2"})
@OpenAPIDefinition
public class PaymentApplication {
    
    public static void main(String[] args)
    {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
