package com.group_1.master.service;

import com.group_1.master.dto.StoragePlanResponseDto;
import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.StoragePlan;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.StoragePlanRepository;
import com.group_1.sharedDynamoDB.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * service
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 6:03 PM
 * Description: ...
 */
@Service
@AllArgsConstructor
public class StoragePlanServiceImpl implements StoragePlanService {
    private final StoragePlanRepository planRepository;
    private final UserRepository userRepository;
    @Override
    public List<StoragePlanResponseDto> getAllPackages(String userId) {
        UserInfo userInfo = userRepository.getRecordById(userId);
        if (userInfo == null)
            throw new NoSuchElementFoundException(userId, "userInfo");
        List<StoragePlanResponseDto> result = new ArrayList<>();
        for (StoragePlan plan : planRepository.scanAll())
        {
            result.add(StoragePlanResponseDto.builder()
                            .name(plan.getName())
                            .price(plan.getPrice())
                            .order(plan.getOrder())
                            .canPurchased(plan.getOrder() > userInfo.getPlanOrder())
                            .abilities(plan.getAbilities()).build());
        }
        result.sort(Comparator.comparingInt(StoragePlanResponseDto::getOrder));
        return result;
    }
}
