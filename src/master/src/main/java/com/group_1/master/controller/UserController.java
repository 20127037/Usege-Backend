package com.group_1.master.controller;

import com.group_1.master.dto.StoragePlanResponseDto;
import com.group_1.master.dto.UserStatisticResponseDto;
import com.group_1.master.service.FileService;
import com.group_1.master.service.FileServiceImpl;
import com.group_1.master.service.StoragePlanService;
import com.group_1.master.service.UserService;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 10:14 PM
 * Description: ...
 */

@RestController
@RequestMapping("user")
@AllArgsConstructor
@Tag(name = "Get user information", description = "Get user information")
public class UserController {

    private final UserService userService;
    private final StoragePlanService storagePlanService;
    //Multipart file upload
    //Update data in Dynamo
    //Upload file to S3
    @GetMapping("{id}")
    @Operation(summary = "Get user information by its id")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable String id)
    {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @GetMapping("statistic/{id}")
    @Operation(summary = "Get user compact statistic information by its id")
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

    @GetMapping("plan/{id}")
    @Operation(summary = "Get list of available plans for the user by user id")
    public ResponseEntity<List<StoragePlanResponseDto>> getUserPlan(@PathVariable String id)
    {
        return ResponseEntity.ok(storagePlanService.getAllPackages(id));
    }
}