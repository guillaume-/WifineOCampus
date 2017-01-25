package com.neocampus.wifishared.utils;

import android.net.TrafficStats;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Hirochi ☠ on 24/01/17.
 */

public class TrafficUtils {

    private static Method getTxBytes
            = ClassUtils.getMethod(TrafficStats.class, "getTxBytes");
    private static Method getRxBytes
            = ClassUtils.getMethod(TrafficStats.class, "getRxBytes");

    public static long getRxBytes(){
        try {
            return (long) getRxBytes.invoke(null, "wlan0");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getTxBytes(){
        try {
            return (long) getTxBytes.invoke(null, "wlan0");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
