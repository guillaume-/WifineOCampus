package com.neocampus.wifishared.utils;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TimingLogger;

import java.util.HashMap;

/**
 * Created by Hirochi on 15/10/2016.
 * adb shell setprop log.tag.TimingLogger VERBOSE
 */

public class TimeLog {

    public final static boolean Debug = true;
    private final static String TAG = "TimingLogger";
    private static HashMap<String, TimingLogger> hashMap = new HashMap<>();

    public static void mark(@NonNull String mark) {
        if (Debug) {
            try {
                String method = getMethodName(1);
                if (hashMap.containsKey(method)) {
                    hashMap.get(method).addSplit("["+mark+"]");
                }
            } catch (Exception e) {
            }
        }
    }

    public static void begin() {
        if (Debug) {
            try {
                String method = getMethodName(1);
                Log.isLoggable(TAG, Log.VERBOSE);
                if (!hashMap.containsKey(method)) {
                    hashMap.put(method, new TimingLogger(TAG, method));
                }
            } catch (Exception e) {
            }
        }
    }

    public static void end() {
        if (Debug) {
            try {
                String method = getMethodName(1);
                if (hashMap.containsKey(method)) {
                    Log.i(TAG, "-----------------------------------------------------\n");
                    hashMap.get(method).addSplit("");
                    hashMap.get(method).dumpToLog();
                    Log.i(TAG, "-----------------------------------------------------\n");
                    hashMap.remove(method);
                }
            } catch (Exception e) {
            }
        }
    }

    private static String getMethodName(final int index) throws Exception {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        if (ste.length <= (index + 3))
            throw new Exception("Index trop grand");
        return ste[index + 3].getMethodName();
    }


}
