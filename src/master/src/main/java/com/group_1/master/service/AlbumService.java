package com.group_1.master.service;

import com.group_1.master.dto.QueryFilesInAlbumResponse;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserAlbum;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 1:47 PM
 * Description: ...
 */
public interface AlbumService {
    QueryResponse<UserAlbum> queryAlbums(String userId, int limit, Map<String, AttributeValue> lastEvaluatedKey);
    QueryFilesInAlbumResponse queryImages(String userId, String albumName, int limit, Map<String, AttributeValue> startKey);
}