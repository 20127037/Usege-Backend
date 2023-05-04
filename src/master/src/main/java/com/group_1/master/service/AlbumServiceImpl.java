package com.group_1.master.service;

import com.group_1.master.dto.QueryFilesInAlbumResponse;
import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.*;
import com.group_1.sharedDynamoDB.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 2:14 PM
 * Description: ...
 */
@Service
@AllArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final UserAlbumRepository userAlbumRepository;
    private final UserFileRepository userFileRepository;
    private final UserFilesInAlbumRepository userFilesInAlbumRepository;

    @Override
    public QueryResponse<UserAlbum> queryAlbums(String userId, int limit, Map<String, AttributeValue> lastEvaluatedKey) {
        return userAlbumRepository.query(
                DynamoDbRepository.getQueryConditional(DynamoDbRepository.getKey(userId)),
                null,
                limit,
                lastEvaluatedKey,
                false
        );
    }

    @Override
    public QueryFilesInAlbumResponse queryImages(String userId, String albumName, int limit, Map<String, AttributeValue> startKey) {
        QueryResponse<UserFileInAlbum> albumQueryResponse = userFilesInAlbumRepository
                .query(DynamoDbRepository.getQueryConditional(DynamoDbRepository.getKey(userId, albumName)),
                        null,
                        UserFileInAlbum.Indexes.ALBUM_NAME,
                        limit,
                        startKey,
                        false);
        List<UserFile> resultSet = new ArrayList<>();
        for (UserFileInAlbum userFileInAlbum : albumQueryResponse.getResponse())
        {
            UserFile userFile = userFileRepository.getRecordByKey(DynamoDbRepository.getKey(userId, userFileInAlbum.getFileName()));
            if (userFile == null)
                continue;
            resultSet.add(userFile);
        }
        return QueryFilesInAlbumResponse.builder()
                .albumName(albumName)
                .prevEvaluatedKey(null)
                .nextEvaluatedKey(albumQueryResponse.getNextEvaluatedKey())
                .response(resultSet).build();
    }
}
