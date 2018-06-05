package com.fengsigaoju.health.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.fengsigaoju.health.zuul.util.*;
import com.google.common.collect.Lists;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yutong song
 * @date 2018/4/24
 */
@Component
public class SecurityFilter extends ZuulFilter implements InitializingBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    private static List<String> whiteList;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 无需token的白名单不走过滤逻辑,其余走token验证
     * @return
     */
    @Override
    public boolean shouldFilter() {
        BaseResult result = new BaseResult();
        RequestContext ctx = RequestContext.getCurrentContext();
       if (whiteList.contains(ctx.getRequest().getRequestURI())){
           return false;
       }
       return true;
    }

    /**
     * 1,验证参数完整性
     * 2,验证参数没有被篡改
     * 3,验证该token存在
     * @return
     */
    @Override
    public Object run() {
        BaseResult result = new BaseResult();
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        try {
            ValidateUtils.checkTrue(request.getParameter("timestamp") != null, "timestamp is empty!");
            ValidateUtils.checkTrue(request.getParameter("token") != null, "token is empty!");
            ValidateUtils.checkTrue(request.getParameter("secretKey") != null, "secretKey is empty!");
            String timestamp = (String) request.getParameter("timestamp");
            String token = (String) request.getParameter("token");
            String secretKey = (String) request.getParameter("secretKey");
            //TODO 如果用户连续登录需要把之前的删除了
            ValidateUtils.checkTrue(StringUtils.equals(secretKey, Md5Util.md5Encode(token + timestamp)), "check failed");
            ValidateUtils.checkTrue(redisTemplate.opsForValue().get(String.valueOf(token)) != null, "without sessionId!");
            //更新token的使用时间
            redisTemplate.expire(token, 30, TimeUnit.MINUTES);
            return  null;
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "校验失败");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            ctx.getResponse().setCharacterEncoding("UTF-8");
            try {
                ctx.getResponse().getWriter().write(JSON.toJSONString(result));
            } catch (IOException e) {
                 LoggerUtil.error(e,LOGGER,"返回json异常");
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        whiteList = Lists.newArrayList();
        //登录校验
        whiteList.add("/user/check");
        //注册新用户
        whiteList.add("/user/register");
        //添加新步数
        whiteList.add("/step");
    }
}
