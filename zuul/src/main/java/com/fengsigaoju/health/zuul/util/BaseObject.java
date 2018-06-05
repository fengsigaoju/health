package com.fengsigaoju.health.zuul.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author yutong song
 * @date 2018/3/29
 */
public class BaseObject implements Serializable {


    private static final long serialVersionUID = 39400076738350421L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
