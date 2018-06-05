package com.fengsigaoju.health.user.util;

import com.fengsigaoju.health.user.domain.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author yutong song
 * @date 2018/3/29
 */
public class DateUtils {

    /**
     * 兼顾性能和simpleDateFormat的安全性,所以使用一个线程使用一个simpleDateFormat实例的方式
     */
    private static ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>();

    private static String lock = "local";

    /**
     * 一分钟的长度
     */
    private static final long MINUTE_LENGTH = 1000 * 60;

    /**
     * 一天的长度
     */
    private static final long DAY_LENGTH = 1000 * 60 * 60 * 24;

    /**
     * 一周的长度
     */
    private static final long WEEK_length = 100 * 60 * 60 * 24 * 7;

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 将字符串表达式转换为日期
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static Date parse(String str) throws ParseException {
        if (str == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = local.get();
        if (simpleDateFormat == null) {
            synchronized (lock) {
                if (simpleDateFormat == null) {
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    local.set(simpleDateFormat);
                    return simpleDateFormat.parse(str);
                }
            }
        }
        return simpleDateFormat.parse(str);
    }

    /**
     * 将日期转换为字符串表达式
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = local.get();
        if (simpleDateFormat == null) {
            synchronized (lock) {
                if (simpleDateFormat == null) {
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    local.set(simpleDateFormat);
                    return simpleDateFormat.format(date);
                }
            }
        }
        return simpleDateFormat.format(date);
    }

    /**
     * 获取今天起始
     *
     * @return
     */
    public static String getToday(Date date) {
        try {
            ValidateUtils.checkNotNull(date, "[getBeforeDay]传入对象为空");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date dayEnd = calendar.getTime();
            return format(dayEnd);
        } catch (Exception e) {
            LoggerUtil.error(e, LOGGER, "获取今日起始,date:{0}", date);
            throw new BusinessException("日期格式有误");
        }
    }

    /**
     * 获取周一起始日期
     *
     * @param date
     * @return
     */
    public static String getMon(Date date) {
        try {
            ValidateUtils.checkNotNull(date, "[getMon]传入对象为空");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Date dayEnd = calendar.getTime();
            return format(dayEnd);
        } catch (Exception e) {
            LoggerUtil.error(e, LOGGER, "获取周一起始,date:{0}", date);
            throw new BusinessException("日期格式有误");
        }
    }

    /**
     * 获取周二结束日期
     *
     * @param date
     * @return
     */
    public static String getTue(Date date) {
        try {
            ValidateUtils.checkNotNull(date, "[getTue]传入对象为空");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            Date dayEnd = calendar.getTime();
            return format(dayEnd);
        } catch (Exception e) {
            LoggerUtil.error(e, LOGGER, "获取周二起始,date:{0}", date);
            throw new BusinessException("日期格式有误");
        }
    }

    /**
     * 获取周三结束日期
     *
     * @param date
     * @return
     */
    public static String getWed(Date date) {
        try {
            ValidateUtils.checkNotNull(date, "[getMonWed]传入对象为空");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            Date dayEnd = calendar.getTime();
            return format(dayEnd);
        } catch (Exception e) {
            LoggerUtil.error(e, LOGGER, "获取周三起始,date:{0}", date);
            throw new BusinessException("日期格式有误");
        }
    }

    /**
     * 获取周四结束日期
     *
     * @param date
     * @return
     */
    public static String getThu(Date date) {
        try {
            ValidateUtils.checkNotNull(date, "[getThu]传入对象为空");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            Date dayEnd = calendar.getTime();
            return format(dayEnd);
        } catch (Exception e) {
            LoggerUtil.error(e, LOGGER, "获取周四起始,date:{0}", date);
            throw new BusinessException("日期格式有误");
        }
    }

    /**
     * 获取周五结束日期
     *
     * @param date
     * @return
     */
    public static String getFri(Date date) {
        try {
            ValidateUtils.checkNotNull(date, "[getFri]传入对象为空");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            Date dayEnd = calendar.getTime();
            return format(dayEnd);
        } catch (Exception e) {
            LoggerUtil.error(e, LOGGER, "获取周五起始,date:{0}", date);
            throw new BusinessException("日期格式有误");
        }
    }

    /**
     * 获取周六结束日期
     *
     * @param date
     * @return
     */
    public static String getSau(Date date) {
        try {
            ValidateUtils.checkNotNull(date, "[getSau]传入对象为空");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            Date dayEnd = calendar.getTime();
            return format(dayEnd);
        } catch (Exception e) {
            LoggerUtil.error(e, LOGGER, "获取周六起始,date:{0}", date);
            throw new BusinessException("日期格式有误");
        }
    }


    /**
     * 获取周日结束日期
     *
     * @param date
     * @return
     */
    public static String getSun(Date date) {
        try {
            ValidateUtils.checkNotNull(date, "[getSun]传入对象为空");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Date dayEnd = calendar.getTime();
            return format(dayEnd);
        } catch (Exception e) {
            LoggerUtil.error(e, LOGGER, "获取周日起始,date:{0}", date);
            throw new BusinessException("日期格式有误");
        }
    }


    public static String nextMonth(Date date) {
        try {
            ValidateUtils.checkNotNull(date, "[nextMonth]传入的对象为空");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
            return format(calendar.getTime());
        } catch (Exception e) {
            LoggerUtil.error(e, LOGGER, "获取下一个月书籍失败,date:{0}", date);
            throw new BusinessException("日期格式有误");
        }
    }
}
