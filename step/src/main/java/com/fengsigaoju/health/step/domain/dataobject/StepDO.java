package com.fengsigaoju.health.step.domain.dataobject;

import com.fengsigaoju.health.step.domain.BaseObject;

import java.util.Date;

/**
 * step对象
 *
 * @author yutong song
 * @date 2018/3/29
 */
public class StepDO extends BaseObject {

    private long stepId;

    private long userId;

    private Date timepoint;

    private Double xAxis;

    private Double yAxis;

    private Double zAxis;

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getTimepoint() {
        return timepoint;
    }

    public void setTimepoint(Date timepoint) {
        this.timepoint = timepoint;
    }

    public Double getxAxis() {
        return xAxis;
    }

    public void setxAxis(Double xAxis) {
        this.xAxis = xAxis;
    }

    public Double getyAxis() {
        return yAxis;
    }

    public void setyAxis(Double yAxis) {
        this.yAxis = yAxis;
    }

    public Double getzAxis() {
        return zAxis;
    }

    public void setzAxis(Double zAxis) {
        this.zAxis = zAxis;
    }
}
