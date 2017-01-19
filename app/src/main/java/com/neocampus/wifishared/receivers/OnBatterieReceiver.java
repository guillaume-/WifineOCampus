package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

/**
 * Created by JAMAA on 18/01/17.
 */

public class OnBatterieReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int max_level = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryLevel = (level / (float)max_level) * 100.0f;
        System.out.print("Batterie changed : "+ batteryLevel);
    }

}
