package com.neocampus.wifishared.utils;

import android.net.TrafficStats;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TrafficUtils permet d'�ffectuer des actions en relation avec le traffic internet
 */
public class TrafficUtils {

    /**
     * M�thode getTxBytes de la classe {@link TrafficStats},
     * cette m�thode n'est pas accessible par une utilisation conventionnelle
     */
    private static Method getTxBytes
            = ClassUtils.getMethod(TrafficStats.class, "getTxBytes");

    /**
     * M�thode getRxBytes de la classe {@link TrafficStats},
     * cette m�thode n'est pas accessible par une utilisation conventionnelle
     */
    private static Method getRxBytes
            = ClassUtils.getMethod(TrafficStats.class, "getRxBytes");

    /**
     * Renvoi la quantit� de donn�es re�us par l'interface internet wlan0
     * @return quantit� de donn�e re�us
     */
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

    /**
     * Renvoi la quantit� de donn�es transmise par l'interface internet wlan0
     * @return quantit� de donn�e transmise
     */
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
