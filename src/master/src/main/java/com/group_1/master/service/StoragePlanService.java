package com.group_1.master.service;


import com.group_1.master.dto.StoragePlanResponseDto;

import java.util.List;

/**
 * service
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 6:03 PM
 * Description: ...
 */
public interface StoragePlanService {
    List<StoragePlanResponseDto> getAllPackages(String userId);
}