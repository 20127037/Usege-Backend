package com.group_1.master.service;

import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.repository.UserFileDbRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.UUID;

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


    @Override
    public QueryResponse<UserFile> queryFiles(String userId, int limit, Map<String, AttributeValue> startKey, String[] attributes) {
        return userFileDbRepository.query(
                QueryConditional.keyEqualTo(Key.builder().partitionValue(userId).build()),
                limit, startKey, false, attributes);
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
