package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.neocampus.wifishared.services.ServiceNeOCampus;

/**
 * OnBootCompleted permet de d'effectu� une action lors du d�marrage du syst�me android
 */
public class OnBootCompleted extends BroadcastReceiver {

    /**
     * Constructeur de la classe
     */
    public OnBootCompleted() {
    }

    /**
     * Cette m�thode est appel� lorsque le syst�me android d�marre,
     * on lance le service {@link ServiceNeOCampus}
     * @param context contexte de l'application
     * @param intent contient les informations d'identification de l'�v�nement
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServiceNeOCampus.class));
    }
}
