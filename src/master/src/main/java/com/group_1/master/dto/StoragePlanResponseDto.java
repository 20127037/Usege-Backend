package com.group_1.master.dto;

import com.group_1.sharedDynamoDB.model.StoragePlanAbility;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

/**
 * com.group_1.master.dto
 * Created by NhatLinh - 19127652
 * Date 4/13/2023 - 9:55 PM
 * Description: ...
 */
@Data
@Builder
public class StoragePlanResponseDto {
    private String name;
    private float price;
    private boolean canPurchased;
    private int order;
    private Collection<StoragePlanAbility> abilities;
}

