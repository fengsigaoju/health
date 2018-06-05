package com.fengsigaoju.health.friend.service;

import com.fengsigaoju.health.friend.domain.dto.FriendDTO;
import com.fengsigaoju.health.friend.domain.dto.RankDTO;

import java.util.List;

/**
 * @author yutong song
 * @date 2018/4/26
 */
public interface FriendService {

    List<FriendDTO> listByResponseIdAndStatus(long responseId,String friendStatus);

    List<FriendDTO> listByRequestIdAndStatus(long requestId,String friendStatus);

    boolean buildFriend(long requestId,long reponseId,String remark);

    boolean agreeFriend(long friendId);

    List<RankDTO> queryRank(long userId);

    boolean disAgreeFriend(long friendId);

}
