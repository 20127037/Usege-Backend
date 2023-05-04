package com.group_1.master.service;

import com.group_1.master.dto.QueryFilesInAlbumResponse;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 2:08 PM
 * Description: ...
 */
public interface TrashBinService {
    QueryResponse<UserFile> queryImages(String userId, int limit, Map<String, AttributeValue> startKey);
    UserFile getImage(String userId, String fileName);
}