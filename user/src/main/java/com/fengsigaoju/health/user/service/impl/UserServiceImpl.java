package com.fengsigaoju.health.user.service.impl;

import com.fengsigaoju.health.user.dao.UserDao;
import com.fengsigaoju.health.user.domain.UserStatusEnum;
import com.fengsigaoju.health.user.domain.dataobject.UserDO;
import com.fengsigaoju.health.user.domain.dto.UserDTO;
import com.fengsigaoju.health.user.domain.exception.BusinessException;
import com.fengsigaoju.health.user.service.UserService;
import com.fengsigaoju.health.user.util.DateUtils;
import com.fengsigaoju.health.user.util.LoggerUtil;
import com.fengsigaoju.health.user.util.OssClient;
import com.fengsigaoju.health.user.util.ValidateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yutong song
 * @date 2018/4/23
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private OssClient ossClient;

    private static int width = 60;

    private static int height = 60;

    @Override
    public UserDTO queryByUserId(long userId) {
        LoggerUtil.info(LOGGER, "enter in [UserServiceImpl]queryByUserId,userId:{0}", userId);
        return convertDOtoDTO(userDao.queryByUserId(userId));

    }

    /**
     * 用户DO对象转DTO对象
     *
     * @param userDO
     * @return
     */
    private UserDTO convertDOtoDTO(UserDO userDO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userDO.getUsername());
        userDTO.setUserId(userDO.getUserId());
        userDTO.setGmtCreate(DateUtils.format(userDO.getGmtCreate()));
        userDTO.setGmtModified(DateUtils.format(userDO.getGmtModified()));
        userDTO.setLinkPhone(userDO.getLinkPhone());
        userDTO.setPictureUrl(userDO.getPictureUrl());
        userDTO.setUserStatus(userDO.getUserStatus());
        return userDTO;
    }

    @Override
    public boolean addUser(String username, String password) {
        LoggerUtil.info(LOGGER, "enter in [UserServiceImpl]addUser,username:{0},password:{1}", username, password);
        List<UserDO> userDOList = userDao.listByUserName(username);
        ValidateUtils.checkTrue(CollectionUtils.isEmpty(userDOList),"该用户名已存在");
        UserDO userDO = new UserDO();
        userDO.setUsername(username);
        userDO.setPassword(password);
        userDO.setUserStatus(UserStatusEnum.IN_SERVICE.name());
        return userDao.addUser(userDO) > 0;

    }

    @Override
    public boolean updatePhoneByUserId(long phoneId, long userId) {
        LoggerUtil.info(LOGGER, "enter in [UserServiceImpl]updatePhoneByUserId,phoneId:{0},userId:{1}", phoneId, userId);
        UserDO userDO = userDao.queryByUserId(userId);
        userDO.setLinkPhone(phoneId);
        return userDao.updateByUserId(userDO);
    }


    @Override
    public UserDTO queryByUserName(String username) {
        LoggerUtil.info(LOGGER, "enter in [UserServiceImpl]listByUserName,username:{0}", username);
        List<UserDO> userDOList = userDao.listByUserName(username);
        ValidateUtils.checkTrue(userDOList.size() == 1, "用户名重复或不存在,username:{0}", username);
        return convertDOtoDTO(userDOList.get(0));
    }

    @Override
    public long hasMatchUser(String username, String password) {
        LoggerUtil.info(LOGGER, "enter in [UserServiceImpl]hasMatchUser.username:{0},password:{1}", username, password);
        Map<String,String>map=new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        List<UserDO> userDOList = userDao.listByUserNameAndPassword(map);
        return userDOList.get(0).getUserId();
    }

    @Override
    public String uploadPicture(String pictureName, InputStream inputStream, long userId) throws Exception {
        LoggerUtil.info(LOGGER, "enter in uploadPicture,pictureName:{0},userId:{1}", pictureName,userId);
        return transactionTemplate.execute(new TransactionCallback<String>() {
            public String doInTransaction(TransactionStatus status) {
                try {
                    //1,先调整大小为60*60.
                    String format = achiveFormat(pictureName);
                    Image srcImg = ImageIO.read(inputStream);
                    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    result.getGraphics().drawImage(srcImg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    ImageIO.write(result, format, bs);
                    InputStream uploadInputStream = new ByteArrayInputStream(bs.toByteArray());
                    //2,再上传文件
                    ossClient.upload(pictureName, uploadInputStream);
                    //3,修改数据库里面的pictureUrl
                    UserDO userDO = userDao.queryByUserId(userId);
                    ValidateUtils.checkNotNull(userDO,"该用户不存在");
                    userDO.setPictureUrl("https://video-spring.oss-cn-beijing.aliyuncs.com/"+pictureName);
                    ValidateUtils.checkTrue(userDao.updateByUserId(userDO),"更新失败");
                    return "https://video-spring.oss-cn-beijing.aliyuncs.com/"+pictureName;
                }catch (IOException ioe){
                    LoggerUtil.error(ioe,LOGGER,"读取文件失败");
                }
                status.setRollbackOnly();
                return null;
            }
        });
    }

    /**
     * 获取格式(png,jpeg)
     *
     * @param pictureName
     * @return
     */
    private String achiveFormat(String pictureName) {
        String[] temp = pictureName.split("\\.");
        if (!StringUtils.equals(temp[temp.length - 1], "png") && (StringUtils.equals(temp[temp.length - 1], "jpg")) && (StringUtils.equals(temp[temp.length - 1], "jpeg"))) {
            throw new BusinessException("格式不符合规范");
        }
        return temp[temp.length - 1];
    }
}
