package service;

import com.group_1.sharedAws.model.UserInfo;
import com.group_1.sharedAws.repository.UserRepository;
import exception.NoSuchElementFoundException;
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
        UserInfo userInfo = repository.getRecordById(userId);
        return userInfo;
    }
}
