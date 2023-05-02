package com.group_1.master.service;

import com.group_1.master.dto.QueryFilesInAlbumResponse;
import com.group_1.sharedDynamoDB.model.UserAlbum;
import com.group_1.sharedDynamoDB.model.UserFileInAlbum;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 1:47 PM
 * Description: ...
 */
public interface AlbumService {
    UserAlbum createAlbum(String userId, String albumName);
    UserAlbum deleteAlbum(String userId, String albumName);
    List<UserFileInAlbum> addImagesToAlbum(String userId, String albumName, String... fileNames);
    List<UserFileInAlbum> deleteImagesFromAlbum(String userId, String albumName, String... fileNames);
    List<UserFileInAlbum> moveImages(String userId, String fromAlbum, String toAlbum, String... fileNames);
    QueryFilesInAlbumResponse queryImages(String userId, String albumName, int limit, Map<String, AttributeValue> startKey);
}
