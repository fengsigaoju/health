package com.fengsigaoju.health.friend.service.impl;

import com.fengsigaoju.health.friend.dao.FriendDao;
import com.fengsigaoju.health.friend.domain.Enum.FriendStatusEnum;
import com.fengsigaoju.health.friend.domain.dataobject.FriendDO;
import com.fengsigaoju.health.friend.domain.dto.FriendDTO;
import com.fengsigaoju.health.friend.domain.dto.RankDTO;
import com.fengsigaoju.health.friend.domain.dto.UserDTO;
import com.fengsigaoju.health.friend.domain.result.SimpleResult;
import com.fengsigaoju.health.friend.service.FriendService;
import com.fengsigaoju.health.friend.util.DateUtils;
import com.fengsigaoju.health.friend.util.LoggerUtil;
import com.fengsigaoju.health.friend.util.RestUtil;
import com.fengsigaoju.health.friend.util.ValidateUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author yutong song
 * @date 2018/4/26
 */
@Service
public class FriendServiceImpl implements FriendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendServiceImpl.class);

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<FriendDTO> listByResponseIdAndStatus(long responseId, String friendStatus) {
        LoggerUtil.info(LOGGER, "enter in FriendServiceImpl[listByResponseIdAndStatus],responseId:{0},friendStatus:{1}", responseId, friendStatus);
        return convertDOstoDTOs(friendDao.listByResponseIdAndStatus(buildMapParams(responseId, friendStatus,"responseId")));
    }

    private Map<String, String> buildMapParams(long id, String friendStatus,String typeId) {
        Map<String, String> map = Maps.newHashMap();
        map.put(typeId, String.valueOf(id));
        map.put("friendStatus", friendStatus);
        return map;
    }

    @Override
    public List<FriendDTO> listByRequestIdAndStatus(long requestId, String friendStatus) {
        LoggerUtil.info(LOGGER, "enter in FriendServiceImpl[listByRequestIdAndStatus],requestId:{0},friendStatus:{1}", requestId, friendStatus);
        return convertDOstoDTOs(friendDao.listByRequestIdAndStatus(buildMapParams(requestId, friendStatus,"requestId")));
    }

    @Override
    public boolean buildFriend(long requestId, long responseId,String remark) {
        LoggerUtil.info(LOGGER, "enter in FriendServiceImpl[addFriend],requestId:{0},reponseId:{1}", requestId, responseId);
        ValidateUtils.checkTrue(requestId != responseId, "不能添加自己为好友");
        //这里注意类型一定要和http处理方那边一致
        SimpleResult<UserDTO> requestResult = restTemplate.getForObject("http://USER/id/{userId}", SimpleResult.class,requestId);
        SimpleResult<UserDTO> responseResult= restTemplate.getForObject("http://USER/id/{userId}", SimpleResult.class,responseId);
        ValidateUtils.checkNotNull(requestResult.getResult(),"系统异常,请稍后重试");
        ValidateUtils.checkNotNull(responseResult.getResult(),"待请求添加好友不存在");
        List<FriendDO> agreeList = friendDao.listByRequestIdAndStatus(buildMapParams(requestId, FriendStatusEnum.AGREE.name(),"requestId"));
        agreeList.addAll(friendDao.listByResponseIdAndStatus(buildMapParams(requestId, FriendStatusEnum.AGREE.name(),"responseId")));
        List<FriendDO> solvingList = friendDao.listByRequestIdAndStatus(buildMapParams(requestId, FriendStatusEnum.SOLVING.name(),"requestId"));
        solvingList.addAll(friendDao.listByResponseIdAndStatus(buildMapParams(requestId, FriendStatusEnum.SOLVING.name(),"responseId")));
        if (!CollectionUtils.isEmpty(agreeList)) {
            Iterator<FriendDO> iterator = agreeList.iterator();
            while (iterator.hasNext()) {
                FriendDO friendDO = iterator.next();
                //requestId列表里已经有这个responseId
                ValidateUtils.checkTrue(friendDO.getResponseId() != responseId, "该用户已经是您的好友");
                ValidateUtils.checkTrue(friendDO.getRequestId() != responseId, "该用户已经是您的好友");
            }
        }
        if (!CollectionUtils.isEmpty(solvingList)) {
            Iterator<FriendDO> iterator = solvingList.iterator();
            while (iterator.hasNext()) {
                FriendDO friendDO = iterator.next();
                //requestId列表里已经有这个responseId
                ValidateUtils.checkTrue(friendDO.getResponseId() != responseId, "该请求已在请求列表里,请等待对方回应");
                ValidateUtils.checkTrue(friendDO.getRequestId() != responseId, "该请求已在请求列表里,请等待对方回应");
            }
        }

        FriendDO friendDO = new FriendDO();
        friendDO.setRemark(remark);
        friendDO.setRequestId(requestId);
        friendDO.setResponseId(responseId);
        friendDO.setFriendStatus(FriendStatusEnum.SOLVING.name());
        return friendDao.addFriend(friendDO) > 0;
    }


    @Override
    public boolean agreeFriend(long friendId) {
        LoggerUtil.info(LOGGER, "enter in FriendServiceImpl[agreeFriend],friendId:{0}", friendId);
        FriendDO friendDO = friendDao.queryByFriendId(friendId);
        ValidateUtils.checkTrue(friendDO != null, "好友关系不存在");
        ValidateUtils.checkTrue(FriendStatusEnum.SOLVING.name().equals(friendDO.getFriendStatus()), "状态不是正在处理中,无法添加好友");
        friendDO.setFriendStatus(FriendStatusEnum.AGREE.name());
        return friendDao.updateByFriendId(friendDO) > 0;
    }

    /**
     * DO集合对象转DTO集合对象
     *
     * @param friendDOS
     * @return
     */
    private List<FriendDTO> convertDOstoDTOs(List<FriendDO> friendDOS) {
        List<FriendDTO> friendDTOList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(friendDOS)) {
            for (FriendDO friendDO : friendDOS) {
                friendDTOList.add(convertDOtoDTO(friendDO));
            }
        }
        return friendDTOList;
    }

    /**
     * DO对象转DTO对象
     *
     * @param friendDO
     * @return
     */
    private FriendDTO convertDOtoDTO(FriendDO friendDO) {
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setFriendId(friendDO.getFriendId());
        friendDTO.setFriendStatus(friendDO.getFriendStatus());
        friendDTO.setRequestUser(RestUtil.convertToUserDTO((Map<String,Object>)restTemplate.getForObject("http://USER/id/{0}", SimpleResult.class, friendDO.getRequestId()).getResult()));
        friendDTO.setResponseUser(RestUtil.convertToUserDTO((Map<String,Object>)restTemplate.getForObject("http://USER/id/{0}", SimpleResult.class, friendDO.getResponseId()).getResult()));
        friendDTO.setGmtCreate(DateUtils.format(friendDO.getGmtCreate()));
        friendDTO.setGmtModified(DateUtils.format(friendDO.getGmtModified()));
        friendDTO.setRemark(friendDO.getRemark());
        return friendDTO;
    }

    /**
     * 获取步数信息
     * @param userId
     * @return
     */
    @Override
    public List<RankDTO> queryRank(long userId) {
        LoggerUtil.info(LOGGER,"enter in StepServiceImpl[queryRank],userId:{0}",userId);
        //1,首先获取该用户关联的好友列表
        //2,获取每一个好友的今日步数
        //3,组装返回DTO
        List<FriendDTO> responseList=this.listByResponseIdAndStatus(userId, FriendStatusEnum.AGREE.name());
        List<FriendDTO> requestList =this.listByRequestIdAndStatus(userId, FriendStatusEnum.AGREE.name());
        List<RankDTO> rankDTOList=Lists.newArrayList();
        for (FriendDTO friendDTO:responseList){
            RankDTO rankDTO=new RankDTO();
            rankDTO.setUserId(friendDTO.getRequestUser().getUserId());
            SimpleResult<String> requestResult = restTemplate.getForObject("http://STEP/day/{userId}", SimpleResult.class,friendDTO.getRequestUser().getUserId());
            rankDTO.setStepNumber(Long.valueOf(requestResult.getResult()));
            rankDTO.setUsername(friendDTO.getRequestUser().getUsername());
            rankDTOList.add(rankDTO);
        }
        for (FriendDTO friendDTO:requestList){
            RankDTO rankDTO=new RankDTO();
            rankDTO.setUserId(friendDTO.getResponseUser().getUserId());
            rankDTO.setUsername(friendDTO.getResponseUser().getUsername());
            SimpleResult<String> requestResult = restTemplate.getForObject("http://STEP/day/{userId}", SimpleResult.class,friendDTO.getResponseUser().getUserId());
            rankDTO.setStepNumber(Long.valueOf(requestResult.getResult()));
            rankDTOList.add(rankDTO);
        }

        Ordering<RankDTO> ordering=Ordering.natural().onResultOf(new Function<RankDTO, Comparable>() {
            public Comparable apply(RankDTO input) {
                return input.getStepNumber();
            }
        }).reverse();
        Collections.sort(rankDTOList,ordering);
        return rankDTOList;
    }

    @Override
    public boolean disAgreeFriend(long friendId) {
        LoggerUtil.info(LOGGER,"enter in FriendServiceImpl[disAgreeFriend],friendId:{0}",friendId);
        return friendDao.deleteByFriendId(friendId)>0;
    }
}
