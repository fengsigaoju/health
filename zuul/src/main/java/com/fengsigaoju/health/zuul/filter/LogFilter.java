package com.fengsigaoju.health.zuul.filter;

import com.fengsigaoju.health.zuul.util.LoggerUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志过滤器
 * @author yutong song
 * @date 2018/4/24
 */
@Component
public class LogFilter  extends ZuulFilter{

    private static final Logger LOGGER= LoggerFactory.getLogger(LogFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        Map<String, String[]> objectMap = request.getParameterMap();
        Map<String, String> returnMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry : objectMap.entrySet()) {
            String key = entry.getKey();
            String value = "";
            if (key == null) {
                key = "";
            }
            if (entry.getValue().length > 0) {
                value = entry.getValue()[0];
            }
            for (int i = 1; i < entry.getValue().length; i++) {
                value = "," + entry.getValue()[i];
            }
            returnMap.put(key, value);
        }
        LoggerUtil.info(LOGGER,"url :{0},params :{1}",request.getRequestURL(),returnMap);
        return null;
    }
}
