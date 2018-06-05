package com.fengsigaoju.health.step.service;


import java.text.ParseException;
import java.util.List;

/**
 * @author yutong song
 * @date 2018/4/25
 */
public interface StepService {

    /**
     * 增加步数记录
     * @param userId
     * @param xAxis
     * @param yAxis
     * @param zAxis
     * @return
     */
    boolean addStep(long userId,double xAxis,double yAxis,double zAxis);

    /**
     * 查询今日步数
     * @param userId
     * @return
     */
    String queryTotalByToday(long userId) throws ParseException;

    /**
     * 查询一周步数
     * @param userId
     * @return
     */
    List<String> queryTotalByWeek(long userId) throws ParseException;

    /**
     * 判断是否需要提醒用户
     * @param userId
     * @return
     */
    boolean knnAlgorithm(long userId) throws ParseException;

    /**
     * 添加knn纪录
     * @param userId
     * @param xAxis
     * @param yAxis
     * @param zAxis
     * @return
     */
    boolean knnAdd(long userId,double xAxis,double yAxis,double zAxis);
}
