package com.group_1.auth.dto;

import lombok.Builder;
import lombok.Data;

/**
 * com.group_1.authenticationService.dto
 * Created by NhatLinh - 19127652
 * Date 3/31/2023 - 4:53 PM
 * Description: ...
 */
@Data
@Builder
public class ResponseTokenDto {
    private final String userId;
    private final String accessToken;
    private final Integer expiresIn;
    private final String refreshToken;
    private final String idToken;
}
