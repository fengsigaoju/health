package com.fengsigaoju.health.step.controller;

import com.fengsigaoju.health.step.domain.exception.BusinessException;
import com.fengsigaoju.health.step.domain.result.BaseResult;
import com.fengsigaoju.health.step.domain.result.ListResult;
import com.fengsigaoju.health.step.domain.result.SimpleResult;
import com.fengsigaoju.health.step.service.StepService;
import com.fengsigaoju.health.step.util.ExceptionHandler;
import com.fengsigaoju.health.step.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yutong song
 * @date 2018/4/25
 */
@RestController
public class StepController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StepController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StepService stepService;

    @PostMapping(value = "/step")
    public BaseResult addStep(Double xAxis, Double yAxis, Double zAxis, long token) {
        LoggerUtil.info(LOGGER, "enter in StepController[addStep],xAxis:{0},yAxis:{1},zAxis:{2},token:{3}", xAxis, yAxis, zAxis, token);
        //TODO 这里都需要重构一下,当查询不到cookie时抛出异常
        long userId = Long.valueOf(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(token))));
        BaseResult result = new BaseResult();
        try {
            result.setSuccess(stepService.addStep(userId, xAxis, yAxis, zAxis));
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "添加步数失败,xAxis:{0},yAxis:{1},zAxis:{2},token:{3}", xAxis, yAxis, zAxis, token);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "添加步数失败,xAxis:{0},yAxis:{1},zAxis:{2},token:{3}", xAxis, yAxis, zAxis, token);
        }
        return result;
    }

    /**
     * 查询今日步数
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/day/{userId}")
    public SimpleResult<String> queryTodayByToday(@PathVariable long userId) {
        LoggerUtil.info(LOGGER, "enter in StepController[queryTodayByToday],userId:{0}", userId);
        SimpleResult<String> result = new SimpleResult<>();
        try {
            result.setResult(stepService.queryTotalByToday(userId));
            result.setSuccess(true);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "查询今日步数失败,userId:{0}", userId);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "查询今日步数失败,userId:{0}", userId);
        }
        return result;
    }

    /**
     * 查询这周步数
     * @param userId
     * @return
     */
    @GetMapping(value = "/week/{userId}")
    public ListResult<String> queryTotalByWeek(@PathVariable long userId) {
        LoggerUtil.info(LOGGER, "enter in StepController[queryTotalByWeek],userId:{0}", userId);
        ListResult<String> result = new ListResult<>();
        try {
            result.setResults(stepService.queryTotalByWeek(userId));
            result.setSuccess(true);
        } catch (BusinessException be) {
            ExceptionHandler.handleBusinessException(LOGGER, result, be, "查询这周步数失败,userId:{0}", userId);
        } catch (Exception ex) {
            ExceptionHandler.handleSystemException(LOGGER, result, ex, "查询这周步数失败,userId:{0}", userId);
        }
        return result;
    }

    @GetMapping(value = "/knn/{userId}")
    public BaseResult queryKnn(@PathVariable long userId){
        LoggerUtil.info(LOGGER,"enter in StepController[queryKnn],userId:{0}",userId);
        BaseResult result=new BaseResult();
        try{
            result.setSuccess(stepService.knnAlgorithm(userId));
        }catch (BusinessException be){
            ExceptionHandler.handleBusinessException(LOGGER,result,be,"查询knn结果失败,userId:{0}",userId);
        }catch (Exception ex){
            ExceptionHandler.handleSystemException(LOGGER,result,ex,"查询knn结果失败,userId:{0}",userId);
        }
        return result;
    }

    /**
     * 新增knn结果
     * @param userId
     * @return
     */
    @PostMapping(value = "/knn/{userId}")
    public BaseResult KnnAdd(@PathVariable long userId,Double xAxis,Double yAxis,Double zAxis){
        LoggerUtil.info(LOGGER,"enter in StepController[KnnAdd],userId:{0},xAxis:{1},yAxis:{2},zAxis:{3}",userId,xAxis,yAxis,zAxis);
        BaseResult result=new BaseResult();
        try{
            result.setSuccess(stepService.knnAdd(userId,xAxis,yAxis,zAxis));
        }catch (BusinessException be){
            ExceptionHandler.handleBusinessException(LOGGER,result,be,"新增knn失败,userId:{0}",userId);
        }catch (Exception ex){
            ExceptionHandler.handleSystemException(LOGGER,result,ex,"新增knn失败,userId:{0}",userId);
        }
        return result;
    }


}
