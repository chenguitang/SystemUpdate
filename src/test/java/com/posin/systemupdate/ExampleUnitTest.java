package com.posin.systemupdate;

import org.junit.Test;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

//        System.out.println("decimalFormat: " + decimalFormat(542540.2546, 2));
        System.out.println("isNumeric: " + isNumeric("-12154.01254a"));
        System.out.println("\nisNumericTwo: " + isNumericTwo("-1215401254"));

        String aa = null;
        System.out.println("ajsdlfjladj:  " +aa.length());
    }

    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^-?[0-9]+.?[0-9]+$");
        else
            return false;
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumericTwo(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
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