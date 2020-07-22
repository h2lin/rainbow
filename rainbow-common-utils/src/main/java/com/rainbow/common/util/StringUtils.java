package com.rainbow.common.util;

/**
 * @Desc String工具类
 * @Author wuzh
 * @Date 2020/7/22
 */
public class StringUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String subExceed(final String str, final int length) {
        return !isEmpty(str) && length < str.length() ? str.substring(0, length) : str;
    }
}
