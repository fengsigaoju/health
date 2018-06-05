package com.fengsigaoju.health.friend.domain.dto;


import com.fengsigaoju.health.friend.domain.BaseObject;

/**
 * 用户实体
 * @author yutong song
 * @date 2018/4/23
 */
public class UserDTO extends BaseObject {

    private long userId;

    private String username;

    private String password;

    private String userStatus;

    private String gmtCreate;

    private String gmtModified;

    private long linkPhone;

    private String pictureUrl;

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

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
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

    public UserDTO(long userId, String username, String password, String userStatus, String gmtCreate, String gmtModified, long linkPhone, String pictureUrl) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userStatus = userStatus;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.linkPhone = linkPhone;
        this.pictureUrl = pictureUrl;
    }

    public UserDTO() {
    }
}
