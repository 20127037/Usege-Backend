package com.group_1.file.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * com.group_1.uploadFile.exception
 * Created by NhatLinh - 19127652
 * Date 5/1/2023 - 11:45 PM
 * Description: ...
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class ExceedSpaceException extends RuntimeException {
    private Long maxSpace, usedSpace, needSpace;
}
