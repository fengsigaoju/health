package com.fengsigaoju.health.step.domain.exception;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 自定义异常
 * 在service和dao层抛出,在controller层被捕获
 * 用来返回例如名称重复,权限不够等商业异常,并被controller拦截塞到返回对象中
 * @author yutong song
 * @date 2018/3/29
 */
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 3243377604765079321L;

    public BusinessException() {

    }

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
