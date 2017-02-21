package com.neocampus.wifishared.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkUtils permet d'�ffectuer des actions en relation avec la connexion internet
 */
public class NetworkUtils {

    /**
     * Renvoi si l'internet mobile est activ�
     * @param context contexte de l'application
     * @return vrai si internet mobile est activ�, faux sinon
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected()
                && activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

}
