package com.fengsigaoju.health.user.service;

import com.fengsigaoju.health.user.domain.dto.UserDTO;

import java.io.InputStream;

/**
 * @author yutong song
 * @date 2018/4/23
 */
public interface UserService {

    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    UserDTO queryByUserId(long userId);

    /**
     * 添加用户信息
     * @param username
     * @param password
     * @return
     */
    boolean addUser(String username,String password);

    /**
     * 更新手机电话
     * @param phoneId
     * @param userId
     * @return
     */
    boolean updatePhoneByUserId(long phoneId,long userId);

    /**
     * 根据用户名查询用户
     * service层应该只能查出一个
     *
     * @param username
     * @return
     */
    UserDTO queryByUserName(String username);

    /**
     * 判断用户是否存在
     *
     * @param username
     * @param password
     * @return
     */
    long hasMatchUser(String username, String password);


    /**
     * 上传图片
     * @param pictureName
     * @param inputStream
     * @param userId
     * @return
     * @throws Exception
     */
    String uploadPicture(String pictureName, InputStream inputStream,long userId) throws Exception;

}
