package com.neocampus.wifishared.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.utils.WifiApControl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public class SessionUserView extends LinearLayout {

    private SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss", Locale.FRANCE);
    private HashMap<WifiApControl.Client, View> clients = new HashMap<>();

    public SessionUserView(Context context) {
        super(context);
    }

    public SessionUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setClients(List<WifiApControl.Client> pclients) {

        for(WifiApControl.Client client : pclients) {
            showClient(client);
        }
    }

    public void showClient(WifiApControl.Client client) {
        View view ;
        if(!clients.containsKey(client)) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.app_sessions_layout, null, false);

            TextView textView1 = (TextView) view.findViewById(R.id.addressPhysique);
            TextView textView2 = (TextView) view.findViewById(R.id.adressIP);

            textView1.setText(client.hwAddr);
            textView2.setText(client.ipAddr);

            int LayoutW = LayoutParams.MATCH_PARENT;
            int LayoutH = LayoutParams.WRAP_CONTENT;
            LayoutParams
                    params = new LayoutParams(LayoutW, LayoutH);
            addView(view, params);
            clients.put(client, view);
        }
    }

    public int getCount() {
        return clients.size();
    }
}
