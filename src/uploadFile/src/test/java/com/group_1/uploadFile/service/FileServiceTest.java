package com.group_1.uploadFile.service;

import com.group_1.account.dto.AccountRequestDto;
import com.group_1.account.service.AccountServiceImpl;
import com.group_1.sharedAws.config.AwsClientConfig;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.*;
import com.group_1.sharedS3.repository.S3Repository;
import com.group_1.uploadFile.dto.UserFileRefUploadDto;
import com.group_1.uploadFile.dto.UserFileUploadDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * com.group_1.uploadFile.service
 * Created by NhatLinh - 19127652
 * Date 5/3/2023 - 3:15 PM
 * Description: ...
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Extensions({
        @ExtendWith(SpringExtension.class),
        @ExtendWith(MockitoExtension.class)
})
public class FileServiceTest {

    private final AwsClientConfig config = new AwsClientConfig(
            Region.AP_SOUTHEAST_1,
            ProfileCredentialsProvider.create("mobile_group_1_dev"),
            URI.create("http://localhost:4566")
    );
    private final DynamoDbEnhancedClient dynamoClient = DynamoDbEnhancedClient.builder().dynamoDbClient(DynamoDbClient.builder()
                    .region(config.region())
                    .credentialsProvider(config.credentialsProvider())
                    .endpointOverride(config.overrideUri())
                    .build())
            .build();
    private final S3Client s3Client = S3Client.builder()
            .region(config.region())
            .forcePathStyle(true)
            .credentialsProvider(config.credentialsProvider())
            .endpointOverride(config.overrideUri())
            .build();
    private final CognitoIdentityProviderClient cognitoIdentityProviderClient = CognitoIdentityProviderClient
            .builder()
            .region(config.region())
            .credentialsProvider(config.credentialsProvider())
            .build();

    private final S3Repository fileRepository = new S3Repository(s3Client, "usege");
    private final UserFileRepository userFileRepository = new UserFileRepository(dynamoClient);
    private final UserRepository userRepository = new UserRepository(dynamoClient);
    private final PaymentHistoryRepository paymentHistoryRepository = new PaymentHistoryRepository(dynamoClient);
    private final StoragePlanRepository storagePlanRepository = new StoragePlanRepository(dynamoClient);
    private final AccountServiceImpl accountService = new AccountServiceImpl(
            cognitoIdentityProviderClient,
            storagePlanRepository,
            paymentHistoryRepository,
            userRepository,
            "7b4h8r9h9f8il2qr9bii4npmo9",
            "ap-southeast-1_Fm8xzxQHY");
    private final FileServiceImpl fileService = new FileServiceImpl(
            fileRepository,
            userRepository,
            userFileRepository
    );
    private final String TEST_EMAIL = "honhatlinh@gmail.com";
    private final String TEST_PW = "Quynhshin123!";
    private String userId;

    @BeforeAll
    public void setupTest()
    {
        accountService.removeAccount(TEST_EMAIL);
        paymentHistoryRepository.clearTable();
        userRepository.clearTable();
        userFileRepository.clearTable();
        UserInfo userInfo = accountService.createAccount(
                new AccountRequestDto(TEST_EMAIL, TEST_PW));
        assertThat(userInfo, Matchers.notNullValue());
        userId = userInfo.getUserId();
    }

    @Test
    void testUserUploadNFiles()
    {
        long totalSize = 0L;
        final String[] testImgPaths = new String[]{
                "C:\\Users\\NhatLinh\\Desktop\\avatar\\bot_0_avatar.PNG",
                "C:\\Users\\NhatLinh\\Desktop\\avatar\\fake_father_avatar.PNG",
                "C:\\Users\\NhatLinh\\Desktop\\avatar\\main_avatar.PNG"
        };
        UserInfo beforeUserInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        for (String testImgPath : testImgPaths)
        {
            MockMultipartFile file;
            try {
                file = new MockMultipartFile("myFile.png", new FileInputStream(testImgPath));
                totalSize += file.getSize() / 1024;
                testShouldSaveFileAndIncreaseImgCountAfterUploading(userId, file);
            } catch (IOException ignored) {
            }
        }
        UserInfo afterUserInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        assertThat(afterUserInfo.getImgCount(), Matchers.equalTo(beforeUserInfo.getImgCount() + testImgPaths.length));
        assertThat(afterUserInfo.getUsedSpace(), Matchers.equalTo(beforeUserInfo.getUsedSpace() + totalSize));
    }

    void testShouldSaveFileAndIncreaseImgCountAfterUploading(String userId, MockMultipartFile file) {
        assertThat(file, Matchers.notNullValue());
        UserFile userFile = fileService.userUploadFile(userId, UserFileUploadDto.builder()
                .date(LocalDateTime.now().toString())
                .description("a")
                .location("a")
                .build(), file);
        assertThat(userFile, Matchers.notNullValue());
        assertThat(fileRepository.getObject(userId, userFile.getFileName()), Matchers.notNullValue());
    }

    @Test
    void testUserUploadNRefFiles()
    {
        final UserFileRefUploadDto[] files = new UserFileRefUploadDto[]{
                new UserFileRefUploadDto(String.format("pexels-%d", 16658410),
                        "https://images.pexels.com/photos/16658410/pexels-photo-16658410.jpeg",
                        "https://images.pexels.com/photos/16658410/pexels-photo-16658410.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280",
                        "Valeria November"),
                new UserFileRefUploadDto(String.format("pexels-%d", 16658410),
                        "https://images.pexels.com/photos/16658410/pexels-photo-16658410.jpeg",
                        "https://images.pexels.com/photos/16658410/pexels-photo-16658410.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280",
                        "Valeria November"),
                new UserFileRefUploadDto(String.format("pexels-%d", 16577585),
                        "https://images.pexels.com/photos/16577585/pexels-photo-16577585.jpeg",
                        "https://images.pexels.com/photos/16577585/pexels-photo-16577585.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280",
                        "Tetiana Bozhakovska")
        };
        UserInfo beforeUserInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        for (UserFileRefUploadDto file : files)
        {
            testUserUploadRefFile(userId, file);
        }
        long distinctCount =  Arrays.stream(files).map(UserFileRefUploadDto::fileName).distinct().count();
        UserInfo afterUserInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        assertThat(afterUserInfo.getImgCount(), Matchers.equalTo(beforeUserInfo.getImgCount() + distinctCount));
    }

    private void testUserUploadRefFile(String userId, UserFileRefUploadDto file)
    {
        UserFile userFile =  fileService.userUploadRefFile(userId, file);
        assertThat(userFile, Matchers.notNullValue());
    }
}