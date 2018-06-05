package com.fengsigaoju.health.friend.domain.result;

import java.util.List;

/**
 * 好友请求领域模型
 * @author yutong song
 * @date 2018/4/6
 */
public class FriendResult<T> extends  BaseResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 返回信息
     */
    private String message;

    /**
     * request的返回
     */
    private List<T> requestFriend;

    /**
     * response的返回
     */
    private List<T> responseFriend;

    public FriendResult() {
    }

    public FriendResult(boolean success, String message, List<T> requestFriend, List<T> responseFriend) {
        super(success, message);
        this.requestFriend = requestFriend;
        this.responseFriend = responseFriend;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setRequestFriend(List<T> requestFriend) {
        this.requestFriend = requestFriend;
    }

    public void setResponseFriend(List<T> responseFriend) {
        this.responseFriend = responseFriend;
    }

    public List<T> getRequestFriend() {
        return requestFriend;
    }

    public List<T> getResponseFriend() {
        return responseFriend;
    }
}
