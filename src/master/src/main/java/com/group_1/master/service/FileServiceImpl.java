package com.group_1.master.service;

import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.repository.UserFileDbRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
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

    private final UserFileDbRepository userFileDbRepository;
    private QueryResponse<UserFile> queryFileInternal(String userId,
                                              Expression expression,
                                              int limit,
                                              Map<String, AttributeValue> startKey,
                                              String[] attributes) {
        QueryConditional conditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(userId).build());
        return userFileDbRepository.query(conditional, expression,
                limit, startKey, false, attributes);
    }


    @Override
    public QueryResponse<UserFile> queryFiles(String userId, int limit,
                                              Boolean isFavourite, Boolean isDeleted,
                                              Map<String, AttributeValue> startKey, String[] attributes) {
        Expression.Builder expressionBuilder = Expression.builder();
        if (isDeleted != null)
            expressionBuilder.putExpressionValue(UserFile.Fields.isDeleted, AttributeValue.fromBool(isDeleted));
        if (isFavourite != null)
            expressionBuilder.putExpressionValue(UserFile.Fields.isFavourite, AttributeValue.fromBool(isFavourite));
        return queryFileInternal(userId, expressionBuilder.build(), limit, startKey, attributes);
    }

    @Override
    public UserFile getFile(String userId, String fileName, String uri) {

        Expression.Builder expressionBuilder = Expression.builder();
        if (fileName != null)
            expressionBuilder.putExpressionValue(UserFile.Fields.fileName, AttributeValue.fromS(fileName));
        if (uri != null)
            expressionBuilder.putExpressionValue(UserFile.Fields.originalUri, AttributeValue.fromS(uri));
        QueryResponse<UserFile> response = queryFileInternal(userId, expressionBuilder.build(), 1, null, new String[]{
                UserFile.Fields.userId
        });
        List<UserFile> userFile = response.getResponse();
        if (userFile == null || userFile.isEmpty())
            throw new NoSuchElementFoundException(fileName, "userFiles");
        return userFile.get(0);
    }

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
