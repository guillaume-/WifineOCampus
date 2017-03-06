package com.neocampus.wifishared.listeners;

import android.view.View;

/**
 * OnAdapterViewListener permet de d�finir l'affichage d'un �l�ment d'une listView
 *
 * @author Hirochi ?
 */
public interface OnAdapterViewListener {

    /**
     * Cette m�thode est appel� pour afficher un �l�ment
     * @param view conteneur graphique
     * @param o �l�ment a affiche
     * @return conteneur graphique trait�
     */
   View showView(View view, Object o);
}
