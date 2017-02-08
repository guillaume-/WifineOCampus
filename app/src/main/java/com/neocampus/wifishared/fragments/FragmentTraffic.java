package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentConfigListener;
import com.neocampus.wifishared.utils.NotificationUtils;
import com.neocampus.wifishared.views.DataSurfaceView;

/**
 * FragmentTraffic est un fragment qui affiche une vue permettant la configuration du seuil de consommation de données
 *
 * @see Fragment
 * @see OnFragmentConfigListener
 */
public class FragmentTraffic extends Fragment implements OnFragmentConfigListener, CompoundButton.OnCheckedChangeListener {

    /**
     * Identifiant de la valeur initiale de consommation de données
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Identifiant du code de notification
     */
    private static final String ARG_PARAM2 = "param2";

    /**
     * Valeur du seuil de consommation de données
     */
    private float mLimiteData;

    /**
     * Code de notification, la notification est active pour la batterie si : code >= 0x0100
     */
    private int notificationCode;

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
     * @param notificationCode code de notification
     * @return une nouvelle instance de FragmentTraffic
     */
    public static FragmentTraffic newInstance(float limite_data, int notificationCode) {
        FragmentTraffic fragment = new FragmentTraffic();
        Bundle args = new Bundle();
        args.putFloat(ARG_PARAM1, limite_data);
        args.putInt(ARG_PARAM2, notificationCode);
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
            this.notificationCode = getArguments().getInt(ARG_PARAM2);
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

        Switch aSwitch = (Switch) view.findViewById(R.id.switch1);
        aSwitch.setChecked(NotificationUtils.isDataEnabled(notificationCode));
        aSwitch.setOnCheckedChangeListener(this);

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

    /**
     * @see OnFragmentConfigListener#getNotificationCode()
     */
    @Override
    public int getNotificationCode() {
        return notificationCode;
    }

    /**
     * Modifier le code de notification, en cas d'activation ou de déssactivé de notification pour le traffic
     * @param buttonView Vue sur laquelle l'utilisateur a cliqué
     * @param isChecked Indique si la notification est activé ou pas
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        notificationCode += NotificationUtils.isDataEnabled(notificationCode) ?
                -NotificationUtils.NOTIFY_DATA : NotificationUtils.NOTIFY_DATA;
    }


}
