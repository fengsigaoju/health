package com.fengsigaoju.health.friend.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 基础领域模型
 * @author yutong song
 * @date 2018/4/23
 */
public class BaseObject implements Serializable {

    private static final long serialVersionUID = 39400076738350421L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
