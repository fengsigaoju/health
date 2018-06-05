package com.fengsigaoju.health.friend.domain.dto;

/**
 * 好友列表
 * @author yutong song
 * @date 2018/4/26
 */
public class FriendDTO {


    private long friendId;

    /**
     * 请求发起方
     */
    private UserDTO  requestUser;

    /**
     * 请求相应方
     */
    private UserDTO responseUser;

    /**
     * 好友状态
     * @see com.fengsigaoju.health.friend.domain.Enum.FriendStatusEnum
     */
    private String friendStatus;


    private String gmtCreate;


    private String gmtModified;

    /**
     * 备注
     */
    private String remark;

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public UserDTO getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(UserDTO requestUser) {
        this.requestUser = requestUser;
    }

    public UserDTO getResponseUser() {
        return responseUser;
    }

    public void setResponseUser(UserDTO responseUser) {
        this.responseUser = responseUser;
    }

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
