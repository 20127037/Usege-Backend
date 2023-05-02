package com.group_1.master.service;

import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.repository.DynamoDbRepository;
import com.group_1.sharedDynamoDB.repository.UserDeletedFileRepository;
import com.group_1.sharedDynamoDB.repository.UserFileRepository;
import com.group_1.sharedDynamoDB.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 4/21/2023 - 4:21 PM
 * Description: ...
 */
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final UserFileRepository userFileDbRepository;
    private final UserDeletedFileRepository userDeletedRepository;
    private final UserRepository userRepository;

    @Override
    public QueryResponse<UserFile> queryFiles(String userId, int limit,
                                              Boolean isFavourite,
                                              Map<String, AttributeValue> startKey, String[] attributes) {
        Expression filterExpression = null;
        if (isFavourite != null)
        {
            filterExpression = Expression.builder()
                    .putExpressionValue(UserFile.Fields.isFavourite, AttributeValue.fromBool(isFavourite))
                    .build();
        }
        return userFileDbRepository.query(
                DynamoDbRepository.getQueryConditional(userId, null), filterExpression, UserFile.Indexes.UPDATED, limit, startKey, false, attributes);
    }

    @Override
    public UserFile getFileByName(String userId, String fileName, boolean deletedInclude) {
        UserFile userFile = userFileDbRepository.getRecordByKey(DynamoDbRepository.getKey(userId, fileName));
        if (userFile == null)
        {
            userFile = userDeletedRepository.getRecordByKey(DynamoDbRepository.getKey(userId, fileName));
            if (userFile == null)
                throw new NoSuchElementFoundException(fileName, "userFiles.indexes");
        }
        return userFile;
    }

    @Override
    public UserFile updateFile(String userId, UserFile update) {
        return userFileDbRepository.updateRecord(DynamoDbRepository.getKey(userId, update.getFileName()), f -> {
            if (update.getTags() != null)
                f.setTags(update.getTags());
            if (update.getDescription() != null)
                f.setDescription(update.getDescription());
            if (update.getDate() != null)
                f.setDate(update.getDate());
            if (update.getIsFavourite() != null)
                f.setIsFavourite(update.getIsFavourite());
            if (update.getLocation() != null)
                f.setLocation(update.getLocation());
        });
    }
//    @Override
//    public UserFile getFileByOriginalUri(String userId, String uri) {
//        QueryResponse<UserFile> response = queryFileInternal(userId, null,
//                new AbstractMap.SimpleEntry<>(UserFile.Indexes.URI, uri),
//                1, null, new String[]{
//                        UserFile.Fields.userId
//                });
//        List<UserFile> userFile = response.getResponse();
//        if (userFile == null || userFile.isEmpty())
//            throw new NoSuchElementFoundException(uri, "userFiles.uri");
//        return userFile.get(0);
//    }

//    public void addUserFileDummy()
//    {
//        for (int i = 0; i < 10; i++)
//        {
//            userFileDbRepository.saveRecord(UserFile.builder()
//                    .userId("link")
//                    .fileId(UUID.randomUUID().toString())
//                    .contentType("image")
//                    .build());
//        }
//    }
}
