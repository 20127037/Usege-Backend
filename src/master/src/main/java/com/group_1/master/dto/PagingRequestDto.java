package com.group_1.master.dto;

import java.util.Map;

/**
 * com.group_1.master.dto
 * Created by NhatLinh - 19127652
 * Date 4/21/2023 - 4:19 PM
 * Description: ...
 */

public record PagingRequestDto(int limit, String[] attributes, Map<String, String> lastKey) {
}
