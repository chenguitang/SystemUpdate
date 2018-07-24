package com.posin.systemupdate.utils;

import java.text.DecimalFormat;

/**
 * FileName: StringUtils
 * Author: Greetty
 * Time: 2018/6/14 15:48
 * Description: TODO
 */
public class StringUtils {

    /**
     * 拼接字符串
     *
     * @param message 字符串
     * @return 拼接后字符串
     */
    public static String append(Object... message) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : message) {
            sb.append(obj);
        }
        return sb.toString();
    }

    /**
     * 增加空格数
     *
     * @param size 空格数量
     * @return String
     */
    public static String appendSpace(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 格式化Double字符
     *
     * @param value double
     * @param scale int 保留几位数
     * @return String
     */
    public static String decimalFormat(double value, int scale) {
        StringBuilder sb = new StringBuilder();

        sb.append("0");
        if (scale > 0) {
            sb.append(".");
        }
        for (int i = 0; i < scale; i++) {
            sb.append("0");
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        return df.format(value);
    }


}
