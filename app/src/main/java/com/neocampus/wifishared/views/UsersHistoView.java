package com.neocampus.wifishared.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnAdapterViewListener;
import com.neocampus.wifishared.sql.database.TableUtilisateur;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hirochi ☠ on 31/01/17.
 */

public class UsersHistoView extends LinearLayout implements OnAdapterViewListener {

    private ListClientAdapter adapter;
    private List<TableUtilisateur> utilisateurs = new ArrayList<>();
    private SimpleDateFormat format = new SimpleDateFormat("HH':'mm':'ss", Locale.FRANCE);

    public UsersHistoView(Context context) {
        super(context);
    }

    public UsersHistoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UsersHistoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.onInit();
    }

    private void onInit() {
        adapter = new ListClientAdapter(getContext(),
                this, R.layout.app_users_recent, utilisateurs);
        ListView lvItems = (ListView) findViewById(R.id.lvClients);
        lvItems.setAdapter(adapter);
    }


    public void changeUtilisateurs(List<TableUtilisateur> putilisateurs)
    {
        adapter.swap(putilisateurs);
    }

    @Override
    public View showView(View view, Object o) {
        TableUtilisateur utilisateur = (TableUtilisateur) o;

        TextView textView1 = (TextView) view.findViewById(R.id.addressPhysique);
        TextView textView2 = (TextView) view.findViewById(R.id.adressIP);
        TextView textView3 = (TextView) view.findViewById(R.id.heure_cx);

        textView1.setText(utilisateur.getAdressMac());
        textView2.setText(utilisateur.getAdressIP());

        if(utilisateur.getDateFinCnx() == 0) {
            textView3.setText(String.format("Toujours connecté : %s",
                    format.format(utilisateur.getDateDebutCnx())));
        }
        else {
            textView3.setText(String.format("De %s à %s",
                    format.format(utilisateur.getDateDebutCnx()),
                    format.format(utilisateur.getDateFinCnx())));
        }

        return view;
    }
}
