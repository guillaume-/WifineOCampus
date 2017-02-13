package com.neocampus.wifishared.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.neocampus.wifishared.observables.TimeObservable;
import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.manage.SQLManager;


/**
 * OnAlarmReceiver permet d'installer une alarme
 * et d'être informé lors de son activation
 */
public class OnAlarmReceiver extends BroadcastReceiver {

    /**
     * Libellé de l'action, permet d'identifier le signal du système
     */
    public static final String ACTION_ALARM_ACTIVATED = "com.neocampus.wifishared.NEOCAMPUS_ALARM_ACTIVATED";

    /**
     * Observable qui détecte les activations de l'alarme et les notifie au {@link java.util.Observer}
     * @see TimeObservable
     */
    private TimeObservable observable;

    /**
     * Construteur de la classe, initialise l'{@link java.util.Observable}
     * @param observable {@link TimeObservable} par défaut
     */
    public OnAlarmReceiver(TimeObservable observable) {
        this.observable = observable;
    }

    /**
     * Cette méthode est appelé lorsque le système android active l'alarme qui a été installé,
     * on modifier la date de la dernière activation
     * @param context contexte de l'application
     * @param intent contient les informations d'identification de l'évènement
     *
     * @see TimeObservable#setDate(long)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        observable.setDate(System.currentTimeMillis());
    }

    /**
     * Supprime l'alarme installé
     * @param context context de l'application
     */
    public void removeAlarm(Context context) {
        Intent intent = new Intent(ACTION_ALARM_ACTIVATED);
        this.observable.setDate(0);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        sender.cancel();
    }

    /**
     * Installe ou modifie une alarme
     * @param context context de l'application
     * @param dateFire date de l'activation de l'alarme
     */
    public void updateAlarm(Context context, long dateFire) {
        try {
            Intent intent = new Intent(ACTION_ALARM_ACTIVATED);
            PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            this.observable.setDate(dateFire);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                am.set(AlarmManager.RTC_WAKEUP, dateFire, pi);
            } else if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                am.setExact(AlarmManager.RTC_WAKEUP, dateFire, pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, dateFire, pi);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, dateFire, pi);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Indique si une alarme est installé
     * @param context context de l'application
     * @return vrai si installé faux sinon
     */
    public static boolean existAlarm(Context context) {
        try {
            Intent intent = new Intent(ACTION_ALARM_ACTIVATED);
            return null != PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_NO_CREATE);
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * Calcul la date d'activation d'une alarme si aucune n'a été sauvegardé dans la base de donnée
     * puis sauvegarde nouvelle date
     * @param context context de l'application
     * @param manager interface de communication avec la base de données
     *
     * @see SQLManager#setConfigurationA(long)
     * @see TableConfiguration#setDateAlarm(long)
     * @see TableConfiguration#getDateAlarm()
     */
    public void startAlarm(Context context, SQLManager manager) {
        TableConfiguration configuration = manager.getConfiguration();
        if (configuration.getDateAlarm() == 0) {
            configuration.setDateAlarm(configuration.getLimiteTemps()
                    + System.currentTimeMillis());
            manager.setConfigurationA(configuration.getDateAlarm());
        }
        updateAlarm(context, configuration.getDateAlarm());
    }

    /**
     * Annule une alarme et supprime la date sauvegardé dans la base de données
     * @param context contexte de l'application
     * @param manager interface de communication avec la base de données
     *
     * @see SQLManager#setConfigurationA(long)
     */
    public void stopAlarm(Context context, SQLManager manager) {
        if (existAlarm(context)) {
            removeAlarm(context);
        }
        manager.setConfigurationA(0);
    }
}
