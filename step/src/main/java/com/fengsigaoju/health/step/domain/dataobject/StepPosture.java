package com.fengsigaoju.health.step.domain.dataobject;

import com.fengsigaoju.health.step.domain.BaseObject;

/**
 * 步数信息
 * @author yutong song
 * @date 2018/5/24
 */
public class StepPosture extends BaseObject {

    /**
     * 平均值
     */
    private double average;

    /**
     * 中位数
     */
    private double medians;

    /**
     * 方差
     */
    private double variance;

    /**
     * 类别
     */
    private String category;

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getMedians() {
        return medians;
    }

    public void setMedians(double medians) {
        this.medians = medians;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
