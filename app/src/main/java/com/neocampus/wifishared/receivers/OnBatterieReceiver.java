package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.neocampus.wifishared.observables.BatterieObservable;

/**
 * Created by JAMAA on 18/01/17.
 */

public class OnBatterieReceiver extends BroadcastReceiver {

    private BatterieObservable observable;

    public OnBatterieReceiver(BatterieObservable observable) {
        this.observable = observable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int max_level = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryLevel = (level / (float)max_level) * 100.0f;
        this.observable.setValue((int) batteryLevel);
    }

}
