package com.neocampus.wifishared.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public class ReachableUserView extends LinearLayout {

    private HashMap<WifiApControl.Client, View> clients = new HashMap<>();

    public ReachableUserView(Context context) {
        super(context);
    }

    public ReachableUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showClients(List<WifiApControl.Client> pclients) {

        for(Iterator<Map.Entry<WifiApControl.Client, View>> it =
            this.clients.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<WifiApControl.Client, View> entry = it.next();
            if(!pclients.contains(entry.getKey())) {
                removeView(entry.getValue());
                it.remove();
            }
        }

        for(WifiApControl.Client client : pclients) {
            showClient(client);
        }
    }

    public void showClient(WifiApControl.Client client)
    {
        if(!clients.containsKey(client)) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.app_users_layout, null, false);

            TextView textView1 = (TextView) view.findViewById(R.id.addressPhysique);
            TextView textView2 = (TextView) view.findViewById(R.id.adressIP);

            textView1.setText(client.hwAddr);
            textView2.setText(client.ipAddr);


            int LayoutW = LinearLayout.LayoutParams.MATCH_PARENT;
            int LayoutH = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams
                    params = new LinearLayout.LayoutParams(LayoutW, LayoutH);
            addView(view, params);
            clients.put(client, view);
        }
        else if(!client.connected)
        {
            removeView(clients.get(client));
            clients.remove(client);
        }
    }

    public int getCount() {
        return clients.size();
    }
}
