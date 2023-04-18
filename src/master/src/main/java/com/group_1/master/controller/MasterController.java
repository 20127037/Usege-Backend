package com.group_1.master.controller;

import com.group_1.master.dto.StoragePlanResponseDto;
import com.group_1.master.dto.UserStatisticResponseDto;
import com.group_1.master.service.StoragePlanService;
import com.group_1.master.service.UserService;
import com.group_1.sharedDynamoDB.model.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 10:14 PM
 * Description: ...
 */

@RestController("master")
@AllArgsConstructor
public class MasterController {

    private final UserService userService;
    private final StoragePlanService storagePlanService;
    //Multipart file upload
    //Update data in Dynamo
    //Upload file to S3
    @GetMapping("user/{id}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable String id)
    {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @GetMapping("user/statistic/{id}")
    public ResponseEntity<UserStatisticResponseDto> getUserStatistic(@PathVariable String id)
    {
        UserInfo userInfo = userService.getUserInfo(id);
        return ResponseEntity.ok(UserStatisticResponseDto.builder()
                        .maxSpaceInKb(userInfo.getMaxSpace())
                        .usedSpaceInKb(userInfo.getUsedSpace())
                        .countAlbum(userInfo.getAlbumCount())
                        .countImg(userInfo.getImgCount())
                .build());
    }

    @GetMapping("user/plan/{id}")
    public ResponseEntity<List<StoragePlanResponseDto>> getUserPlan(@PathVariable String id)
    {
        return ResponseEntity.ok(storagePlanService.getAllPackages(id));
    }

    @GetMapping("test")
    public ResponseEntity testAuth()
    {
        return ResponseEntity.ok().build();
    }
}