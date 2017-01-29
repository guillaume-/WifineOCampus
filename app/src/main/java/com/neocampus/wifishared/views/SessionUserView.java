package com.neocampus.wifishared.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnAdapterViewListener;
import com.neocampus.wifishared.utils.WifiApControl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public class SessionUserView extends LinearLayout implements OnAdapterViewListener {

    private List<WifiApControl.Client> clients = new ArrayList<>();
    private SimpleDateFormat format = new SimpleDateFormat("HH':'mm':'ss", Locale.FRANCE);
    private ListClientAdapter adapter;

    public SessionUserView(Context context) {
        super(context);
    }

    public SessionUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.onInit();
    }

    private void onInit() {
        adapter = new ListClientAdapter(getContext(),
                this, R.layout.app_sessions_layout, clients);
        ListView lvItems = (ListView) findViewById(R.id.lvClients);
        lvItems.setAdapter(adapter);
    }

    public void changeClients(List<WifiApControl.Client> newList)
    {
        adapter.swap(newList);
    }

    public void setClient(WifiApControl.Client client) {
        if(client.connected) {
            adapter.add(client);
        }
        else {
            clients.lastIndexOf(client);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View showView(View view, Object o) {
        WifiApControl.Client client = (WifiApControl.Client) o;
        TextView textView1 = (TextView) view.findViewById(R.id.addressPhysique);
        TextView textView2 = (TextView) view.findViewById(R.id.adressIP);
        TextView textView3 = (TextView) view.findViewById(R.id.heure_cx);

        textView1.setText(client.hwAddr);
        textView2.setText(client.ipAddr);

        if(client.connected) {
            textView3.setText(String.format("Toujours connecté : %s", format.format(client.date.connected)));
        }
        else {
            textView3.setText(String.format("De %s à %s",
                    format.format(client.date.connected),
                    format.format(client.date.disconnected)));
        }
        return view;
    }

    public int getCount() {
        return clients.size();
    }


}
