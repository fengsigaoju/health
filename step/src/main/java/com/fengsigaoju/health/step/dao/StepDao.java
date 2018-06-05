package com.fengsigaoju.health.step.dao;

import com.fengsigaoju.health.step.domain.dataobject.StepDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yutong song
 * @date 2018/4/25
 */
@Mapper
public interface StepDao {

    /**
     * 增加步数
     * @param stepDO
     * @return
     */
    long addStep(StepDO stepDO);

    /**
     * 获取时间点之间的步数数量
     * @param map
     * @return
     */
    long countByTimePoint(Map<String,String>map);

}
