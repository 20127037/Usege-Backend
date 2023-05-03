package com.group_1.master.service;

import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.DynamoDbRepository;
import com.group_1.sharedDynamoDB.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * service
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 10:26 PM
 * Description: ...
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    @Override
    public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = repository.getRecordByKey(DynamoDbRepository.getKey(userId));
        if (userInfo == null)
            throw new NoSuchElementFoundException(userId);
        return userInfo;
    }
}
