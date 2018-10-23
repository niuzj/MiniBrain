package com.niuzj.corelibrary.utils;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.niuzj.corelibrary.base.BaseApplication;


/**
 * 获取设备信息
 */
public class DeviceUtils {

    private static String errorPlaceText = "";

    /**
     * ip地址
     */
    public static String ip() {
        return null;
    }


    /**
     * 设备制造商
     */
    public static String make() {
        if (null != Build.MANUFACTURER) {
            return Build.MANUFACTURER;
        } else {
            return errorPlaceText;
        }

    }

    /**
     * 设备型号
     */
    public static String model() {
        if (null != Build.MODEL) {
            return Build.MODEL;
        } else {
            return errorPlaceText;
        }

    }

    /**
     * 设备操作系统
     */
    public static String os() {
        return "Android";
    }

    /**
     * 设备操作系统版本号
     */
    public static String osVersion() {
        if (null != Build.VERSION.RELEASE) {
            return Build.VERSION.RELEASE;
        } else {
            return errorPlaceText;
        }
    }

    /**
     * 获取屏幕分辨率
     */
    public static Display getScreenSize() {
        WindowManager wm = (WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display;

    }

    /**
     * 获取屏幕的宽
     */
    public static int getDeviceWidth() {
        Display screenSize = getScreenSize();
        int deviceWidth = 0;
        if (null != screenSize) {
            deviceWidth = screenSize.getWidth();
        }
        return deviceWidth;

    }

    /**
     * 获取屏幕的高
     */
    public static int getDeviceHeight() {
        Display screenSize = getScreenSize();
        int deviceHeight = 0;
        if (null != screenSize) {
            deviceHeight = screenSize.getHeight();
        }
        return deviceHeight;

    }





}
