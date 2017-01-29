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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public class ReachableUserView extends LinearLayout implements OnAdapterViewListener {

    private ListClientAdapter adapter;
    private List<WifiApControl.Client> clients = new ArrayList<>();


    public ReachableUserView(Context context) {
        super(context);
    }

    public ReachableUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.onInit();
    }

    private void onInit()
    {
        this.adapter = new ListClientAdapter(getContext(),
                this, R.layout.app_users_layout, clients);
        ListView lvItems = (ListView) findViewById(R.id.lvClients);
        lvItems.setAdapter(adapter);
    }

    public void changeClients(List<WifiApControl.Client> newList)
    {
        List<WifiApControl.Client> list = new ArrayList<>();
        for(WifiApControl.Client client : newList){
            if(client.connected) {
                list.add(client);
            }
        }
        adapter.swap(list);
    }

    @Override
    public View showView(View view, Object o) {
        WifiApControl.Client client = (WifiApControl.Client) o;
        TextView textView1 = (TextView) view.findViewById(R.id.addressPhysique);
        TextView textView2 = (TextView) view.findViewById(R.id.adressIP);

        textView1.setText(client.hwAddr);
        textView2.setText(client.ipAddr);

        return view;
    }

    public void setClient(WifiApControl.Client client) {
        if(client.connected) {
            adapter.add(client);
        }

        else if(client.date != null) {
            adapter.remove(client);
        }
    }

    public int getCount() {
        return clients.size();
    }


}
