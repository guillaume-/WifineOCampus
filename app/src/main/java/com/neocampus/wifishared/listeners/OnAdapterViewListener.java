package com.neocampus.wifishared.listeners;

import android.view.View;

/**
 * OnAdapterViewListener permet de définir l'affichage d'un élément d'une listView
 *
 * @author Hirochi ☠
 */
public interface OnAdapterViewListener {

    /**
     * Cette méthode est appelé pour afficher un élément
     * @param view conteneur graphique
     * @param o élément a affiche
     * @return conteneur graphique traité
     */
   View showView(View view, Object o);
}
