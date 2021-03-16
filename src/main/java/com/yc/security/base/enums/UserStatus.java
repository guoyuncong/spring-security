package com.yc.security.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gyc
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserStatus {

    /**
     * 用户状态
     * NORMAL   正常
     * DISABLE  禁用
     */
    NORMAL(1, "正常"),
    DISABLE(2, "禁用"),
    INVALIDED(3,"作废"),

    ;

    private Integer status;

    private String desc;
}
