package com.group_1.master.service;

import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.repository.DynamoDbRepository;
import com.group_1.sharedDynamoDB.repository.UserDeletedFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 2:14 PM
 * Description: ...
 */
@Service
@AllArgsConstructor
public class TrashBinServiceImpl implements TrashBinService {
    private final UserDeletedFileRepository userDeletedFileRepository;

    @Override
    public QueryResponse<UserFile> queryImages(String userId, int limit, Map<String, AttributeValue> startKey) {
        return userDeletedFileRepository
                .query(DynamoDbRepository.getQueryConditional(DynamoDbRepository.getKey(userId)),
                        null,
                        UserFile.Indexes.UPDATED,
                        limit,
                        startKey,
                        false);
    }

    @Override
    public UserFile getImage(String userId, String fileName) {
        UserFile userFile = userDeletedFileRepository.getRecordByKey(DynamoDbRepository.getKey(userId, fileName));
        if (userFile == null)
            throw new NoSuchElementFoundException(fileName, "files");
        return userFile;
    }
}
