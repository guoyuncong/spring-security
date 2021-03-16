package com.yc.security.service;

import com.yc.security.model.entity.User;

import java.util.List;

public interface PermissionService {

    /**
     * 获取用户权限
     *
     * @param user bean
     * @return 权限标识集合
     */
    List<String> getUserPermissions(User user);
}
