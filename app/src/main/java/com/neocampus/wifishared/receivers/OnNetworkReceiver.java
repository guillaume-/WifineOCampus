package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.neocampus.wifishared.observables.NetworkObservable;
import com.neocampus.wifishared.utils.NetworkUtils;

/**
 * Created by Hirochi ☠ on 06/02/17.
 */

public class OnNetworkReceiver extends BroadcastReceiver {

    private NetworkObservable observable;

    public OnNetworkReceiver(NetworkObservable observable) {
        this.observable = observable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        observable.setEnabled(NetworkUtils.isNetworkAvailable(context));
    }
}
