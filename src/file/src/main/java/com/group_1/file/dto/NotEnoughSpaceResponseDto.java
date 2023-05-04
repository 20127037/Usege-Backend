package com.group_1.file.dto;

/**
 * com.group_1.uploadFile.dto
 * Created by NhatLinh - 19127652
 * Date 5/1/2023 - 11:52 PM
 * Description: ...
 */
public record NotEnoughSpaceResponseDto(Long maxSpaceInKb, Long usedSpaceInKb, Long needSpaceInKb){
}
