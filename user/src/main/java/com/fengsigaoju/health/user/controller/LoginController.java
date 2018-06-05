package com.fengsigaoju.health.user.controller;

import com.fengsigaoju.health.user.domain.exception.BusinessException;
import com.fengsigaoju.health.user.domain.result.SimpleResult;
import com.fengsigaoju.health.user.service.IdGenerateService;
import com.fengsigaoju.health.user.service.LoginService;
import com.fengsigaoju.health.user.service.UserService;
import com.fengsigaoju.health.user.util.ExceptionHandler;
import com.fengsigaoju.health.user.util.LoggerUtil;
import com.fengsigaoju.health.user.util.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yutong song
 * @date 2018/4/23
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    /**
     * 根据用户名和密码判断该用户是否存在(这边将token转化为String)
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "/check")
    public SimpleResult<String> login(String username, String password) {
        LoggerUtil.info(LOGGER, "enter in LoginController[login]username:{0},password:{1}", username, password);
        SimpleResult<String> result = new SimpleResult<>();
        try {
            long id = loginService.loginAndReturnSessionId(username, password);
            ValidateUtils.checkTrue(id>0,"用户名和密码不匹配");
            result.setSuccess(true);
            result.setResult(String.valueOf(id));
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "登录失败,username:{0},password:{1}", username, password);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "登录失败,username:{0}.password:{1}", username, password);
        }
        return result;
    }
}
