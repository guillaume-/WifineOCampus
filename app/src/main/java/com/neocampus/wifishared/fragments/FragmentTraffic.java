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
import com.neocampus.wifishared.views.DataSurfaceView;

/**
 * FragmentTraffic est un fragment qui affiche une vue permettant la configuration du seuil de consommation de données
 *
 * @see Fragment
 * @see OnFragmentConfigListener
 */
public class FragmentTraffic extends Fragment implements OnFragmentConfigListener {

    /**
     * Identifiant de la valeur initiale de consommation de données
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Valeur du seuil de consommation de données
     */
    private float mLimiteData;

    /**
     * Objet graphique de configuration de consommation de données en GIGA
     * @see DataSurfaceView
     */
    private DataSurfaceView gigaSurfaceView;

    /**
     * Objet graphique de configuration de consommation de données en MEGA
     * @see DataSurfaceView
     */
    private DataSurfaceView megaSurfaceView;

    /**
     * Interface de communication avec l'activité principale {@link com.neocampus.wifishared.activity.MainActivity}
     * #see {@link OnActivitySetListener}
     */
    private OnActivitySetListener mListener;

    /**
     * Constructeur du fragment
     */
    public FragmentTraffic() {
        // Required empty public constructor
    }

    /**
     * Crée une instance en initialisant le seuil de consommation de données
     *
     * @param limite_data seuil de consommation de données
     * @return une nouvelle instance de FragmentTraffic
     */
    public static FragmentTraffic newInstance(float limite_data) {
        FragmentTraffic fragment = new FragmentTraffic();
        Bundle args = new Bundle();
        args.putFloat(ARG_PARAM1, limite_data);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Récupère le seuil initial de consommation de données
     *
     * @see Fragment#onCreate(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mLimiteData = getArguments().getFloat(ARG_PARAM1);
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_config, container, false);
        this.gigaSurfaceView = (DataSurfaceView) view.findViewById(R.id.echelle_surface);
        this.megaSurfaceView = (DataSurfaceView) view.findViewById(R.id.data_configuration);
        this.gigaSurfaceView.setDateType(DataSurfaceView.DATA_TYPE.DATA_GIGA);
        this.gigaSurfaceView.setDataValue(1.0f * (float) Math.floor(mLimiteData) );

        this.megaSurfaceView.setDateType(DataSurfaceView.DATA_TYPE.DATA_MEGA);
        this.megaSurfaceView.setDataValue((float) (mLimiteData - Math.floor(mLimiteData))* 999.9f);

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
     *
     * @return seuil indiqué par l'utilisateur
     *
     * @see DataSurfaceView#getDataValue()
     */
    @Override
    public float getLimiteDataTraffic() {
        float giga = (float) Math.round(this.gigaSurfaceView.getDataValue());
        float mega = (float) Math.round(this.megaSurfaceView.getDataValue());
        return giga + (mega / 1000.f);
    }

    /**
     * @return null
     *
     * @see OnFragmentConfigListener#getLimiteBatterie()
     */
    @Override
    public int getLimiteBatterie() {
        return 0;
    }

    /**
     * @return null
     *
     * @see OnFragmentConfigListener#getLimiteTemps()
     */
    @Override
    public long getLimiteTemps() {
        return 0;
    }


}
