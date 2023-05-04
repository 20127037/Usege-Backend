package com.group_1.album.service;

import com.group_1.account.dto.AccountRequestDto;
import com.group_1.account.service.AccountServiceImpl;
import com.group_1.file.dto.UserFileUploadDto;
import com.group_1.master.dto.QueryFilesInAlbumResponse;
import com.group_1.master.utils.RequestMapperUtils;
import com.group_1.sharedAws.config.AwsClientConfig;
import com.group_1.sharedDynamoDB.model.UserAlbum;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserFileInAlbum;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.*;
import com.group_1.sharedS3.repository.S3Repository;
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
import java.util.ArrayList;
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
public class AlbumServiceTest {

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
    private final UserAlbumRepository userAlbumRepository = new UserAlbumRepository(dynamoClient);
    private final UserFilesInAlbumRepository userFilesInAlbumRepository = new UserFilesInAlbumRepository(dynamoClient);
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
    private final com.group_1.master.service.AlbumService getAlbumService = new com.group_1.master.service.AlbumServiceImpl(
            userAlbumRepository,
            userFileRepository,
            userFilesInAlbumRepository
    );
    private final com.group_1.album.service.AlbumService updateAlbumService = new com.group_1.album.service.AlbumServiceImpl(
            userAlbumRepository,
            userFileRepository,
            userFilesInAlbumRepository,
            userRepository
    );
    private final String TEST_EMAIL = "honhatlinh@gmail.com";
    private final String TEST_PW = "Quynhshin123!";
    private final String ALBUM = "My album";
    private final String TO_ALBUM = "New album";
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

    @BeforeEach
    void deleteFileInAlbums()
    {
        userAlbumRepository.clearTable();
        userFilesInAlbumRepository.clearTable();
        userRepository.updateRecord(DynamoDbRepository.getKey(userId), u -> u.setAlbumCount(0L));
    }

    @Test
    void testCreateAlumIncreaseAlbumCount()
    {
        UserInfo beforeInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        UserAlbum userAlbum = updateAlbumService.createAlbum(userId, ALBUM);
        assertThat(userAlbum, Matchers.notNullValue());
        UserInfo afterInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        assertThat(afterInfo.getAlbumCount(), Matchers.equalTo(beforeInfo.getAlbumCount() + 1));
    }

//
//    UserAlbum createAlbum(String userId, String albumName);
//    UserAlbum deleteAlbum(String userId, String albumName);
//    List<UserFileInAlbum> addImagesToAlbum(String userId, String albumName, String... fileNames);
//    List<UserFileInAlbum> deleteImagesFromAlbum(String userId, String albumName, String... fileNames);
//    List<UserFileInAlbum> moveImages(String userId, String fromAlbum, String toAlbum, String... fileNames);
//    QueryResponse<UserAlbum> queryAlbums(String userId, int limit, Map<String, AttributeValue> lastEvaluatedKey);
//    QueryFilesInAlbumResponse queryImages(String userId, String albumName, int limit, Map<String, AttributeValue> startKey);

    @Test
    void testAddNotDefinedImageToAlbum()
    {
        List<UserFileInAlbum> fileInAlbums = updateAlbumService.addImagesToAlbum(userId, ALBUM, "dump file");
        assertThat(fileInAlbums.size(), Matchers.equalTo(0));
    }

    @Test
    void testAddFilesToAlbumIncreaseFileCountAndQuery()
    {
        UserAlbum beforeAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, ALBUM));
        List<UserFileInAlbum> fileInAlbums = updateAlbumService.addImagesToAlbum(userId, ALBUM, uploadedFiles.stream().map(UserFile::getFileName).toArray(String[]::new));
        UserAlbum afterAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, ALBUM));
        assertThat(afterAlbum.getImgCount(), Matchers.equalTo(beforeAlbum.getImgCount() + fileInAlbums.size()));
        int pageCount = testQueryWithNPerPage(1);
        assertThat(pageCount, Matchers.equalTo(afterAlbum.getImgCount().intValue()));
    }

    @Test
    void testAddFilesToAlbumThenRemoveAndQuery()
    {
        int delCount = 3;
        List<UserFileInAlbum> addedFiles = updateAlbumService.addImagesToAlbum(userId, ALBUM, uploadedFiles.stream().map(UserFile::getFileName).toArray(String[]::new));
        UserAlbum beforeAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, ALBUM));
        List<UserFileInAlbum> deletedFiles = updateAlbumService.deleteImagesFromAlbum(userId, ALBUM, addedFiles.subList(0, delCount).stream().map(UserFileInAlbum::getFileName).toArray(String[]::new));
        assertThat(delCount, Matchers.equalTo(deletedFiles.size()));
        UserAlbum afterAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, ALBUM));
        assertThat(afterAlbum.getImgCount(), Matchers.equalTo(beforeAlbum.getImgCount() - delCount));
        int pageCount = testQueryWithNPerPage(1);
        assertThat(pageCount, Matchers.equalTo(afterAlbum.getImgCount().intValue()));
    }

    @Test
    void testAddFilesThenMoveToAnotherAlbumAndQuery()
    {
        int moveCount = 3;
        List<UserFileInAlbum> addedFiles = updateAlbumService.addImagesToAlbum(userId, ALBUM, uploadedFiles.stream().map(UserFile::getFileName).toArray(String[]::new));
        UserAlbum beforeAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, ALBUM));
        List<UserFileInAlbum> movedFiles = updateAlbumService.moveImages(userId, ALBUM, TO_ALBUM,
                addedFiles.subList(0, moveCount).stream().map(UserFileInAlbum::getFileName).toArray(String[]::new));
        assertThat(moveCount, Matchers.equalTo(movedFiles.size()));

        UserAlbum afterAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, ALBUM));
        assertThat(afterAlbum.getImgCount(), Matchers.equalTo(beforeAlbum.getImgCount() - moveCount));
        UserAlbum afterMovedAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, TO_ALBUM));
        assertThat(afterMovedAlbum.getImgCount().intValue(), Matchers.equalTo(moveCount));

        int oldPageCount = testQueryWithNPerPage(1);
        assertThat(oldPageCount, Matchers.equalTo(afterAlbum.getImgCount().intValue()));
        int movePageCount = testQueryWithNPerPage(TO_ALBUM, 1);
        assertThat(movePageCount, Matchers.equalTo(afterMovedAlbum.getImgCount().intValue()));
    }

    int testQueryWithNPerPage(int perPage)
    {
        return testQueryWithNPerPage(ALBUM, perPage);
    }

    int testQueryWithNPerPage(String album, int perPage)
    {
        int counter = 0;
        Map<String, String> prevKey = null;
        do {
            QueryFilesInAlbumResponse queryResponse = getAlbumService.queryImages(userId, album, perPage,
                    RequestMapperUtils.mapPagingKey(prevKey));
            List<UserFile> userFiles = queryResponse.getResponse();
            counter += userFiles.size();
            prevKey = queryResponse.getNextEvaluatedKey();
            if (prevKey == null)
                continue;
            for (UserFile userFile : userFiles)
                assertThat(userFile, Matchers.notNullValue());
        } while (prevKey != null);
        return counter;
    }
}