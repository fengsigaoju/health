package com.fengsigaoju.health.user.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 生成ossClient
 *
 * @author yutong song
 * @date 2018/5/15
 */
@Configuration
public class OssClientConfiguration {

    @Value("${OssClient.endPoint}")
    private String endPoint;

    @Value("${OssClient.accessKeyId}")
    private String accessKeyId;

    @Value("${OssClient.accessKeySecret}")
    private String accessKeySecret;

    @Bean
    public OSSClient getOssClient() {
        return new OSSClient(endPoint, accessKeyId, accessKeySecret);
    }
}