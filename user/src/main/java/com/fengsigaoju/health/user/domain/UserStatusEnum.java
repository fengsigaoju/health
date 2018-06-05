package com.fengsigaoju.health.user.domain;

/**
 * @author yutong song
 * @date 2018/4/23
 */
public enum UserStatusEnum {

    DELETED("被删除"),

    IN_SERVICE("使用中");

    private String description;

    UserStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
