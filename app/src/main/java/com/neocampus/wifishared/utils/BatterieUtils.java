package com.neocampus.wifishared.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * BatterieUtils permet d'éffectuer des actions en relation avec la batterie
 */
public class BatterieUtils {

    /**
     * Limite par défaut du niveau de la batterie
     */
    public static int BATTERIE_DEFAULT_LIMIT = 30;

    /**
     * Renvoi le niveau actuel de la batterie
     * @param context contexte de l'application
     * @return niveau de la batterie
     */
    public static float getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return ((float)level / (float)scale) * 100.0f;
    }
}
