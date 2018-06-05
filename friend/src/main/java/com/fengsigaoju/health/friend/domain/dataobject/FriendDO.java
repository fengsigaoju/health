package com.fengsigaoju.health.friend.domain.dataobject;

import com.fengsigaoju.health.friend.domain.BaseObject;

import java.util.Date;

/**
 * @author yutong song
 * @date 2018/4/26
 */
public class FriendDO extends BaseObject {

    private long friendId;

    /**
     * 请求发起方
     */
    private long requestId;

    /**
     * 请求回应方
     */
    private long responseId;

    /**
     * @see com.fengsigaoju.health.friend.domain.Enum.FriendStatusEnum
     */
    private String friendStatus;

    private Date gmtCreate;

    private Date gmtModified;

    private String remark;

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
