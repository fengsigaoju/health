package com.fengsigaoju.health.friend.domain.Enum;

/**
 * 好友状态枚举
 * SOLVING->AGREE(REFUSE)
 * @author yutong song
 * @date 2018/4/26
 */
public enum FriendStatusEnum {

    SOLVING("正在处理中"),

    AGREE("同意"),

    REFUSE("拒绝"),

    UNDO("撤销");

    private String description;

    FriendStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
