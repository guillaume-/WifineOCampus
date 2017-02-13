package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.neocampus.wifishared.services.ServiceNeOCampus;

/**
 * OnBootCompleted permet de d'effectué une action lors du démarrage du système android
 */
public class OnBootCompleted extends BroadcastReceiver {

    /**
     * Constructeur de la classe
     */
    public OnBootCompleted() {
    }

    /**
     * Cette méthode est appelé lorsque le système android démarre,
     * on lance le service {@link ServiceNeOCampus}
     * @param context contexte de l'application
     * @param intent contient les informations d'identification de l'évènement
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServiceNeOCampus.class));
    }
}
