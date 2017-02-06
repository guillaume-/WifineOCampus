package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentConfigListener;
import com.neocampus.wifishared.views.BatterieSurfaceView;

/**
 * FragmentBatterie est un fragment qui affiche une vue permettant la configuration du seuil de la batterie
 *
 * @see Fragment
 * @see OnFragmentConfigListener
 */
public class FragmentBatterie extends Fragment implements OnFragmentConfigListener {

    /**
     * Identifiant de la valeur initiale de la batterie
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Objet graphique de configuration de la batterie
     * @see BatterieSurfaceView
     */
    private BatterieSurfaceView surfaceView;

    /**
     * Valeur du seuil de la batterie
     */
    private int mBatterieLimit;

    /**
     * Interface de communication avec l'activité principale {@link com.neocampus.wifishared.activity.MainActivity}
     * #see {@link OnActivitySetListener}
     */
    private OnActivitySetListener mListener;

    /**
     * Constructeur du fragment
     */
    public FragmentBatterie() {
        // Required empty public constructor
    }

    /**
     * Crée une instance en initialisant le seuil de la batterie
     *
     * @param batterie seuil initial de la batterie
     * @return une nouvelle instance de FragmentBatterie
     */
    public static FragmentBatterie newInstance(int batterie) {
        FragmentBatterie fragment = new FragmentBatterie();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, batterie);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Récupère le seuil initial de la batterie
     *
     * @see Fragment#onCreate(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBatterieLimit = getArguments().getInt(ARG_PARAM1);
        }
    }

    /**
     * Crée la vue affiché par le fragment
     *
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mListener.hideAppBarRefresh();
        this.mListener.showAppBarSaveConfig();

        View view = inflater.inflate(R.layout.fragment_batterie, container, false);
        surfaceView = (BatterieSurfaceView) view.findViewById(R.id.batterie_configuration);
        surfaceView.setLimiteBatterie(mBatterieLimit);
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
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * @see Fragment#onDetach()
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * @return null
     *
     * @see OnFragmentConfigListener#getLimiteDataTraffic()
     */
    @Override
    public float getLimiteDataTraffic() {
        return 0;
    }

    /**
     * @return seuil indiqué par l'utilisateur
     *
     * @see BatterieSurfaceView#getLimiteBatterie()
     * @see OnFragmentConfigListener#getLimiteBatterie()
     */
    @Override
    public int getLimiteBatterie() {
        return this.surfaceView.getLimiteBatterie();
    }

    /**
     * @return null
     * @see OnFragmentConfigListener#getLimiteTemps()
     */
    @Override
    public long getLimiteTemps() {
        return 0;
    }
}
