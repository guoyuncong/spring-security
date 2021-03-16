package com.yc.security.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class Entity {

    /**
     * 创建时间
     */
    @JsonIgnore
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonIgnore
    private Date updateTime;

    /**
     * 版本号
     */
    @JsonIgnore
    private Integer version;

    /**
     * 由谁创建
     */
    @JsonIgnore
    private String createBy;

    /**
     * 由谁修改
     */
    @JsonIgnore
    private String updateBy;

    /**
     * 创建时间
     */
    private Long createDate;

    /**
     * 修改时间
     */
    private Long updateDate;

    public Long getCreateDate() {
        return createTime.getTime();
    }

    public Long getUpdateDate() {
        return createTime.getTime();
    }
}
