package com.fengsigaoju.health.user.service.impl;

import com.fengsigaoju.health.user.service.IdGenerateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author yutong song
 * @date 2018/4/25
 */
@Service
public class IdGenerateServiceImpl implements IdGenerateService {
    //开始该类生成ID的时间截，1288834974657 (Thu, 04 Nov 2010 01:42:54 GMT) 这一时刻到当前时间所经过的毫秒数，占 41 位（还有一位是符号位，永远为 0）。
    private final long startTime = 1463834116272L;

    //机器id所占的位数
    private static long workerIdBits = 5L;

    //数据标识id所占的位数
    private  static long datacenterIdBits = 5L;

    //支持的最大机器id，结果是31,这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数（不信的话可以自己算一下，记住，计算机中存储一个数都是存储的补码，结果是负数要从补码得到原码）
    private static long maxWorkerId = -1L ^ (-1L << workerIdBits);

    //支持的最大数据标识id
    private static  long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    //序列在id中占的位数
    private static long sequenceBits = 12L;

    //机器id向左移12位
    private long workerIdLeftShift = sequenceBits;

    //数据标识id向左移17位
    private long datacenterIdLeftShift = workerIdBits + workerIdLeftShift;

    //时间截向左移5+5+12=22位
    private long timestampLeftShift = datacenterIdBits + datacenterIdLeftShift;

    //生成序列的掩码，这里为1111 1111 1111
    private static  long sequenceMask = ~(-1 << sequenceBits);

    @Value("${server.port}")
    private long workerId;

    @Value("${datacenterId}")
    private long datacenterId;

    //同一个时间截内生成的序列数，初始值是0，从0开始
    private long sequence = 0L;

    //上次生成id的时间截
    private long lastTimestamp = -1L;

    //生成id
    @Override
    public synchronized long nextId(){
        long timestamp = timeGen();
        if(timestamp < lastTimestamp){
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果是同一时间生成的，则自增
        if(timestamp == lastTimestamp){
            sequence = (sequence + 1) & sequenceMask;
            if(sequence == 0){
                //生成下一个毫秒级的序列
                timestamp = tilNextMillis();
                //序列从0开始
                sequence = 0L;
            }
        }else{
            //如果发现是下一个时间单位，则自增序列回0，重新自增
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        //看本文第二部分的结构图，移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - startTime) << timestampLeftShift)
                | (datacenterId << datacenterIdLeftShift)
                | (workerId << workerIdLeftShift)
                | sequence;
    }

    protected long tilNextMillis(){
        long timestamp = timeGen();
        if(timestamp <= lastTimestamp){
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen(){
        return System.currentTimeMillis();
    }
}
