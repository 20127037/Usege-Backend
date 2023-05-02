package com.group_1.master.dto;

import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * com.group_1.master.dto
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 3:35 PM
 * Description: ...
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class QueryFilesInAlbumResponse extends QueryResponse<UserFile> {
    private String albumName;
}
