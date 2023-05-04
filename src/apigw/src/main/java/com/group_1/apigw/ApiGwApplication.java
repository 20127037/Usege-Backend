package com.group_1.apigw;

import com.netflix.discovery.EurekaNamespace;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PACKAGE_NAME
 * Created by NhatLinh - 19127652
 * Date 4/4/2023 - 4:30 PM
 * Description: ...
 */
@SpringBootApplication
@OpenAPIDefinition
public class ApiGwApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(ApiGwApplication.class, args);
    }
}
