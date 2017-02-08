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
import com.neocampus.wifishared.views.ChronoTimeView;

import java.util.Date;

/**
 * FragmentTime est un fragment qui affiche une vue permettant la configuration du seuil du temps d'activation d'une session de partage
 *
 * @see Fragment
 * @see OnFragmentConfigListener
 */
public class FragmentTime extends Fragment implements OnFragmentConfigListener, CompoundButton.OnCheckedChangeListener {

    /**
     * Identifiant de la valeur initiale du temps d'activation
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Identifiant du code de notification
     */
    private static final String ARG_PARAM2 = "param2";

    /**
     * Valeur du seuil du temps d'activation
     */
    private long mTimeLimit;

    /**
     * Code de notification, la notification est active pour la batterie si : code >= 0x0010
     */
    private int notificationCode;

    /**
     * Objet graphique de configuration du temps d'activation
     * @see ChronoTimeView
     */
    private ChronoTimeView timeView;

    /**
     * Interface de communication avec l'activité principale {@link com.neocampus.wifishared.activity.MainActivity}
     * #see {@link OnActivitySetListener}
     */
    private OnActivitySetListener mListener;

    /**
     * Constructeur du fragment
     */
    public FragmentTime() {
        // Required empty public constructor
    }

    /**
     * Crée une instance en initialisant le seuil du temps d'activation
     *
     * @param time seuil initial du temps d'activation
     * @param notificationCode code de notification
     * @return une nouvelle instance de FragmentTime
     */
    public static FragmentTime newInstance(long time, int notificationCode) {
        FragmentTime fragment = new FragmentTime();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, time);
        args.putInt(ARG_PARAM2, notificationCode);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Récupère le seuil initial du temps d'activation
     *
     * @see Fragment#onCreate(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTimeLimit = getArguments().getLong(ARG_PARAM1);
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        this.timeView = (ChronoTimeView) view.findViewById(R.id.timeView);

        this.timeView.setHours((int) (mTimeLimit / (3600*1000)));
        this.timeView.setMinute(new Date(mTimeLimit).getMinutes());

        Switch aSwitch = (Switch) view.findViewById(R.id.switch1);
        aSwitch.setChecked(NotificationUtils.isTimeEnabled(notificationCode));
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
     * @return null
     *
     * @see OnFragmentConfigListener#getLimiteBatterie()
     */
    @Override
    public int getLimiteBatterie() {
        return 0;
    }

    /**
     * @return seuil indiqué par l'utilisateur
     *
     * @see ChronoTimeView#getHours()
     * @see ChronoTimeView#getMinute()
     */
    @Override
    public long getLimiteTemps() {
        return ((timeView.getHours() * 3600)
                + (timeView.getMinute() * 60))*1000;
    }

    /**
     * @see OnFragmentConfigListener#getNotificationCode()
     */
    @Override
    public int getNotificationCode() {
        return notificationCode;
    }

    /**
     * Modifier le code de notification, en cas d'activation ou de déssactivé de notification pour le temps
     * @param buttonView Vue sur laquelle l'utilisateur a cliqué
     * @param isChecked Indique si la notification est activé ou pas
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        notificationCode += NotificationUtils.isTimeEnabled(notificationCode) ?
                -NotificationUtils.NOTIFY_TIME : NotificationUtils.NOTIFY_TIME;
    }
}
