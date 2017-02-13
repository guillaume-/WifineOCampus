package com.neocampus.wifishared.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.observables.BatterieObservable;
import com.neocampus.wifishared.observables.DataObservable;

import java.util.Locale;

/**
 * NotificationUtils permet d'éffectuer des actions en relation avec la notification
 */
public class NotificationUtils {
    /**
     * Identifiant de notification de l'application
     */
    public static final int ID_NOTIFICATION
            = Math.abs("Wifi neOCampus".hashCode());

    /**
     * Code de notification de la batterie
     */
    public static final int NOTIFY_BATTERIE = 0x1000;

    /**
     * Code de notification de la consommation de données
     */
    public static final int NOTIFY_DATA = 0x0100;

    /**
     * Code de notification du temps
     */
    public static final int NOTIFY_TIME = 0x0010;

    /**
     * Cette méthode affiche une notification,
     * si le partage wifi s'arrête sur franchissement du seuil de consommation de données
     * @param context contexte de l'application
     * @param observable contient la consommation de données
     */
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

    /**
     * Cette méthode affiche une notification,
     * si le partage wifi s'arrête sur franchissement du seuil de la batterie
     * @param context contexte de l'application
     * @param observable contient le niveau de la batterie
     */
    public static void showBatterieNotify(Context context, BatterieObservable observable)
    {
        show(context,"Le partage Wi-Fi s'est arrêté",
                "Le niveau de la batterie a atteint "+observable.getValue()+"%");
    }

    /**
     * Cette méthode affiche une notification,
     * si le partage wifi s'arrête sur franchissement du temps d'activation
     * @param context contexte de l'application
     */
    public static void showTimeNotify(Context context)
    {
        show(context, "Le partage Wi-Fi s'est arrêté", "Le temps de partage s'est écoulé");
    }

    /**
     * Cette méthode affiche une notification,
     * si le partage wifi s'arrête lors de la désactivation d'internet mobile
     * @param context contexte de l'application
     */
    public static void showNetworkNotify(Context context) {
        show(context, "Le partage Wi-Fi s'est arrêté", "Impossible de se connecter à internet");
    }

    /**
     * Affiche une notification
     * @param context contexte de l'application
     * @param titleText titre de la notification
     * @param textContent texte d'information de la notification
     */
    public static void show(Context context, String titleText, String textContent)
    {
        Notification.BigTextStyle bigText = new Notification.BigTextStyle();
        bigText.bigText(titleText);
        bigText.setBigContentTitle(context.getString(R.string.app_name));
        bigText.setSummaryText(textContent);

        int property = Notification.DEFAULT_LIGHTS;
        property |= Notification.DEFAULT_SOUND;

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

    /**
     * Vérifie si le code de notification de la batterie est présent dans le code de notification
     * @param notificationCode code de notification
     * @return vrai si la notification batterie est activé, faux sinon
     */
    public static boolean isBatterieEnabled(int notificationCode)
    {
        return (notificationCode & NOTIFY_BATTERIE) == NOTIFY_BATTERIE;
    }

    /**
     * Vérifie si le code de notification du temps est présent dans le code de notification
     * @param notificationCode code de notification
     * @return vrai si la notification temps est activé, faux sinon
     */
    public static boolean isTimeEnabled(int notificationCode)
    {
        return (notificationCode & NOTIFY_TIME) == NOTIFY_TIME;
    }

    /**
     * Vérifie si le code de notification de consommation de données est présent dans le code de notification
     * @param notificationCode code de notification
     * @return vrai si la notification consommation est activé, faux sinon
     */
    public static boolean isDataEnabled(int notificationCode)
    {
        return (notificationCode & NOTIFY_DATA) == NOTIFY_DATA;
    }
}
