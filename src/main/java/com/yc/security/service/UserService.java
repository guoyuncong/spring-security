package com.yc.security.service;


import com.yc.security.model.entity.User;

public interface UserService {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return bean
     */
    User getUserByUserName(String username);


}
