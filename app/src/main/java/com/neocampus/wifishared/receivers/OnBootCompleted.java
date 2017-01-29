package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.neocampus.wifishared.services.ServiceNeOCampus;

public class OnBootCompleted extends BroadcastReceiver {

    public static final String ACTION_START_SERVICE = "com.neocampus.wifishared.START_SERVICE";

    public OnBootCompleted() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServiceNeOCampus.class));
    }
}
