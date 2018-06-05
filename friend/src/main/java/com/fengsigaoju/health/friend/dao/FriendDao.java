package com.fengsigaoju.health.friend.dao;

import com.fengsigaoju.health.friend.domain.dataobject.FriendDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yutong song
 * @date 2018/4/26
 */
@Mapper
public interface FriendDao {


    /**
     * 根据回应id和状态来获取关系实例
     *
     * @param map
     * @return
     */
    List<FriendDO> listByResponseIdAndStatus(Map<String, String> map);

    /**
     * 根据请求id和状态来获取关系实例
     *
     * @param map
     * @return
     */
    List<FriendDO> listByRequestIdAndStatus(Map<String, String> map);


    /**
     * 添加好友实例
     *
     * @param friendDO
     * @return
     */
    long addFriend(FriendDO friendDO);

    /**
     * 更新好友状态
     *
     * @param friendDO
     * @return
     */
    long updateByFriendId(FriendDO friendDO);


    /**
     * 根据好友id查询好友信息
     *
     * @param friendId
     * @return
     */
    FriendDO queryByFriendId(long friendId);


    /**
     * 根据好友id删除好友请求
     * @param friendId
     * @return
     */
    long deleteByFriendId(long friendId);
}
