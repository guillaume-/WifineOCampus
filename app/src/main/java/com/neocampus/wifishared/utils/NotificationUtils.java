package com.neocampus.wifishared.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.observables.BatterieObservable;
import com.neocampus.wifishared.observables.DataObservable;

import java.util.Locale;

/**
 * Created by Hirochi ☠ on 25/01/17.
 */

public class NotificationUtils {
    public static final int ID_NOTIFICATION
            = Math.abs("Wifi neOCampus".hashCode());

    public static void showDataNotify(Context context, DataObservable observable)
    {
        String data;
        float dataTraffic = observable.getValue() / (1000.0f* 1000.0f*1000.0f);
        if (dataTraffic >= 1.0f) {
            data = String.format(Locale.FRANCE, "%.3f Go", dataTraffic);
        } else {
            data = String.format(Locale.FRANCE, "%.2f Mo", (dataTraffic * 1000.f));
        }
        show(context,"Le partage Wi-Fi s'est arrêté", "La consommation de données a atteint "+data);
    }

    public static void showBatterieNotify(Context context, BatterieObservable observable)
    {
        show(context,"Le partage Wi-Fi s'est arrêté",
                "Le niveau de la batterie a atteint "+observable.getValue()+"%");
    }

    public static void showTimeNotify(Context context)
    {
        show(context, "Le partage Wi-Fi s'est arrêté", "Le temps de partage s'est écoulé");
    }

    public static void show(Context context, String titleText, String textContent)
    {
        Notification.BigTextStyle bigText = new Notification.BigTextStyle();
        bigText.bigText(titleText);
        bigText.setBigContentTitle(context.getString(R.string.app_name));
        bigText.setSummaryText(textContent);

        int property = Notification.DEFAULT_LIGHTS;
//        property |= Notification.DEFAULT_SOUND;

        Notification.Builder mBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_cloche)
                .setAutoCancel(false)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(titleText)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setStyle(bigText)
                .setDefaults(property);

        Notification notification = mBuilder.build();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ID_NOTIFICATION, notification);
    }
}
