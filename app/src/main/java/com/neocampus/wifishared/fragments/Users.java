package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentSetListener;
import com.neocampus.wifishared.listeners.OnReachableClientListener;
import com.neocampus.wifishared.utils.WifiApControl;
import com.neocampus.wifishared.views.LinearLayoutUsers;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentSetListener} interface
 * to handle interaction events.
 * Use the {@link Users#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Users extends Fragment implements OnFragmentSetListener,  OnReachableClientListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    private OnActivitySetListener mListener;

    public Users() {
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
    public static Users newInstance(String param1, String param2) {
        Users fragment = new Users();
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

        this.view = inflater.inflate(R.layout.fragment_users, container, false);
        List<WifiApControl.Client> result = this.mListener.getReachableClients(this);
        if(result.isEmpty()) {
            TextView textView = (TextView) view.findViewById(R.id.iDClientCount);
            textView.setText("(0)");
        }
        return view;
    }

    @Override
    public void onReachableClient(final WifiApControl.Client c) {
        if(view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutUsers layoutUsers =
                            (LinearLayoutUsers) view.findViewById(R.id.iDLinearLayoutUsers);
                    layoutUsers.showClient(c);
                }
            });
        }
    }


    @Override
    public void onReachableClients(final List<WifiApControl.Client> clients) {
        if(view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) view.findViewById(R.id.iDClientCount);
                    textView.setText("(" + clients.size() + ")");
                    LinearLayoutUsers layoutUsers =
                            (LinearLayoutUsers) view.findViewById(R.id.iDLinearLayoutUsers);
                    layoutUsers.setClients(clients);
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


}
