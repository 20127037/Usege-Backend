package com.group_1.sharedS3.config;

import com.group_1.sharedAws.config.AwsClientConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
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
@AllArgsConstructor
public class S3Config {
    private final AwsClientConfig awsClientConfig;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }
    @Bean
    public S3Client dynamoDbClient() {
        S3ClientBuilder s3AsyncClientBuilder = S3Client.builder()
                .region(awsClientConfig.region())
                .forcePathStyle(true)
                .credentialsProvider(awsClientConfig.credentialsProvider());
        URI overrideUri = awsClientConfig.overrideUri();
        if (overrideUri != null)
            s3AsyncClientBuilder.endpointOverride(overrideUri);
        return s3AsyncClientBuilder.build();
    }
}
