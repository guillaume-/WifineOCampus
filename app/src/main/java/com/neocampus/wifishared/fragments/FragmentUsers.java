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

public class FragmentUsers extends Fragment implements OnFragmentSetListener,  OnReachableClientListener {

    private View view;
    private TextView totalClientCount;
    private TextView reachableClientCount;
    private ReachableUserView reachableUserView;
    private SessionUserView sessionUserView;

    private OnActivitySetListener mListener;

    public FragmentUsers() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
    }

    @Override
    public void onReachableClients(List<WifiApControl.Client> clients) {
        onRefreshSessionClients(clients);
        onRefreshReachableClients(clients);
    }


    @Override
    public void onRefreshAll() {
        this.mListener.postRequestListClients();
    }


    @Override
    public void onRefreshClient(final WifiApControl.Client client) {
        if(totalClientCount != null && sessionUserView != null) {
            totalClientCount.post(new Runnable() {
                @Override
                public void run() {
                    sessionUserView.setClient(client);
                    totalClientCount.setText("(" + sessionUserView.getCount() + ")");
                }
            });
        }
        if(reachableClientCount != null && reachableUserView != null) {
            reachableClientCount.post(new Runnable() {
                @Override
                public void run() {
                    reachableUserView.setClient(client);
                    reachableClientCount.setText("(" + reachableUserView.getCount() + ")");
                }
            });
        }
    }

    @Override
    public void onRefreshClientCount(final int newCOunt) {
    }

    @Override
    public void onRefreshTimeValue(long newDateValue) {

    }

    @Override
    public void onRefreshHotpostState(HotspotObservable observable) {
        if(!observable.isRunning() && observable.isUPS()) {
            onReachableClients(new ArrayList<WifiApControl.Client>());
        }
    }

    private void onRefreshReachableClients(final List<WifiApControl.Client> clients) {
        if (reachableClientCount != null && reachableUserView != null) {
            reachableClientCount.post(new Runnable() {
                @Override
                public void run() {
                    reachableUserView.changeClients(clients);
                    reachableClientCount.setText("(" + reachableUserView.getCount() + ")");
                }
            });
        }
    }

    private void onRefreshSessionClients(final List<WifiApControl.Client> clients) {
        if(totalClientCount != null && sessionUserView != null) {
            totalClientCount.post(new Runnable() {
                @Override
                public void run() {
                    sessionUserView.changeClients(clients);
                    totalClientCount.setText("(" + sessionUserView.getCount() + ")");
                }
            });
        }
    }

    @Override
    public void onRefreshBatterieLevel(int newLevel) {
    }

    @Override
    public void onRefreshTimeConfig(long newTimeLimit) {
    }

    @Override
    public void onRefreshDataTraffic(long dataTrafficOctet) {
    }


    @Override
    public void onRefreshAllConfig() {
    }

    @Override
    public void onRefreshDataConfig(float newDataLimite) {
    }

    @Override
    public void onRefreshBatterieConfig(int newBatterieLimit) {
    }




}
