package com.group_1.master.service;

import com.group_1.account.dto.AccountRequestDto;
import com.group_1.account.service.AccountServiceImpl;
import com.group_1.master.dto.QueryFilesInAlbumResponse;
import com.group_1.master.utils.RequestMapperUtils;
import com.group_1.sharedAws.config.AwsClientConfig;
import com.group_1.sharedDynamoDB.model.*;
import com.group_1.sharedDynamoDB.repository.*;
import com.group_1.sharedS3.repository.S3Repository;
import com.group_1.uploadFile.dto.UserFileUploadDto;
import org.apache.catalina.User;
import org.hamcrest.Matcher;
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
public class TrashBinServiceTest {

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
    private final com.group_1.master.service.FileServiceImpl fileService = new com.group_1.master.service.FileServiceImpl(
            userFileRepository,
            userDeletedFileRepository
    );
    private final com.group_1.uploadFile.service.FileServiceImpl uploadFileService
            = new com.group_1.uploadFile.service.FileServiceImpl(
            fileRepository,
            userRepository,
            userFileRepository
    );
    private final AlbumService albumService = new AlbumServiceImpl(
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
    private final TrashBinService trashBinService =
            new TrashBinServiceImpl(userFileRepository, userRepository, userDeletedFileRepository, userFilesInAlbumRepository, albumService);

    @BeforeAll
    public void setupTest() {
        accountService.removeAccount(TEST_EMAIL);
        paymentHistoryRepository.clearTable();
        userRepository.clearTable();
        UserInfo userInfo = accountService.createAccount(
                new AccountRequestDto(TEST_EMAIL, TEST_PW));
        assertThat(userInfo, Matchers.notNullValue());
        userId = userInfo.getUserId();
    }

    void setupFiles() {
        uploadedFiles.clear();
        for (String testImgPath : testImgPaths) {
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
    void deleteFileInAlbums() {
        userAlbumRepository.clearTable();
        userFilesInAlbumRepository.clearTable();
        userDeletedFileRepository.clearTable();
        userFileRepository.clearTable();
        userRepository.updateRecord(DynamoDbRepository.getKey(userId), u -> {
            u.setAlbumCount(0L);
            u.setUsedSpace(0L);
            u.setDeletedImgCount(0L);
            u.setImgCount(0L);
        });
        setupFiles();
    }

    @Test
    void testDeleteFileIncreaseImgCountDecreaseDelImgCount() {
        int delCount = 3;
        UserInfo before = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        List<UserFile> delFiles = trashBinService.createDeletedFiles(userId, uploadedFiles.subList(0, delCount).stream().map(UserFile::getFileName).toArray(String[]::new));
        assertThat(delFiles.size(), Matchers.equalTo(delCount));
        checkTrashValuesAfterDel(before, delCount);
    }

    @Test
    void testDeleteAllFilesThenQuery() {
        UserInfo before = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        trashBinService.createDeletedFilesFromAllFiles(userId);
        checkTrashValuesAfterDel(before, before.getImgCount().intValue());
    }

    @Test
    void testRestoreFiles() {
        int resCount = 3;
        trashBinService.createDeletedFilesFromAllFiles(userId);
        UserInfo before = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        List<UserFile> resFiles = trashBinService.restoreFiles(userId, uploadedFiles.subList(0, resCount).stream().map(UserFile::getFileName).toArray(String[]::new));
        assertThat(resFiles.size(), Matchers.equalTo(resCount));
        checkTrashValuesAfterRes(before, resCount);
    }

    @Test
    void testRestoreAllFiles() {
        trashBinService.createDeletedFilesFromAllFiles(userId);
        UserInfo before = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        List<UserFile> resFiles = trashBinService.restoreAll(userId);
        assertThat(resFiles.size(), Matchers.equalTo(before.getDeletedImgCount().intValue()));
        checkTrashValuesAfterRes(before, resFiles.size());
    }

    @Test
    void testAddFilesToAlbumThenRemoveAndQueryAlbum() {
        int delCount = 2;
        int addImgCount = 4;
        albumService.addImagesToAlbum(userId, ALBUM, uploadedFiles.subList(0, addImgCount).stream().map(UserFile::getFileName).toArray(String[]::new));
        trashBinService.createDeletedFiles(userId, uploadedFiles.subList(0, delCount).stream().map(UserFile::getFileName).toArray(String[]::new));
        UserAlbum userAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, ALBUM));
        assertThat(userAlbum.getImgCount().intValue(), Matchers.equalTo(addImgCount - delCount));
        assertThat(testQueryAlbumWithNPerPage(ALBUM,1), Matchers.equalTo(addImgCount - delCount));
        assertThat(testQueryTrashWithNPerPage(1), Matchers.equalTo(delCount));
    }

    @Test
    void testAddFilesToAlbumThenDeleteAndRestoreAndQueryAlbum() {
        int firstAdd = 3;
        int secondAdd = uploadedFiles.size() - firstAdd;
        albumService.addImagesToAlbum(userId, ALBUM, uploadedFiles.subList(0, firstAdd).stream().map(UserFile::getFileName).toArray(String[]::new));
        albumService.addImagesToAlbum(userId, TO_ALBUM, uploadedFiles.subList(firstAdd, uploadedFiles.size()).stream().map(UserFile::getFileName).toArray(String[]::new));

        trashBinService.createDeletedFilesFromAllFiles(userId);

        Matcher<Integer> equal0 = Matchers.equalTo(0);
        UserAlbum firstAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, ALBUM));
        assertThat(firstAlbum.getImgCount().intValue(), equal0);
        UserAlbum secAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, TO_ALBUM));
        assertThat(secAlbum.getImgCount().intValue(), equal0);
        assertThat(testQueryAlbumWithNPerPage(ALBUM,1), equal0);
        assertThat(testQueryAlbumWithNPerPage(TO_ALBUM,1), equal0);

        trashBinService.restoreAll(userId);

        firstAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, ALBUM));
        assertThat(firstAlbum.getImgCount().intValue(), Matchers.equalTo(firstAdd));
        secAlbum = userAlbumRepository.getRecordByKey(DynamoDbRepository.getKey(userId, TO_ALBUM));
        assertThat(secAlbum.getImgCount().intValue(), Matchers.equalTo(secondAdd));
        assertThat(testQueryAlbumWithNPerPage(ALBUM,1), Matchers.equalTo(firstAdd));
        assertThat(testQueryAlbumWithNPerPage(TO_ALBUM,1), Matchers.equalTo(secondAdd));
    }


    @Test
    void testDelFilesInAlbumThenQueryAlbum() {

    }

    void checkTrashValuesAfterRes(UserInfo before, int resCount) {
        UserInfo after = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        assertThat(after.getImgCount(), Matchers.equalTo(before.getImgCount() + resCount));
        assertThat(after.getDeletedImgCount(), Matchers.equalTo(before.getDeletedImgCount() - resCount));
        checkQueryAllAndTrash(after);
    }


    void checkTrashValuesAfterDel(UserInfo before, int delCount) {
        UserInfo after = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        assertThat(after.getImgCount(), Matchers.equalTo(before.getImgCount() - delCount));
        assertThat(after.getDeletedImgCount(), Matchers.equalTo(before.getDeletedImgCount() + delCount));
        checkQueryAllAndTrash(after);
    }

    void checkQueryAllAndTrash(UserInfo userInfo)
    {
        assertThat(testQueryTrashWithNPerPage(1), Matchers.equalTo(userInfo.getDeletedImgCount().intValue()));
        assertThat(testQueryWithNPerPage(1), Matchers.equalTo(userInfo.getImgCount().intValue()));
    }


    int testQueryTrashWithNPerPage(int perPage)
    {
        int counter = 0;
        Map<String, String> prevKey = null;
        do {
            QueryResponse<UserFile> queryResponse = trashBinService.queryImages(userId, perPage, RequestMapperUtils.mapPagingKey(prevKey));
            List<UserFile> userFiles = queryResponse.getResponse();
            counter += userFiles.size();
            prevKey = queryResponse.getNextEvaluatedKey();
            String updated = null;
            for (UserFile userFile : userFiles)
            {
                assertThat(userFile, Matchers.notNullValue());
                if (updated != null)
                    assertThat(userFile.getUpdated(), Matchers.lessThan(updated));
                updated = userFile.getUpdated();
            }
        } while (prevKey != null);
        return counter;
    }

    int testQueryAlbumWithNPerPage(String album, int perPage)
    {
        int counter = 0;
        Map<String, String> prevKey = null;
        do {
            QueryFilesInAlbumResponse queryResponse = albumService.queryImages(userId, album, perPage,
                    RequestMapperUtils.mapPagingKey(prevKey));
            List<UserFile> userFiles = queryResponse.getResponse();
            counter += userFiles.size();
            prevKey = queryResponse.getNextEvaluatedKey();
            for (UserFile userFile : userFiles)
                assertThat(userFile, Matchers.notNullValue());
        } while (prevKey != null);
        return counter;
    }

    int testQueryWithNPerPage(int perPage)
    {
        int counter = 0;
        Map<String, String> prevKey = null;
        do {
            QueryResponse<UserFile> queryResponse = fileService.queryFiles(userId, perPage,
                    null, RequestMapperUtils.mapPagingKey(prevKey), null);
            List<UserFile> userFiles = queryResponse.getResponse();
            counter += userFiles.size();
            prevKey = queryResponse.getNextEvaluatedKey();
            String updated = null;
            for (UserFile userFile : userFiles)
            {
                assertThat(userFile, Matchers.notNullValue());
                if (updated != null)
                    assertThat(userFile.getUpdated(), Matchers.lessThan(updated));
                updated = userFile.getUpdated();
            }
        } while (prevKey != null);
        return counter;
    }
}