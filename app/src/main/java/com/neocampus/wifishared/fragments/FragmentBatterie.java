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
import com.neocampus.wifishared.views.BatterieSurfaceView;

/**
 * FragmentBatterie est un fragment qui affiche une vue permettant la configuration du seuil de la batterie
 *
 * @see Fragment
 * @see OnFragmentConfigListener
 */
public class FragmentBatterie extends Fragment implements OnFragmentConfigListener, CompoundButton.OnCheckedChangeListener {

    /**
     * Identifiant de la valeur initiale de la batterie
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Identifiant du code de notification
     */
    private static final String ARG_PARAM2 = "param2";

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
     * Code de notification, la notification est active pour la batterie si : code >= 0x1000
     */
    private int notificationCode;

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
     * @param notificationCode code de notification
     * @return une nouvelle instance de FragmentBatterie
     */
    public static FragmentBatterie newInstance(int batterie, int notificationCode) {
        FragmentBatterie fragment = new FragmentBatterie();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, batterie);
        args.putInt(ARG_PARAM2, notificationCode);
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
            notificationCode = getArguments().getInt(ARG_PARAM2);
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

        Switch aSwitch = (Switch) view.findViewById(R.id.switch1);
        aSwitch.setChecked(NotificationUtils.isBatterieEnabled(notificationCode));
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

    /**
     * @see OnFragmentConfigListener#getNotificationCode()
     */
    @Override
    public int getNotificationCode() {
        return notificationCode;
    }

    /**
     * Modifier le code de notification, en cas d'activation ou de déssactivé de notification pour la batterie
     * @param buttonView Vue sur laquelle l'utilisateur a cliqué
     * @param isChecked Indique si la notification est activé ou pas
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        notificationCode +=  NotificationUtils.isBatterieEnabled(notificationCode) ?
                -NotificationUtils.NOTIFY_BATTERIE : NotificationUtils.NOTIFY_BATTERIE;
    }
}
