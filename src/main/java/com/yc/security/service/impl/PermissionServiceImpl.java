package com.yc.security.service.impl;

import com.yc.security.base.constants.Constants;
import com.yc.security.model.entity.User;
import com.yc.security.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    @Override
    public List<String> getUserPermissions(User user) {
        List<String> permissions = new ArrayList<>();
        if (user.isAdmin()) {
            permissions.add(Constants.PERMISSION_ALL);
        } else {
//            permissions.addAll(permissionMapper.getPermissionMarkByUserId(user.getId()));
        }
        return permissions;
    }
}
