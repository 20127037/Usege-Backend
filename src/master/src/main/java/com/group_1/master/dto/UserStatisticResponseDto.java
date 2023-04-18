package com.group_1.master.dto;

import lombok.Builder;
import lombok.Data;

/**
 * com.group_1.master.dto
 * Created by NhatLinh - 19127652
 * Date 4/13/2023 - 11:15 PM
 * Description: ...
 */
@Data
@Builder
public class UserStatisticResponseDto {
    private final long usedSpaceInKb;
    private final long maxSpaceInKb;
    private final long countImg;
    private final long countAlbum;
}
