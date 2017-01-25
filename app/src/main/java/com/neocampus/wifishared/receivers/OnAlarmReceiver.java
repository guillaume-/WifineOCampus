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
 * Created by Hirochi ☠ on 25/01/17.
 */

public class OnAlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_ALARM_ACTIVATED = "com.neocampus.wifishared.NEOCAMPUS_ALARM_ACTIVATED";

    private TimeObservable observable;

    public OnAlarmReceiver(TimeObservable observable) {
        this.observable = observable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        observable.setDate(System.currentTimeMillis());
    }

    public void removeAlarm(Context context) {
        Intent intent = new Intent(ACTION_ALARM_ACTIVATED);
        this.observable.setDate(0);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        sender.cancel();
    }

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

    public static boolean existAlarm(Context context) {
        try {
            Intent intent = new Intent(ACTION_ALARM_ACTIVATED);
            return null != PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_NO_CREATE);
        } catch (Exception e) {

        }
        return false;
    }

    public void startAlarm(Context context, SQLManager manager) {
        TableConfiguration configuration = manager.getConfiguration();
        if (configuration.getDateAlarm() == 0) {
            configuration.setDateAlarm(configuration.getLimiteTemps()
                    + System.currentTimeMillis());
            manager.setConfigurationE(configuration.getDateAlarm());
        }
        updateAlarm(context, configuration.getDateAlarm());
    }

    public void stopAlarm(Context context, SQLManager manager) {
        if (existAlarm(context)) {
            removeAlarm(context);
        }
        manager.setConfigurationE(0);
    }
}
