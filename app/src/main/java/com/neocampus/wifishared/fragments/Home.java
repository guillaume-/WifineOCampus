package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentSetListener;
import com.neocampus.wifishared.listeners.OnReachableClientListener;
import com.neocampus.wifishared.utils.WifiApControl;
import com.neocampus.wifishared.views.CirclePageIndicator;
import com.neocampus.wifishared.views.CirclePagerAdapter;

import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentSetListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements OnFragmentSetListener, OnReachableClientListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private TextView batterieLevel,
                     batterieLimite,
                     dataLevel,
                     dataLimite;
    private long dataT0;
    private boolean isDataUsable;

    private OnActivitySetListener mListener;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        batterieLevel = (TextView) view.findViewById(R.id.batterie_level_result);
        batterieLimite = (TextView) view.findViewById(R.id.batterie_level_limit);
        dataLevel = (TextView) view.findViewById(R.id.data_level);
        dataLimite = (TextView) view.findViewById(R.id.data_limit);

        ViewPager viewPager = (ViewPager) this.view.findViewById(R.id.id_view_pager);
        CirclePageIndicator indicator = (CirclePageIndicator) this.view.findViewById(R.id.id_circle_indicator);
        viewPager.setAdapter(new CirclePagerAdapter(viewPager));
        indicator.setViewPager(viewPager);

        List<WifiApControl.Client> result = this.mListener.getReachableClients(this);
        if(result.isEmpty()) {
            TextView textView = (TextView) view.findViewById(R.id.iDClientCount);
            textView.setText("(0)");
        }

        int batterie_level = this.mListener.getCurrentBatterieLevel();
        int batterie_limite_level = this.mListener.getLimiteBatterieLevel();
        //batterieLimite.setText(String.format(Locale.FRANCE, "%d %% ", batterie_limite_level));
        //batterieLevel.setText(String.format(Locale.FRANCE, "%d %% ", batterie_level - batterie_limite_level));
        batterieLimite.setText("?");
        batterieLevel.setText("?");
        isDataUsable = (TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED);
        if(isDataUsable) {
            dataT0 = TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes();
            dataLevel.setText("0 octet");
            dataLimite.setText("Non défini");
        } else {
            dataLevel.setText("Non supporté");
            dataLimite.setText("Non supporté");
        }

        if (WifiApControl.checkPermission(getContext(), true)) {
            WifiApControl apControl = WifiApControl.getInstance(getContext());
            WifiConfiguration
                    configuration = apControl.getWifiApConfiguration();
            WifiConfiguration upsConfig = WifiApControl.getUPSWifiConfiguration();
            if (WifiApControl.equals(configuration, upsConfig))
            {
                if(apControl.isEnabled()) {
                    Button button = (Button) view.findViewById(R.id.button);
                    button.setText("Arrêter le Partage");
                }
            }
        }
        return view;
    }

    @Override
    public void onReachableClient(WifiApControl.Client c) {
    }

    @Override
    public void onReachableClients(final List<WifiApControl.Client> clients) {
        if(view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) view.findViewById(R.id.iDClientCount);
                    textView.setText("(" + clients.size() + ")");
                }
            });
        }
    }


    @Override
    public void onRefreshNotify() {
        if(view != null) {
            List<WifiApControl.Client> result = this.mListener.getReachableClients(this);
            if (result.isEmpty()) {
                TextView textView = (TextView) view.findViewById(R.id.iDClientCount);
                textView.setText("(0)");
            }
            int batterie_level = this.mListener.getCurrentBatterieLevel();
            int batterie_limite_level = this.mListener.getLimiteBatterieLevel();
            //batterieLimite.setText(String.format(Locale.FRANCE, "%d %% ", batterie_limite_level));
            //batterieLevel.setText(String.format(Locale.FRANCE, "%d %% ", batterie_level - batterie_limite_level));
            if(isDataUsable) {
                long dataTx = TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes();
                dataLevel.setText(dataToStr(dataTx-dataT0));
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivitySetListener) {
            mListener = (OnActivitySetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnActivitySetListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String dataToStr(long data){
        if(data > 1000000000)
            return ""+(data/1000000000)+" Go";
        else if(data > 1000000)
            return ""+(data/1000000)+" Mo";
        else if(data > 1000)
            return ""+(data/1000)+" Ko";
        else if(data > 1)
            return ""+(data)+" octets";
        return ""+(data)+" octet";
    }
}
