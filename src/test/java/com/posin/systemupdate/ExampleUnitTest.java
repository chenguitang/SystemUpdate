package com.posin.systemupdate;

import org.junit.Test;

import java.text.DecimalFormat;

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

        System.out.println("decimalFormat: " + decimalFormat(542540.2546, 2));
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