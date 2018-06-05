package com.fengsigaoju.health.user.service.impl;

import com.fengsigaoju.health.user.domain.exception.BusinessException;
import com.fengsigaoju.health.user.service.IdGenerateService;
import com.fengsigaoju.health.user.service.LoginService;
import com.fengsigaoju.health.user.service.UserService;
import com.fengsigaoju.health.user.util.LoggerUtil;
import com.fengsigaoju.health.user.util.Md5Util;
import com.fengsigaoju.health.user.util.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author yutong song
 * @date 2018/4/25
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private IdGenerateService idGenerateService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 1,验证用户名和密码匹配
     * 2,生成全局唯一性sessionId
     * 3,将全局唯一性sessionId放到redis中存储
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Long loginAndReturnSessionId(String username, String password) {
        LoggerUtil.info(LOGGER, "enter in LoginServiceImpl[loginAndReturnSessionId,username:{0},password:{1}", username, password);
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            @Override
            public Long doInTransaction(TransactionStatus transactionStatus) {
                try {
                    long userId = userService.hasMatchUser(username, Md5Util.md5Encode(password));
                    ValidateUtils.checkTrue(userId > 0, "用户名密码不正确");
                    long token = idGenerateService.nextId();
                    if (token > 0) {
                        redisTemplate.opsForValue().set(String.valueOf(token), String.valueOf(userId), 30, TimeUnit.MINUTES);
                    }
                    return token;
                } catch (Exception ex) {
                    LoggerUtil.error(ex, LOGGER, "登录校验失败,username:{0},password:{1}", username, password);
                    transactionStatus.setRollbackOnly();
                }
                return (long) -1;
            }
        });
    }
}
