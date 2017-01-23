package com.neocampus.wifishared.fragments;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FragmentHome extends Fragment
        implements OnFragmentSetListener, OnReachableClientListener {

    private View view;
    private TextView clientCount;
    private TextView batterieLevel;
    private TextView batterieLimite;
    private TextView dataLevel;
    private TextView dataLimite;
    private Button hotSpotButton;

    private OnActivitySetListener mListener;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.mListener.hideAppBarSaveConfig();
        this.mListener.showAppBarRefresh();

        this.view = inflater.inflate(R.layout.fragment_home, container, false);
        this.dataLevel = (TextView) view.findViewById(R.id.data_traffic);
        this.clientCount = (TextView) view.findViewById(R.id.reachable_client_count);
        this.batterieLevel = (TextView) view.findViewById(R.id.batterie_level_result);
        this.batterieLimite = (TextView) view.findViewById(R.id.batterie_level_limit);
        this.dataLimite = (TextView) view.findViewById(R.id.data_limit);
        this.hotSpotButton = (Button) view.findViewById(R.id.button);

        ViewPager viewPager = (ViewPager) this.view.findViewById(R.id.id_view_pager);
        CirclePageIndicator indicator = (CirclePageIndicator) this.view.findViewById(R.id.id_circle_indicator);
        viewPager.setAdapter(new CirclePagerAdapter(viewPager));
        indicator.setViewPager(viewPager);

        onRefreshAll();
        onRefreshAllConfig();

        if(WifiApControl.checkPermission(getContext())) {
            WifiApControl apControl = WifiApControl.getInstance(getContext());
            if (apControl.isEnabled()
                    && apControl.isUPSWifiConfiguration()) {
                hotSpotButton.setText(getString(R.string.desactiver_le_partage));
            }
        }
        else {
            hotSpotButton.setText(getString(R.string.no_permission));
        }
        return view;
    }

    @Override
    public void onReachableClients(List<WifiApControl.Client> clients) {
        onRefreshClientCount(clients.size());
    }


    @Override
    public void onRefreshAll() {
        onRefreshClientCount(0);
        this.mListener.peekListClients();
        onRefreshBatterieLevel(this.mListener.getCurrentBatterieLevel());
    }

    @Override
    public void onRefreshClient(WifiApControl.Client client) {
    }

    @Override
    public void onRefreshClientCount(final int newCount) {
        if (clientCount != null) {
            clientCount.post(new Runnable() {
                @Override
                public void run() {
                    clientCount.setText("(" + newCount + ")");
                }
            });
        }
    }

    @Override
    public void onRefreshHotpostState(boolean activate) {
        if(hotSpotButton != null)
        {
            if(activate) {
                hotSpotButton.setText(getString(R.string.desactiver_le_partage));
            }else{
                onReachableClients(new ArrayList<WifiApControl.Client>());
                hotSpotButton.setText(getString(R.string.activer_le_partage));
            }
        }
    }

    @Override
    public void onRefreshDataTraffic(long dataTrafficOctet) {
        if (dataLevel != null) {
            final String strDataTraffic;
            float dataTraffic = dataTrafficOctet / (1000.0f* 1000.0f*1000.0f);
            if (dataTraffic >= 1.0f) {
                strDataTraffic = String.format(Locale.FRANCE, "%.3f Go", dataTraffic);
            } else {
                strDataTraffic = String.format(Locale.FRANCE, "%d Mo", (int) (dataTraffic * 1000.f));
            }
            dataLevel.post(new Runnable() {
                @Override
                public void run() {
                    dataLevel.setText(strDataTraffic);
                }
            });
        }
    }



    @Override
    public void onRefreshBatterieLevel(int newBatterieLevel) {
        if (batterieLevel != null) {
            int batterieLimit = this.mListener.getLimiteBatterieLevel();
            batterieLevel.setText(String.format(Locale.FRANCE, "%d %% ", newBatterieLevel - batterieLimit));
        }
    }

    @Override
    public void onRefreshAllConfig() {
        onRefreshDataConfig(this.mListener.getLimiteDataTrafic());
        onRefreshBatterieConfig(this.mListener.getLimiteBatterieLevel());
    }

    @Override
    public void onRefreshBatterieConfig(int newBatterieLimit) {
        if (batterieLimite != null) {
            batterieLimite.setText(String.format(Locale.FRANCE, "%d %% ", newBatterieLimit));
        }
    }

    @Override
    public void onRefreshDataConfig(float newDataLimit) {
        if (dataLimite != null) {
            String limiteData;
            if (newDataLimit >= 1.0f) {
                limiteData = String.format(Locale.FRANCE, "%.3f Go", newDataLimit);
            } else {
                limiteData = String.format(Locale.FRANCE, "%d Mo", (int) (newDataLimit * 1000.f));
            }
            dataLimite.setText(limiteData);
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


}
