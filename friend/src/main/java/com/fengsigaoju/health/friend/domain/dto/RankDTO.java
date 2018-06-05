package com.fengsigaoju.health.friend.domain.dto;


import com.fengsigaoju.health.friend.domain.BaseObject;

/**
 * 排行榜对象
 * @author yutong song
 * @date 2018/4/11
 */
public class RankDTO extends BaseObject {

    private long userId;

    private long stepNumber;

    private String username;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(long stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
