package com.fengsigaoju.health.user.service;

/**
 * @author yutong song
 * @date 2018/4/25
 */
public interface LoginService {

    /**
     * 根据用户名和密码判断用户是否登录并且返回全局唯一sessionId
     * @param username
     * @param password
     * @return
     */
    Long loginAndReturnSessionId(String username,String password);
}
