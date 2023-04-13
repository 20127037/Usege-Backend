package com.group_1.master.service;


import com.group_1.sharedDynamoDB.model.UserInfo;

/**
 * service
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 10:25 PM
 * Description: ...
 */
public interface UserService {
    UserInfo getUserInfo(String userId);
}
