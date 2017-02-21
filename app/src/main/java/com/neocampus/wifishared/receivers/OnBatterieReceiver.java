package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.neocampus.wifishared.observables.BatterieObservable;

/**
 * OnBatterieReceiver permet d'�tre inform� par le syst�me android lors du changement du niveau de la batterie,
 * et notifie la nouvelle valeur au {@link java.util.Observer}
 */
public class OnBatterieReceiver extends BroadcastReceiver {

    /**
     * Observable qui d�tecte et notifie aux {@link java.util.Observer} la nouvelle valeur
     */
    private BatterieObservable observable;

    /**
     * Constructeur de la classe, initialise l'{@link java.util.Observable}
     * @param observable {@link BatterieObservable} par d�faut
     */
    public OnBatterieReceiver(BatterieObservable observable) {
        this.observable = observable;
    }

    /**
     * Cette m�thode est appel� lorsque le syst�me android notifie le changement du niveau de la batterie,
     * on modifier le niveau de la batterie
     * @param context contexte de l'application
     * @param intent contient les informations d'identification de l'�v�nement
     *
     * @see BatterieObservable#setValue(int)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int max_level = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryLevel = (level / (float)max_level) * 100.0f;
        this.observable.setValue((int) batteryLevel);
    }

}
