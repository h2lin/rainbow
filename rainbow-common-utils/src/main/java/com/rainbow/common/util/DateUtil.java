package com.rainbow.common.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @Desc 时间日期工具类
 * @Author wuzh
 * @Date 2020/7/22
 */
@Slf4j
@UtilityClass
public class DateUtil {
    /** 格式：yyyy-MM */
    public static final String FOROMAT_DATE_YY_MM = "yyyy-MM";
    /** 格式：yyyy-MM-dd */
    public static final String FOROMAT_DATE = "yyyy-MM-dd";
    /** 格式：yyyy-MM-dd HH:mm:ss */
    public static final String FOROMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /** 格式：yyyy-MM-dd HH:mm:ss.SSS */
    public static final String FOROMAT_DATETIME_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    /** 格式：yyyy-MM-dd HH */
    public static final String FOROMAT_DATETIME_HH = "yyyy-MM-dd HH";
    /** 格式：yyyy-MM-dd HH:mm */
    public static final String FOROMAT_DATETIME_HH_MM = "yyyy-MM-dd HH:mm";
    /** 所有的日期格式 */
    private static final Map<Integer, DateTimeFormatter> DATETIME_FORMATTER_ROUTE = new HashMap<>();

    static {
        // 所有公有
        Field[] fields = DateUtil.class.getFields();
        // 日期格式变量前缀
        String dateFormatPrefix = "FOROMAT_";
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (fieldName.startsWith(dateFormatPrefix)) {
                String format = null;
                try {
                    format = String.valueOf(field.get(field));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
                setDateTimeFormatterRoute(format);
            }
        }
    }

    private static void setDateTimeFormatterRoute(String format) {
        DATETIME_FORMATTER_ROUTE.put(format.length(), DateTimeFormat.forPattern(format));
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取当前日期（yyyy-MM-dd）
     *
     * @return
     */
    public static String getCurrentDate() {
        return DateTime.now().toString(FOROMAT_DATE);
    }

    /**
     * 获取当前日期（yyyy-MM-dd HH:mm:ss）
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return DateTime.now().toString(FOROMAT_DATETIME);
    }

    /**
     * 获取当前日期
     *
     * @param format 格式
     * @return
     */
    public static String getCurrentDateTime(String format) {
        return DateTime.now().toString(format);
    }

    /**
     * 格式化日志
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        return new DateTime(date).toString(format);
    }

    /**
     * 格式化日志
     *
     * @param date
     * @return 返回格式：yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        return format(date, FOROMAT_DATE);
    }

    /**
     * 格式化日志
     *
     * @param date
     * @return 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        return format(date, FOROMAT_DATETIME);
    }

    /**
     * 日期字符串转Date对象
     *
     * <h3>支持的字符串格式</h3>
     * <ul>
     * <li>yyyy-MM
     * <li>yyyy-MM-dd
     * <li>yyyy-MM-dd HH
     * <li>yyyy-MM-dd HH:mm
     * <li>yyyy-MM-dd HH:mm:ss
     * </ul>
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static Date toDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        DateTimeFormatter format = DATETIME_FORMATTER_ROUTE.get(dateStr.length());
        if (Objects.isNull(format)) {
            throw new IllegalArgumentException("dateStr格式不支持！");
        }

        return format.parseDateTime(dateStr).toDate();
    }

    /**
     * String转Date
     * @param dateStr 需要转换的日期 例如 2019-11-08
     * @param format 格式 例如 yyyy-MM-dd
     * @return
     */
    public static Date strToDate(String dateStr,String format){
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        Date date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            date = df.parse(dateStr);
        } catch (ParseException e) {
            log.error("日期转换错误!",e);
        }
        return date;
    }
}
