package com.neocampus.wifishared.utils;

import android.net.TrafficStats;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TrafficUtils permet d'éffectuer des actions en relation avec le traffic internet
 */
public class TrafficUtils {

    /**
     * Méthode getTxBytes de la classe {@link TrafficStats},
     * cette méthode n'est pas accessible par une utilisation conventionnelle
     */
    private static Method getTxBytes
            = ClassUtils.getMethod(TrafficStats.class, "getTxBytes");

    /**
     * Méthode getRxBytes de la classe {@link TrafficStats},
     * cette méthode n'est pas accessible par une utilisation conventionnelle
     */
    private static Method getRxBytes
            = ClassUtils.getMethod(TrafficStats.class, "getRxBytes");

    /**
     * Renvoi la quantité de données reçus par l'interface internet wlan0
     * @return quantité de donnée reçus
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
     * Renvoi la quantité de données transmise par l'interface internet wlan0
     * @return quantité de donnée transmise
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
