package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.neocampus.wifishared.observables.NetworkObservable;
import com.neocampus.wifishared.utils.NetworkUtils;

/**
 * OnNetworkReceiver permet d'être informé par le système android lors du changement de l'état de la connexion internet,
 * et notifie le nouvel état au {@link java.util.Observer}
 */
public class OnNetworkReceiver extends BroadcastReceiver {

    /**
     * Observable qui détecte et notifie aux {@link java.util.Observer} le nouvel état
     */
    private NetworkObservable observable;

    /**
     * Constructeur de la classe, initialise l'{@link java.util.Observable}
     * @param observable {@link NetworkObservable} par défaut
     */
    public OnNetworkReceiver(NetworkObservable observable) {
        this.observable = observable;
    }

    /**
     * Cette méthode est appelé lorsque le système android notifie le changement de l'état de la connexion internet,
     * on modifier l'état de connexion internet mobile
     * @param context contexte de l'application
     * @param intent contient les informations d'identification de l'évènement
     *
     * @see NetworkObservable#setEnabled(boolean)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        observable.setEnabled(NetworkUtils.isNetworkAvailable(context));
    }
}
