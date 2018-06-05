package com.fengsigaoju.health.user.domain.dataobject;

import com.fengsigaoju.health.user.domain.BaseObject;

import java.util.Date;

/**
 * 用户实体
 * @author yutong song
 * @date 2018/4/23
 */
public class UserDO extends BaseObject {

    private long userId;

    private String username;

    private String password;

    private long linkPhone;

    private String pictureUrl;

    private Date gmtCreate;

    private Date gmtModified;

    private String userStatus;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(long linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
