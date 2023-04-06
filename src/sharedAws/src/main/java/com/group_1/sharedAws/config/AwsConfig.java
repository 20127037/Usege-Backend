package com.group_1.sharedAws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;

import java.net.URI;

@Configuration
public class AwsConfig {
    @Value("${amazon.aws.region}")
    private String region;
    @Value("${amazon.aws.profileName}")
    private String profileName;
    @Value("${amazon.aws.uri:#{null}}")
    private String uri;

    @Bean
    public AwsClientConfig awsClientConfig() {
        URI overrideUri = null;
        if (uri != null && !uri.isEmpty())
            overrideUri = URI.create(uri);
        return new AwsClientConfig(Region.of(region), ProfileCredentialsProvider.create(profileName), overrideUri);
    }
}
