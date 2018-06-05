package com.fengsigaoju.health.user.util;

import com.aliyun.oss.OSSClient;
import com.fengsigaoju.health.user.domain.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;


/**
 * oss上传工具
 *
 * @author yutong song
 * @date 2018/4/7
 */
@Component
public class OssClient {


    private static final Logger LOGGER = LoggerFactory.getLogger(OssClient.class);

    /**
     * 最大上传大小
     */
    private static final int MAX_FILE_UPLOAD_SIZE = 16 * 1024 * 104;

    private static final String FILE_UPLOAD_TIME = "upload_time";

    @Autowired
    private  OSSClient ossClient;

    private  final String bucketName="video-spring";

    /**
     * 上传oss文件
     *
     * @param ossKey      文件名称
     * @param inputStream 输入流
     * @return
     */
    public boolean upload(String ossKey, InputStream inputStream) throws BusinessException, IOException {
        ValidateUtils.checkTrue(StringUtils.isNotEmpty(ossKey), "待上传的文件名为空");
        ValidateUtils.checkNotNull(inputStream, "待上传的文件为空");
        ValidateUtils.checkTrue(inputStream.available() < MAX_FILE_UPLOAD_SIZE, "待上传的文件超过16M");
        LoggerUtil.info(LOGGER, "START UPLOAD[ossKey:{0}]", ossKey);
        try {
            ossClient.putObject(bucketName,ossKey, inputStream);
            LoggerUtil.info(LOGGER, "上传文件成功,ossKey:{0}", ossKey);
            return true;
        } catch (BusinessException ex) {
            LoggerUtil.error(ex, LOGGER, "上传文件失败,ossKey:{0}", ossKey);
            return false;
        }
    }

    /**
     * 获得OSS生成的图片的URL
     *
     * @param ossKey
     * @return
     */
    public String getURL(String ossKey) {
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, ossKey, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

}
