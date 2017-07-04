package com.chaoyue.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串处理
 * Created by chaoyue on 2017/5/24.
 */
public class StringUtil {

    /**
     * 去空格(为空转NULL)
     *
     * @param obj
     * @return String
     */
    public static String trimToNull(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            String str = (String) obj;
            if ("".equals(str)) {
                return null;
            } else {
                return str.trim();
            }
        }
        return null;
    }

    /**
     * 去空格(为空转NULL)
     *
     * @param str
     * @return String
     */
    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        } else {
            if ("".equals(str)) {
                return null;
            } else {
                return str.trim();
            }
        }
    }

    /**
     * 去空格(为空转"")
     *
     * @param obj
     * @return String
     */
    public static String trimToString(Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof String) {
            String str = (String) obj;
            return str.trim();

        }
        return "";
    }

    /**
     * 去空格(为空转"")
     *
     * @param str
     * @return String
     */
    public static String trimToString(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    /**
     * 去空格(为空转"0")
     *
     * @param obj
     * @return String
     */
    public static String trimToZero(Object obj) {
        if (obj == null) {
            return "0";
        } else if (obj instanceof String) {
            String str = (String) obj;
            return str.trim();

        }
        return "0";
    }

    /**
     * 去空格(为空转"0")
     *
     * @param str
     * @return String
     */
    public static String trimToZero(String str) {
        if (str == null) {
            return "0";
        } else {
            return str.trim();
        }
    }

    /**
     * 字符串为空判断
     *
     * @param str
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(trimToString(str))) {
            return true;
        }
        return false;
    }

    /**
     * 字符串非空判断
     *
     * @param str
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        if (str == null || "".equals(trimToString(str))) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否为数字类型
     *
     * @param str
     * @return boolean
     */
    public static boolean isNumber(String str) {
        String regex = "^\\d+$";
        return str.matches(regex);
    }

    /**
     * 判断字符串是否为金额类型
     *
     * @param str
     * @return boolean
     */
    public static boolean isAmt(String str) {
        String regex = "^\\d+|\\d+\\.{1}\\d+$";
        return str.matches(regex);
    }

    /**
     * 是否日期字符串
     *
     * @param str
     * @return boolean
     */
    public static boolean isDate(String str) {
        String regex = "^\\d{4}\\-\\d{2}\\-\\d{2}$";
        return str.matches(regex);
    }

    /**
     * 过滤出字符串中的数字
     *
     * @param str
     * @return String[]
     */
    public static String[] filterNumber(String str) {
        String regex = "[^(0-9)]";
        String targetStr = str.replaceAll(regex, "|");
        String[] array = targetStr.split("\\|+");

        return trimNullValue(array);
    }

    /**
     * 过滤出字符串中的字母
     *
     * @param str
     * @return String[]
     */
    public static String[] filterLetter(String str) {
        String regex = "[^(a-zA-Z)]";
        String targetStr = str.replaceAll(regex, "|");
        String[] array = targetStr.split("\\|+");

        return trimNullValue(array);
    }

    /**
     * 过滤出字符串中的中文
     *
     * @param str
     * @return String[]
     */
    public static String[] filterChinese(String str) {
        String regex = "[^(\\u4e00-\\u9fa5)]";
        String targetStr = str.replaceAll(regex, "|");
        String[] array = targetStr.split("\\|+");

        return trimNullValue(array);
    }

    /**
     * 去掉字符数组中的空值
     *
     * @param array
     * @return String[]
     */
    public static String[] trimNullValue(String[] array) {
        List<String> list = new ArrayList<String>();
        if (array != null && array.length > 0) {
            for (String s : array) {
                if (s != null && !"".equals(s)) {
                    list.add(s);
                }
            }
        }
        int size = list.size();
        return list.toArray(new String[size]);
    }
}
