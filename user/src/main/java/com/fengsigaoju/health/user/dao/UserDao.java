package com.fengsigaoju.health.user.dao;

import com.fengsigaoju.health.user.domain.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yutong song
 * @date 2018/4/23
 */
@Mapper
public interface UserDao {

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    UserDO queryByUserId(long userId);

    /**
     * 添加用户信息
     * @param userDO
     * @return
     */
    long addUser(UserDO userDO);

    /**
     * 更新用户信息
     * @param userDO
     * @return
     */
    boolean updateByUserId(UserDO userDO);

    /**
     * 根据用户名批量查询
     * @param username
     * @return
     */
    List<UserDO> listByUserName(String username);

     /**
     * 根据用户名和密码查询
     * @return
     */
    List<UserDO> listByUserNameAndPassword(Map<String,String>map);
}
