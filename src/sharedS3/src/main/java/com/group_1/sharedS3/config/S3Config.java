package com.group_1.sharedS3.config;

import com.group_1.sharedAws.config.AwsClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

/**
 * com.group_1.sharedAws.config
 * Created by NhatLinh - 19127652
 * Date 4/12/2023 - 12:50 AM
 * Description: ...
 */
@Configuration
public class S3Config {

    private final AwsClientConfig awsClientConfig;

    public S3Config(AwsClientConfig awsClientConfig) {
        this.awsClientConfig = awsClientConfig;
    }

    @Bean
    public S3Client s3Client() {
        S3ClientBuilder s3AsyncClientBuilder = S3Client.builder()
                .region(awsClientConfig.region())
                .forcePathStyle(true)
                .credentialsProvider(awsClientConfig.credentialsProvider());
        return s3AsyncClientBuilder.build();
    }
}
