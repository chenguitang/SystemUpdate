package com.posin.systemupdate.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * FileName: MiniposInfo
 * Author: Greetty
 * Time: 2018/5/29 20:21
 * Desc: TODO
 */
public class MiniposInfo {

	private static final String TAG = "MiniposInfo";
	
	private static boolean mLoaded = false;
	
	private static final String EMPTY = "";

	private static String mModel = EMPTY;
	private static String mSn = EMPTY;
	private static String mSoftwareVerson = EMPTY;
	private static String mHardwareVersion = EMPTY;
	private static String mPlatform = EMPTY;
	
	private MiniposInfo() {
	}
	
	public static String getModel() {
		return mModel;
	}
	
	public static String getSn() {
		return mSn;
	}
	
	public static String getSoftwareVersion() {
		return mSoftwareVerson;
	}
	
	public static String getHardwareVersion() {
		return mHardwareVersion;
	}
	
	public static String getPlatform() {
		return mPlatform;
	}
	
	public static void load() {
		if(mLoaded)
			return;
		mLoaded = true;
		
		Properties p = new Properties();
		FileInputStream fis = null;

		try {

			fis = new FileInputStream("/system/build.prop");
			p.load(fis);
			
			String s = p.getProperty("ro.posin.model");
			if(s != null)
				mModel = s.toLowerCase();
			
			s = p.getProperty("ro.posin.version");
			if(s != null)
				mSoftwareVerson = s.toLowerCase();
			
			s = p.getProperty("ro.posin.hw");
			if(s != null)
				mHardwareVersion = s.toLowerCase();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		fis = null;
		
		try {
			fis = new FileInputStream("/proc/cpuinfo");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String s;
			while((s=br.readLine()) != null) {
				//Log.d(TAG, "read : " + s);
				if(s.startsWith("Hardware")) {
					int pos = s.indexOf(':');
					if(pos > 0) {
						mPlatform = s.substring(pos+2).trim();
						break;
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		Log.d(TAG, "model="+mModel);
		Log.d(TAG, "sn="+mSn);
		Log.d(TAG, "sversion="+mSoftwareVerson);
		Log.d(TAG, "hversion="+mHardwareVersion);
		Log.d(TAG, "platform="+mPlatform);
		
	}
}
