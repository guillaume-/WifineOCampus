package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentSetListener;
import com.neocampus.wifishared.listeners.OnReachableClientListener;
import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.utils.WifiApControl;
import com.neocampus.wifishared.views.CirclePageIndicator;
import com.neocampus.wifishared.views.CirclePagerAdapter;
import com.neocampus.wifishared.views.ReachableUserView;
import com.neocampus.wifishared.views.SessionUserView;

import java.util.ArrayList;
import java.util.List;

/**
 * FragmentSession est un fragment qui affiche une vue contenant les clients actuellements connéctés,
 * ainsi qu'une historique de connexions pour la session de partage active
 *
 * @see Fragment
 */
public class FragmentUsers extends Fragment implements OnFragmentSetListener,  OnReachableClientListener {

    /**
     * Vue affiché par le fragment
     */
    private View view;

    /**
     * Objet graphique qui affiche le nombre total de client connecté depuis le lancement de la session de partage
     */
    private TextView totalClientCount;

    /**
     * Objet graphique qui affiche le nombre de client connecté
     */
    private TextView reachableClientCount;

    /**
     * Objet graphique qui affiche les informations tous les clients connectés
     */
    private ReachableUserView reachableUserView;

    /**
     * Objet graphique qui affiche l'historique de connexions de la session de partage active
     */
    private SessionUserView sessionUserView;

    /**
     * Interface de communication avec l'activité principale {@link com.neocampus.wifishared.activity.MainActivity}
     * #see {@link OnActivitySetListener}
     */
    private OnActivitySetListener mListener;
    private boolean added;

    /**
     * Constructeur du fragment
     */
    public FragmentUsers() {
        // Required empty public constructor
    }

    /**
     * Crée la vue affiché par le fragment
     *
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.mListener.hideAppBarSaveConfig();
        this.mListener.showAppBarRefresh();

        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_users, container, false);
        this.totalClientCount = (TextView) view.findViewById(R.id.total_client_count);
        this.reachableClientCount = (TextView) view.findViewById(R.id.reachable_client_count);
        this.reachableUserView = (ReachableUserView) view.findViewById(R.id.reachable_users);
        this.sessionUserView = (SessionUserView) view.findViewById(R.id.session_users);

        ViewPager viewPager = (ViewPager) this.view.findViewById(R.id.id_view_pager);
        CirclePageIndicator indicator = (CirclePageIndicator) this.view.findViewById(R.id.id_circle_indicator);
        viewPager.setAdapter(new CirclePagerAdapter(viewPager));
        indicator.setViewPager(viewPager);

        onRefreshAll();

        return view;
    }

    /**
     *
     * @param context Context de l'application
     *
     * @see Fragment#onAttach(Context)
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivitySetListener) {
            mListener = (OnActivitySetListener) context;
            added = true;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnActivitySetListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        added = false;
    }

    /**
     * Reçoit la liste des clients connéctés
     * @param clients liste des clients connéctés
     */
    @Override
    public void onReachableClients(List<WifiApControl.Client> clients) {
        onRefreshSessionClients(clients);
        onRefreshReachableClients(clients);
    }


    /**
     * Rafraichit les objets graphiques de la vue
     *
     * @see OnFragmentSetListener#onRefreshAll()
     */
    @Override
    public void onRefreshAll() {
        this.mListener.postRequestListClients();
    }

    /**
     * @see
     */
    /**
     * Rafraichit le nombre de clients connectés, suite à la connexion ou déconnexion d'un client
     * @param client information du client
     * @see OnFragmentSetListener#onRefreshClient(WifiApControl.Client)
     */
    @Override
    public void onRefreshClient(final WifiApControl.Client client) {
        if(totalClientCount != null && sessionUserView != null && added) {
            totalClientCount.post(new Runnable() {
                @Override
                public void run() {
                    sessionUserView.setClient(client);
                    totalClientCount.setText("(" + sessionUserView.getCount() + ")");
                }
            });
        }
        if(reachableClientCount != null && reachableUserView != null && added) {
            reachableClientCount.post(new Runnable() {
                @Override
                public void run() {
                    reachableUserView.setClient(client);
                    reachableClientCount.setText("(" + reachableUserView.getCount() + ")");
                }
            });
        }
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshClientCount(final int newCOunt) {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshTimeValue(long newDateValue) {

    }

    /**
     * Remet à zéro le nombre de client connecté en cas d'arrêt de la session de partage actuelle
     * @param observable Observateur de l'état du Wi-FI AP
     *
     * @see OnFragmentSetListener#onRefreshHotpostState(HotspotObservable)
     */
    @Override
    public void onRefreshHotpostState(HotspotObservable observable) {
        if(!observable.isRunning() && observable.isUPS()) {
            onReachableClients(new ArrayList<WifiApControl.Client>());
        }
    }

    /**
     * Rafraichit la liste des clients connectés
     * @param clients nouvelle liste de connectés
     */
    private void onRefreshReachableClients(final List<WifiApControl.Client> clients) {
        if (reachableClientCount != null && reachableUserView != null && added) {
            reachableClientCount.post(new Runnable() {
                @Override
                public void run() {
                    reachableUserView.changeClients(clients);
                    reachableClientCount.setText("(" + reachableUserView.getCount() + ")");
                }
            });
        }
    }

    /**
     * Rafraichit l'historique des clients connectés depuis le lancement de la session de partage actuelle
     * @param clients nouvelle liste de connectés
     */
    private void onRefreshSessionClients(final List<WifiApControl.Client> clients) {
        if(totalClientCount != null && sessionUserView != null && added) {
            totalClientCount.post(new Runnable() {
                @Override
                public void run() {
                    sessionUserView.changeClients(clients);
                    totalClientCount.setText("(" + sessionUserView.getCount() + ")");
                }
            });
        }
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshBatterieLevel(int newLevel) {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshTimeConfig(long newTimeLimit) {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshDataTraffic(long dataTrafficOctet) {
    }


    /**
     * Do nothing
     */
    @Override
    public void onRefreshAllConfig() {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshDataConfig(float newDataLimite) {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshBatterieConfig(int newBatterieLimit) {
    }

}
