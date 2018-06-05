package com.fengsigaoju.health.step.service.impl;

import com.fengsigaoju.health.step.dao.StepDao;
import com.fengsigaoju.health.step.domain.dataobject.StepDO;
import com.fengsigaoju.health.step.domain.dataobject.StepPosture;
import com.fengsigaoju.health.step.service.StepService;
import com.fengsigaoju.health.step.util.DataUtils;
import com.fengsigaoju.health.step.util.DateUtils;
import com.fengsigaoju.health.step.util.LoggerUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yutong song
 * @date 2018/4/25
 */
@Service
public class StepServiceImpl implements StepService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StepServiceImpl.class);

    @Autowired
    private StepDao stepDao;

    private static Date before_knn=new Date(1,4,4);

    private static Map<Long,Queue<StepDO>>map=Maps.newHashMap();

    @Override
    public boolean addStep(long userId, double xAxis, double yAxis, double zAxis) {
        LoggerUtil.info(LOGGER, "enter in StepServiceImpl[addStep],userId:{0},xAxis:{1},yAxis:{2},zAxis:{3}", xAxis, yAxis, zAxis);
        StepDO stepDO = new StepDO();
        stepDO.setUserId(userId);
        stepDO.setxAxis(xAxis);
        stepDO.setyAxis(yAxis);
        stepDO.setzAxis(zAxis);
        return stepDao.addStep(stepDO) > 0;
    }

    @Override
    public String queryTotalByToday(long userId) throws ParseException {
        LoggerUtil.info(LOGGER, "enter in StepServiceImpl[queryTotalByToady,userId:{0}", userId);
        Date today = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        today=DateUtils.parse(sdf.format(today));
        String startTime = DateUtils.getToday(today);
        String endTime = DateUtils.format(today);
        return String.valueOf(stepDao.countByTimePoint(buildMap(startTime, endTime, userId)));
    }

    /**
     * 拼接map参数
     *
     * @param startTime
     * @param endTime
     * @param userId
     * @return
     */
    private Map<String, String> buildMap(String startTime, String endTime, long userId) {
        Map<String, String> map = Maps.newHashMap();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("userId", String.valueOf(userId));
        return map;
    }

    /**
     * 获取一周步数
     * @param userId
     * @return
     */
    @Override
    public List<String> queryTotalByWeek(long userId) throws ParseException {
        LoggerUtil.info(LOGGER, "enter in StepServiceImpl[queryTotalWeek],userId:{0}", userId);
        List<String> list = Lists.newArrayList();
        Date today = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        today=DateUtils.parse(sdf.format(today));
        list.add(String.valueOf(stepDao.countByTimePoint(buildMap(DateUtils.getMon(today), DateUtils.getTue(today), userId))));
        list.add(String.valueOf(stepDao.countByTimePoint(buildMap(DateUtils.getTue(today), DateUtils.getWed(today), userId))));
        list.add(String.valueOf(stepDao.countByTimePoint(buildMap(DateUtils.getWed(today), DateUtils.getThu(today), userId))));
        list.add(String.valueOf(stepDao.countByTimePoint(buildMap(DateUtils.getThu(today), DateUtils.getFri(today), userId))));
        list.add(String.valueOf(stepDao.countByTimePoint(buildMap(DateUtils.getFri(today), DateUtils.getSau(today), userId))));
        list.add(String.valueOf(stepDao.countByTimePoint(buildMap(DateUtils.getSau(today), DateUtils.getSun(today), userId))));
        list.add(String.valueOf(stepDao.countByTimePoint(buildMap(DateUtils.getSun(today), DateUtils.format(today), userId))));
        return list;
    }


    @Override
    //添加knn的参数
    public synchronized  boolean knnAdd(long userId,double xAxis,double yAxis,double zAxis){
        LoggerUtil.info(LOGGER, "enter in StepServiceImpl[knnAdd],userId:{0},xAxis:{1},yAxis:{2},zAxis:{3}", userId, xAxis, yAxis, zAxis);
        StepDO stepDO=new StepDO();
        stepDO.setUserId(userId);
        stepDO.setxAxis(xAxis);
        stepDO.setyAxis(yAxis);
        stepDO.setzAxis(zAxis);
        if (map.get(userId)==null){
          Queue<StepDO>queue=new ArrayDeque<>();
          map.put(userId,queue);
        }
        Queue<StepDO>queue=map.get(userId);
        if (queue.size()<10){
            queue.add(stepDO);
        }else{
            queue.poll();
            queue.add(stepDO);
        }
        return true;
    }

    //从队列里面取出所有的元素，然后计算特征值
    @Override
    public boolean knnAlgorithm(long userId) throws ParseException {
        LoggerUtil.info(LOGGER,"enter in StepServiceImpl[knnAlgorithm],userId:{0}",userId);
        Date today = new Date();
        //这里判断如果超过60s的判断是否摔倒直接
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        today=DateUtils.parse(sdf.format(today));
        if (today.getTime()-before_knn.getTime()<=60*1000) {
            return false;
        }
        //最近10组数据
        List<StepDO>stepDOList=Lists.newArrayList();
        Queue<StepDO>queue=map.get(userId);
        if (queue!=null) {
            while (queue.size() < 10 && !queue.isEmpty()) {
                stepDOList.add(queue.poll());
            }
        }
        //1,获取最近0.5秒内的历史数据
        String startTime = DateUtils.getknn(today);
        String endTime = DateUtils.format(today);
        LoggerUtil.info(LOGGER,"startTime:{0},endTime:{1}",startTime,endTime);
        if (!stepDOList.isEmpty()) {
            //2,计算每一个特征值
            double average = DataUtils.calAverage(stepDOList);
            double medians = DataUtils.calMedians(stepDOList);
            double variance = DataUtils.calVariance(stepDOList);
            LoggerUtil.info(LOGGER, "average:{0},medians:{1},variance:{2}", average, medians, variance);
            //3,读取历史配置文件
            List<StepPosture> stepPostureList = DataUtils.readData();
            LoggerUtil.info(LOGGER, "stepPostureList:{0}", stepPostureList);
            List<Double> range = Lists.newArrayList();
            Map<Double, String> map = Maps.newHashMap();
            Map<String, Integer> countMap = Maps.newHashMap();
            //3,计算该时刻特征值和样本特征值的距离
            for (StepPosture stepPosture : stepPostureList) {
                double distance = cal(stepPosture, average, medians, variance);
                map.put(distance, stepPosture.getCategory());
                range.add(distance);
            }
            LoggerUtil.info(LOGGER, "ramge is:{0}", range);
            Collections.sort(range);
            for (int k = 0; k < 3; k++) {
                Integer i = countMap.get(map.get(range.get(k)));
                if (i == null) {
                    countMap.put(map.get(range.get(k)), 0);
                } else {
                    countMap.put(map.get(range.get(k)), i + 1);
                }
            }
            int max = -1;
            String answer = null;
            for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    answer = entry.getKey();
                }
            }
            if (StringUtils.equals(answer, "摔倒")) {
                before_knn = today;
                return true;
            }
        }
    return false;
    }

    private double cal(StepPosture stepPosture, double average, double medians, double variance) {
            return (stepPosture.getAverage() - average) * (stepPosture.getAverage() - average ) + (stepPosture.getMedians() - medians) * (stepPosture.getMedians() - medians) +
                    (stepPosture.getVariance() - variance) * (stepPosture.getVariance() - variance);

    }

}
