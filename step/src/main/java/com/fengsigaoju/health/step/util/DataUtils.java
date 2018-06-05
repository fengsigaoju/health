package com.fengsigaoju.health.step.util;

import com.fengsigaoju.health.step.domain.dataobject.StepDO;
import com.fengsigaoju.health.step.domain.dataobject.StepPosture;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * @author yutong song
 * @date 2018/5/24
 */
public class DataUtils {

    private static final Logger LOGGER= LoggerFactory.getLogger(DataUtils.class);
    public static List<StepPosture> readData() {
        List<StepPosture> stepPostureList= Lists.newArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/usr/local/step/BaseData"));
            String line = br.readLine();
            if (line != null) {
                line = br.readLine();
            }
            while (line != null) {
                String temp[] = line.split("\\s+");
                StepPosture stepPosture = new StepPosture();
                stepPosture.setAverage(Double.valueOf(temp[0]));
                stepPosture.setMedians(Double.valueOf(temp[1]));
                stepPosture.setVariance(Double.valueOf(temp[2]));
                stepPosture.setCategory(temp[3]);
                stepPostureList.add(stepPosture);
                line=br.readLine();
            }
        } catch (Exception e) {
           LoggerUtil.error(e,LOGGER,"读取文件失败,file path:{0}",DataUtils.class.getResource("/").getPath()+"knn/BaseData");
        }
        return stepPostureList;
    }
    public static double calAverage(List<StepDO> stepDOList){
        if (!CollectionUtils.isEmpty(stepDOList)){
            double sum=0;
            for (StepDO step:stepDOList){
                sum=sum+cal(step.getxAxis(),step.getyAxis(),step.getzAxis());
            }
            return sum/stepDOList.size();
        }
        return 0;
    }

    /**
     * 返回中位数
     * @param stepDOList
     * @return
     */
    public static double calMedians(List<StepDO> stepDOList){
        if (!CollectionUtils.isEmpty(stepDOList)){
            List<Double>medianList=Lists.newArrayList();
           for (StepDO stepDO:stepDOList){
               medianList.add(cal(stepDO.getxAxis(),stepDO.getyAxis(),stepDO.getzAxis()));
           }
            Collections.sort(medianList);
            int len=medianList.size();
            if(len%2==0) {
                return (medianList.get(len/2)+medianList.get(len/2-1))/2;
            }
            return medianList.get(len/2);
        }
        return 0;
    }

    /**
     * 计算方差
     * @param stepDOList
     * @return
     */
    public static double calVariance(List<StepDO>stepDOList){
        if (!CollectionUtils.isEmpty(stepDOList)){
           double x=calAverage(stepDOList);
           double sum=0;
           for (StepDO stepDO:stepDOList){
               sum=sum+(x-cal(stepDO.getxAxis(),stepDO.getyAxis(),stepDO.getzAxis())*cal(stepDO.getxAxis(),stepDO.getyAxis(),stepDO.getzAxis()));
           }
           return sum/stepDOList.size();
        }
        return 0;
    }
    /**
     * 返回综合数
     * @param xAxis
     * @param yAxis
     * @param zAxis
     * @return
     */
    public static double cal(double xAxis,double yAxis,double zAxis){
        return xAxis*xAxis+yAxis*yAxis+zAxis+zAxis;
    }


}
