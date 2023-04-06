package com.group_1.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * com.group_1.eureka
 * Created by NhatLinh - 19127652
 * Date 4/4/2023 - 4:03 PM
 * Description: ...
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
