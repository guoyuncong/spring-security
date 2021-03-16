package com.yc.security.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class User extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 是否是管理员（管理员拥有最高权限）
     */
    private boolean admin;

}
