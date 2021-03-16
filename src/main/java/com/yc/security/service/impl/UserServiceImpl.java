package com.yc.security.service.impl;

import com.yc.security.model.entity.User;
import com.yc.security.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public User getUserByUserName(String username) {
        return null;
    }
}
