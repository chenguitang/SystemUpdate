package com.posin.systemupdate.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * FileName: SystemUtils
 * Author: Greetty
 * Time: 2018/5/30 9:15
 * Desc: TODO
 */
public class SystemUtils {

    public static String getSn() {
        Properties p = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/system/etc/posin/advertisement.prop");
            p.load(fis);
            return p.getProperty("ro.ad.switch");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
