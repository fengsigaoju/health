package com.fengsigaoju.health.user.controller;

import com.fengsigaoju.health.user.domain.dto.UserDTO;
import com.fengsigaoju.health.user.domain.exception.BusinessException;
import com.fengsigaoju.health.user.domain.result.BaseResult;
import com.fengsigaoju.health.user.domain.result.SimpleResult;
import com.fengsigaoju.health.user.service.UserService;
import com.fengsigaoju.health.user.util.ExceptionHandler;
import com.fengsigaoju.health.user.util.LoggerUtil;
import com.fengsigaoju.health.user.util.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yutong song
 * @date 2018/4/23
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * 修改手机号码
     *
     * @param phoneUrl
     * @param token
     * @return
     */
    @PutMapping(value = "/phone/{phoneUrl}")
    public BaseResult updatePhone(@PathVariable long phoneUrl, long token) {
        LoggerUtil.info(LOGGER, "enter in UserController[updatePhone],token:{0},phoneUrl:{1}", token, phoneUrl);
        BaseResult result = new BaseResult();
        try {
            //这里似乎只能先转化为String再转化为long
            long userId = Long.valueOf(String.valueOf((redisTemplate.opsForValue().get(String.valueOf(token)))));
            ValidateUtils.checkTrue(userId > 0, "该用户不存在");
            result.setSuccess(userService.updatePhoneByUserId(phoneUrl, userId));
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "修改手机号码失败,token:{0},phoneUrl:{1}", token, phoneUrl);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "修改手机号码失败,token:{0},phoneUrl:{1}", token, phoneUrl);
        }
        return result;
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    @GetMapping(value = "name/{username}")
    public SimpleResult<UserDTO> queryByUserName(@PathVariable String username) {
        LoggerUtil.info(LOGGER, "enter in UserController[queryByUserName],username:{0}", username);
        SimpleResult<UserDTO> result = new SimpleResult<>();
        try {
            UserDTO userDTO = userService.queryByUserName(username);
            userDTO.setPassword("");
            result.setResult(userDTO);
            result.setSuccess(true);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "根据用户名获取用户信息失败,username:{0}", username);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "根据用户名获取用户信息失败,username:{0}", username);
        }
        return result;
    }

    /**
     * 根据用户token查询用户信息
     *
     * @param token
     * @return
     */
    @GetMapping(value = "/token")
    public SimpleResult<UserDTO> queryByToken(long token) {
        LoggerUtil.info(LOGGER, "enter in UserController[queryByToken],token is:{0}", token);
        SimpleResult<UserDTO> result = new SimpleResult<>();
        try {
            long userId = Long.valueOf(String.valueOf((redisTemplate.opsForValue().get(String.valueOf(token)))));
            UserDTO userDTO = userService.queryByUserId(userId);
            userDTO.setPassword("");
            result.setSuccess(true);
            result.setResult(userDTO);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "根据用户token获取用户信息失败,token:{0}", token);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "根据用户token获取用户信息失败,token:{0}", token);
        }
        return result;
    }

    /**
     * 上传图片
     *
     * @param file
     * @param token
     * @return
     */
    @PutMapping(value = "/picture")
    public SimpleResult<String> uploadPicture(@RequestParam(value = "file") MultipartFile file, long token) {
        LoggerUtil.info(LOGGER, "enter in UserController[uploadPicture]token:{0}", token);
        SimpleResult<String> result = new SimpleResult<>();
        try {
            long userId = Long.valueOf(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(token))));
            result.setResult(userService.uploadPicture(file.getOriginalFilename(), file.getInputStream(), userId));
            result.setSuccess(true);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "上传图片失败,fileName:{0},token:{1}", file.getOriginalFilename(), token);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "上传图片失败,fileName:{0},token:{1}", file.getOriginalFilename(), token);
        }
        return result;
    }

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @GetMapping(value = "/id/{userId}")
    public SimpleResult<UserDTO> queryById(@PathVariable long userId) {
        LoggerUtil.info(LOGGER, "enter in UserController[queryById],userId:{0}", userId);
        SimpleResult<UserDTO> result = new SimpleResult<>();
        try {
            result.setResult(userService.queryByUserId(userId));
            result.setSuccess(true);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "根据用户id查询用户失败,userId:{0}", userId);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "根据用户id查询用户失败,userId:{0}", userId);
        }
        return result;
    }

}
