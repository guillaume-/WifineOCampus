package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.views.SessionHistoView;
import com.neocampus.wifishared.views.UsersHistoView;

import java.util.Locale;

public class FragmentSession extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private OnActivitySetListener mListener;
    private ViewSwitcher simpleViewSwitcher;
    private UsersHistoView histoView;
    private TextView titleView;

    public FragmentSession() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mListener.hideAppBarSaveConfig();
        this.mListener.hideAppBarRefresh();
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_session, container, false);

        simpleViewSwitcher = (ViewSwitcher) view.findViewById(R.id.simpleViewSwitcher);
        SessionHistoView sessionHisto = (SessionHistoView) view.findViewById(R.id.session_historique);
        ListView lvItems = (ListView) sessionHisto.findViewById(R.id.lvClients);
        lvItems.setOnItemClickListener(this);
        sessionHisto.changeSessions(mListener.getAllConsommations());

        TextView backView = (TextView) view.findViewById(R.id.consommation_back);
        backView.setOnClickListener(this);
        titleView = (TextView) view.findViewById(R.id.title_historique);
        histoView = (UsersHistoView) view.findViewById(R.id.users_historique);

        return view;
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        histoView.changeUtilisateurs(mListener.getUtilisateurs(view.getId()));
        titleView.setText(String.format(Locale.FRANCE, "Détails de la session N°%d", view.getId()));
        simpleViewSwitcher.showNext();
    }

    @Override
    public void onClick(View v) {
        simpleViewSwitcher.setDisplayedChild(0);
    }
}
