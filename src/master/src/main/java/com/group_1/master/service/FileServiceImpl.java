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

    @Override
    public QueryResponse<UserFile> queryFiles(String userId, int limit,
                                              Boolean isFavourite,
                                              Map<String, AttributeValue> startKey, String[] attributes) {
        Expression filterExpression = null;
        if (isFavourite != null)
        {
            filterExpression = Expression.builder()
                    .expression("#f = :fav")
                    .putExpressionName("#f", UserFile.Fields.isFavourite)
                    .putExpressionValue(":fav", AttributeValue.fromBool(isFavourite))
                    .build();
        }
        return userFileDbRepository.query(
                DynamoDbRepository.getQueryConditional(DynamoDbRepository.getKey(userId)), filterExpression, UserFile.Indexes.UPDATED, limit, startKey, false, attributes);
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

}
