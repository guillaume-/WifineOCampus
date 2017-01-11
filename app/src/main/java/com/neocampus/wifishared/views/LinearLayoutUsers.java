package com.neocampus.wifishared.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.List;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public class LinearLayoutUsers extends LinearLayout {

    public LinearLayoutUsers(Context context) {
        super(context);
    }

    public LinearLayoutUsers(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showClients(List<WifiApControl.Client> clients) {
        for(WifiApControl.Client client : clients)
            this.showClient(client);
    }

    private void showClient(WifiApControl.Client client)
    {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.app_users_layout, null, false);

        TextView textView1 = (TextView) view.findViewById(R.id.iDAddressPhysique);
        TextView textView2 = (TextView) view.findViewById(R.id.iDAdressIP);

        textView1.setText(client.hwAddr);
        textView2.setText(client.ipAddr);


        int LayoutW = LinearLayout.LayoutParams.MATCH_PARENT;
        int LayoutH = LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams
                params = new LinearLayout.LayoutParams(LayoutW,LayoutH);
        addView(view);
    }
}
