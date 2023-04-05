package com.group_1.master.controller;

import com.group_1.master.service.UserService;
import com.group_1.sharedAws.model.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 10:14 PM
 * Description: ...
 */

@RestController
@AllArgsConstructor
public class MasterController {

    private final UserService userService;
    //Multipart file upload
    //Update data in Dynamo
    //Upload file to S3
    @GetMapping("user/{id}")
    public UserInfo getUserInfo(@PathVariable String id)
    {
        return userService.getUserInfo(id);
    }
}