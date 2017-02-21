package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.neocampus.wifishared.observables.NetworkObservable;
import com.neocampus.wifishared.utils.NetworkUtils;

/**
 * OnNetworkReceiver permet d'�tre inform� par le syst�me android lors du changement de l'�tat de la connexion internet,
 * et notifie le nouvel �tat au {@link java.util.Observer}
 */
public class OnNetworkReceiver extends BroadcastReceiver {

    /**
     * Observable qui d�tecte et notifie aux {@link java.util.Observer} le nouvel �tat
     */
    private NetworkObservable observable;

    /**
     * Constructeur de la classe, initialise l'{@link java.util.Observable}
     * @param observable {@link NetworkObservable} par d�faut
     */
    public OnNetworkReceiver(NetworkObservable observable) {
        this.observable = observable;
    }

    /**
     * Cette m�thode est appel� lorsque le syst�me android notifie le changement de l'�tat de la connexion internet,
     * on modifier l'�tat de connexion internet mobile
     * @param context contexte de l'application
     * @param intent contient les informations d'identification de l'�v�nement
     *
     * @see NetworkObservable#setEnabled(boolean)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        observable.setEnabled(NetworkUtils.isNetworkAvailable(context));
    }
}
