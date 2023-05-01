package com.group_1.uploadFile.dto;

import lombok.Data;

/**
 * com.group_1.uploadFile.dto
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 12:19 AM
 * Description: ...
 */
public record UserFileRefUploadDto(String fileName, String tinyUri, String uri, String description) {
}
