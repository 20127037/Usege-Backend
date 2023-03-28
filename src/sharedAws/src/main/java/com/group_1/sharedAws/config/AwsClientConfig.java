package com.group_1.sharedAws.config;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;

import java.net.URI;

/**
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 5:56 PM
 * Description: Configuration for the dynamoDb (use TestConfiguration when testing)
 */

public record AwsClientConfig(Region region, AwsCredentialsProvider credentialsProvider, URI overrideUri) {
}
