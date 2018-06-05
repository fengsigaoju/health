package com.fengsigaoju.health.friend.controller;

import com.fengsigaoju.health.friend.domain.Enum.FriendStatusEnum;
import com.fengsigaoju.health.friend.domain.dto.FriendDTO;
import com.fengsigaoju.health.friend.domain.dto.RankDTO;
import com.fengsigaoju.health.friend.domain.exception.BusinessException;
import com.fengsigaoju.health.friend.domain.result.BaseResult;
import com.fengsigaoju.health.friend.domain.result.FriendResult;
import com.fengsigaoju.health.friend.domain.result.ListResult;
import com.fengsigaoju.health.friend.service.FriendService;
import com.fengsigaoju.health.friend.util.ExceptionHandler;
import com.fengsigaoju.health.friend.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yutong song
 * @date 2018/4/26
 */
@RestController
public class FriendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping(value = "/response")
    public ListResult<FriendDTO> listByResponseId(long token) {
        LoggerUtil.info(LOGGER, "enter in FriendController[listByResponseId],token:{0}", token);
        ListResult<FriendDTO> result = new ListResult<>();
        try {
            long userId = Long.valueOf(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(token))));
            result.setResults(friendService.listByResponseIdAndStatus(userId, FriendStatusEnum.SOLVING.name()));
            result.setSuccess(true);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "获取用户请求失败,token:{0}", token);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "获取用户请求失败,token:{0}", token);
        }
        return result;
    }

    @GetMapping(value = "/request")
    public ListResult<FriendDTO> listByRequestId(long token) {
        LoggerUtil.info(LOGGER, "enter in FriendController[listByRequestId],token:{0}", token);
        ListResult<FriendDTO> result = new ListResult<>();
        try {
            long userId = Long.valueOf(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(token))));
            result.setResults(friendService.listByRequestIdAndStatus(userId, FriendStatusEnum.SOLVING.name()));
            result.setSuccess(true);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "获取用户响应失败,token:{0}", token);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "获取用户响应失败,token:{0}", token);
        }
        return result;
    }

    /**
     * 发起好友请求
     * 1,由token获取请求用户
     * 2,验证请求对象是否存在
     * 3,添加好友请求关系
     *
     * @param responseId
     * @param remark
     * @return
     */
    @PostMapping(value = "/{responseId}")
    public BaseResult addFriend(@PathVariable long responseId, String remark, long token) {
        LoggerUtil.info(LOGGER, "enter in FriendController[addFriend],requestId:{0},remark:{1},token:{2}", responseId, remark, token);
        BaseResult result = new BaseResult();
        try {
            long userId = Long.valueOf(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(token))));
            result.setSuccess(friendService.buildFriend(userId, responseId, remark));
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "添加好友失败,token:{0},remark:{1},responseId:{2}", token, remark, responseId);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "添加好友失败,token:{0},remark:{1},responseId:{2}", token, remark, responseId);
        }
        return result;
    }


    /**
     * 获取好友列表
     *
     * @param friendId
     * @return
     */
    @PutMapping(value = "/{friendId}")
    public BaseResult agreeFriend(@PathVariable long friendId) {
        LoggerUtil.info(LOGGER, "enter in FriendController[agreeFriend],friendId:{0}", friendId);
        BaseResult result = new BaseResult();
        try {
            result.setSuccess(friendService.agreeFriend(friendId));
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "同意");
        }
        return result;
    }

    @GetMapping(value = "/query")
    public FriendResult<FriendDTO> queryFriend(long token) {
        LoggerUtil.info(LOGGER, "enter in FriendController[queryFriend],token is:{0}", token);
        FriendResult<FriendDTO> result = new FriendResult<>();
        try {
            long userId = Long.valueOf(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(token))));
            List<FriendDTO> friendDTOListResponse = friendService.listByResponseIdAndStatus(userId, FriendStatusEnum.AGREE.name());
            List<FriendDTO> friendDTOListRequest = friendService.listByRequestIdAndStatus(userId, FriendStatusEnum.AGREE.name());
            result.setRequestFriend(friendDTOListRequest);
            result.setResponseFriend(friendDTOListResponse);
            result.setSuccess(true);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "enter in FriendController[queryFriend],token is:{0}", token);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "enter in FriendController[queryFriend],token is:{0}", token);
        }
        return result;
    }

    @GetMapping(value = "/rank")
    public ListResult<RankDTO> getRank(long token) {
        LoggerUtil.info(LOGGER, "enter in friendController[getRank],token is:{0}", token);
        ListResult<RankDTO> result = new ListResult<>();
        try {
            long userId = Long.valueOf(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(token))));
            List<RankDTO> rankDTOList = friendService.queryRank(userId);
            result.setSuccess(true);
            result.setResults(rankDTOList);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "查询排行榜失败,token:{0}", token);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "查询排行榜失败,token:{0}", token);
        }
        return result;
    }

    @DeleteMapping(value = "/{friendId}")
    public BaseResult disAgreeFriend(@PathVariable long friendId) {
        LoggerUtil.info(LOGGER, "enter in FriendController[disAgreeFriend],friendId:{0}", friendId);
        BaseResult result = new BaseResult();
        try {
            result.setSuccess(friendService.disAgreeFriend(friendId));
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "同意");
        }
        return result;
    }

}
