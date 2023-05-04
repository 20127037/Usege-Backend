package com.group_1.file.service;

import com.group_1.account.dto.AccountRequestDto;
import com.group_1.account.service.AccountServiceImpl;
import com.group_1.master.utils.RequestMapperUtils;
import com.group_1.sharedAws.config.AwsClientConfig;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.*;
import com.group_1.sharedS3.repository.S3Repository;
import com.group_1.file.dto.UserFileRefUploadDto;
import com.group_1.file.dto.UserFileUploadDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
//@ComponentScan({"com.group_1.master", "com.group_1.file", "com.group_1.sharedDynamoDB", "com.group_1.sharedAws"})
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
    private final UserDeletedFileRepository userDeletedFileRepository = new UserDeletedFileRepository(dynamoClient);
    private final AccountServiceImpl accountService = new AccountServiceImpl(
            cognitoIdentityProviderClient,
            storagePlanRepository,
            paymentHistoryRepository,
            userRepository,
            "7b4h8r9h9f8il2qr9bii4npmo9",
            "ap-southeast-1_Fm8xzxQHY");
    private final com.group_1.file.service.FileServiceImpl uploadFileService
            = new com.group_1.file.service.FileServiceImpl(
            fileRepository,
            userRepository,
            userFileRepository
    );
    private final com.group_1.master.service.FileService getFileService = new com.group_1.master.service.FileServiceImpl(
            userFileRepository,
            userDeletedFileRepository
    );
    private final com.group_1.file.service.FileService updateFileService = new com.group_1.file.service.FileServiceImpl(
            fileRepository,
            userRepository,
            userFileRepository
    );

    private final String TEST_EMAIL = "honhatlinh@gmail.com";
    private final String TEST_PW = "Quynhshin123!";
    private String userId;
    private final List<UserFile> uploadedFiles = new ArrayList<>();
    private final String[] testImgPaths = new String[]{
            "C:\\Users\\NhatLinh\\Desktop\\avatar\\bot_0_avatar.PNG",
            "C:\\Users\\NhatLinh\\Desktop\\avatar\\fake_father_avatar.PNG",
            "C:\\Users\\NhatLinh\\Desktop\\avatar\\main_avatar.PNG",
            "C:\\Users\\NhatLinh\\Desktop\\avatar\\pexels-photo-16577585.jpeg",
            "C:\\Users\\NhatLinh\\Desktop\\avatar\\pexels-photo-16614145.jpeg",
    };
    @BeforeAll
    public void setupTest()
    {
        accountService.removeAccount(TEST_EMAIL);
        paymentHistoryRepository.clearTable();
        userRepository.clearTable();
        userFileRepository.clearTable();
        userDeletedFileRepository.clearTable();
        UserInfo userInfo = accountService.createAccount(
                new AccountRequestDto(TEST_EMAIL, TEST_PW));
        assertThat(userInfo, Matchers.notNullValue());
        userId = userInfo.getUserId();
        setupFiles();
    }

    void setupFiles()
    {

        for (String testImgPath : testImgPaths)
        {
            MockMultipartFile file;
            try {
                file = new MockMultipartFile("myFile.png", new FileInputStream(testImgPath));
                uploadedFiles.add(uploadFileService.userUploadFile(userId, UserFileUploadDto.builder()
                        .date(LocalDateTime.now().toString())
                        .description("a")
                        .location("a")
                        .build(), file));
            } catch (IOException ignored) {
            }
        }
    }

    @Test
    void testGetFileByNameWithAllFiles()
    {
        for (UserFile userFile : uploadedFiles)
        {
            UserFile file = getFileService.getFileByName(userId, userFile.getFileName(), false);
            assertThat(file, Matchers.notNullValue());
            assertThat(file.getFileName(), Matchers.equalTo(userFile.getFileName()));
        }
    }
    @Test
    void testQueryAllFilesWith1FilePerPage()
    {
        int counter = testQueryWithNPerPage(1, null);
        assertThat(counter, Matchers.equalTo(testImgPaths.length));
    }
    @Test
    void testQueryAllFilesWith3FilePerPage()
    {
        int counter = testQueryWithNPerPage(3, null);
        assertThat(counter, Matchers.equalTo((int)Math.floor(testImgPaths.length / 3f)));
    }
    @Test
    void testQueryAllFavoriteFilesWith1FilePerPage()
    {
        testQueryWithNPerPage(1, true);
    }



    int testQueryWithNPerPage(int perPage, Boolean favorite)
    {
        int counter = 0;
        Map<String, String> prevKey = null;
        do {
            QueryResponse<UserFile> queryResponse = getFileService.queryFiles(userId, perPage,
                    favorite, RequestMapperUtils.mapPagingKey(prevKey), null);
            List<UserFile> userFiles = queryResponse.getResponse();
            String updated = null;
            for (UserFile userFile : userFiles)
            {
                assertThat(userFile, Matchers.notNullValue());
                if (updated != null)
                    assertThat(userFile.getUpdated(), Matchers.lessThan(updated));
                updated = userFile.getUpdated();
            }
            prevKey = queryResponse.getNextEvaluatedKey();
            counter++;
        } while (prevKey != null);
        return counter;
    }

    @Test
    void testMakeFavouriteThenQuery()
    {
        int favCount = 3;
        QueryResponse<UserFile> queryResponse = getFileService.queryFiles(userId, testImgPaths.length,
                false, null, null);
        assertThat(queryResponse.getResponse().size(), Matchers.equalTo(testImgPaths.length));
        for (int i = 0; i < favCount; i++)
        {
            updateFileService.updateFile(userId, UserFile.builder()
                    .fileName(uploadedFiles.get(i).getFileName())
                    .isFavourite(true).build());
        }
        queryResponse = getFileService.queryFiles(userId, testImgPaths.length,
                false, null, null);
        assertThat(queryResponse.getResponse().size(), Matchers.equalTo(testImgPaths.length - favCount));
        queryResponse = getFileService.queryFiles(userId, testImgPaths.length,
                true, null, null);
        assertThat(queryResponse.getResponse().size(), Matchers.equalTo(favCount));
    }

    @Test
    void testUpdateAllPossibleFieldsOfFile()
    {
        final List<String> tags = new ArrayList<>();
        tags.add("a");
        tags.add("b");
        tags.add("c");
        final String des = "My image";
        final String date = LocalDateTime.now().toString();
        final String loc = "My location";
        UserFile update = UserFile.builder()
                .fileName(uploadedFiles.get(0).getFileName())
                .tags(tags)
                .description(des)
                .date(date)
                .isFavourite(true)
                .location(loc)
                .build();
        UserFile updated = updateFileService.updateFile(userId, update);
        assertThat(updated.getDescription(), Matchers.equalTo(des));
        assertThat(updated.getDate(), Matchers.equalTo(date));
        assertThat(updated.getLocation(), Matchers.equalTo(loc));
        assertThat(updated.getIsFavourite(), Matchers.equalTo(true));
        assertThat(updated.getTags(), Matchers.equalTo(tags));
    }
    private void testUserUploadRefFile(String userId, UserFileRefUploadDto file)
    {
        UserFile userFile =  updateFileService.userUploadRefFile(userId, file);
        assertThat(userFile, Matchers.notNullValue());
    }
}