package com.fengsigaoju.health.user.controller;

import com.fengsigaoju.health.user.domain.exception.BusinessException;
import com.fengsigaoju.health.user.domain.result.BaseResult;
import com.fengsigaoju.health.user.service.UserService;
import com.fengsigaoju.health.user.util.ExceptionHandler;
import com.fengsigaoju.health.user.util.LoggerUtil;
import com.fengsigaoju.health.user.util.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yutong song
 * @date 2018/4/23
 */
@RestController
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public BaseResult register(String username, String password) {
        LoggerUtil.info(LOGGER, "enter in RegisterController[register],username:{0},password:{1}", username, password);
        BaseResult result = new BaseResult();
        try {
            result.setSuccess(userService.addUser(username, Md5Util.md5Encode(password)));
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "注册失败,username:{0},password:{1}", username, password);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "注册失败,username:{0},password:{1}", username, password);
        }
        return result;
    }
}