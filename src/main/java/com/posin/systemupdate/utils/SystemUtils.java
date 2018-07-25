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

    /**
     * 获取机器SN码
     *
     * @return String
     */
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

    /**
     * 获取系统名称(系统更新名称标识)
     *
     * @return String
     */
    public static String getPosinModel() {
        Properties p = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/system/build.prop");
            p.load(fis);
            return p.getProperty("ro.posin.model");
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

    /**
     * 获取系统版本
     *
     * @return String
     */
    public static String getSystemVersion() {
        Properties p = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/system/build.prop");
            p.load(fis);
            return p.getProperty("ro.posin.sysversion");
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


    /**
     * 获取系统日期版本
     * 系统更新校验版本
     *
     * @return String
     */
    public static String getSystemDateVersion() {
        Properties p = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/system/build.prop");
            p.load(fis);
            return p.getProperty("ro.posin.version");
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
