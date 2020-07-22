package com.rainbow.common.util;

import lombok.experimental.UtilityClass;

/**
 * @Desc 日志工具类
 * @Author wuzh
 * @Date 2020/7/22
 */
@UtilityClass
public class LogUtil {
//    /** 日志最大长度 */
//    private static final int LOG_MAX_LENGTH = 8192;
//
//    public static String truncate(String format, Object... args) {
//        String msg = ParameterizedMessage.format(format, args);
//        return truncate(msg, LOG_MAX_LENGTH);
//    }
//
//    public static String truncate(String msg) {
//        return truncate(msg, LOG_MAX_LENGTH);
//    }
//
//    public static String truncate(int maxLength, String format, Object... args) {
//        String msg = ParameterizedMessage.format(format, args);
//        return truncate(msg, maxLength);
//    }
//
//    private static String truncate(final String str, final int maxWidth) {
//        if (maxWidth < 0) {
//            throw new IllegalArgumentException("maxWith cannot be negative");
//        }
//        if (str == null) {
//            return null;
//        }
//        if (str.length() > maxWidth) {
//            final int ix = maxWidth > str.length() ? str.length() : maxWidth;
//            return str.substring(0, ix);
//        }
//        return str.substring(0);
//    }
}
