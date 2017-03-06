package com.neocampus.wifishared.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnAdapterViewListener;
import com.neocampus.wifishared.sql.database.TableConsommation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * SessionHistoView permet d'afficher la liste des sessions de partage réalisées
 */

public class SessionHistoView extends LinearLayout implements OnAdapterViewListener {

    private ListClientAdapter adapter;
    private List<TableConsommation> sessions = new ArrayList<>();
    private SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private SimpleDateFormat format2 = new SimpleDateFormat("HH'H'mm", Locale.FRANCE);

    public SessionHistoView(Context context) {
        super(context);
    }

    public SessionHistoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SessionHistoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.onInit();
    }

    private void onInit() {
        adapter = new ListClientAdapter(getContext(),
                this, R.layout.app_session_layout, sessions);
        ListView lvItems = (ListView) findViewById(R.id.lvClients);
        lvItems.setAdapter(adapter);
    }


    public void changeSessions(List<TableConsommation> psessions)
    {
        adapter.swap(psessions);
    }

    @Override
    public View showView(View view, Object o) {
        TableConsommation consommation = (TableConsommation) o;

        TextView textView1 = (TextView) view.findViewById(R.id.consommation_id);
        TextView textView2 = (TextView) view.findViewById(R.id.consommation_date);
        TextView textView3 = (TextView) view.findViewById(R.id.consommation_count);
        TextView textView4 = (TextView) view.findViewById(R.id.consommation_volume);
        TextView textView5 = (TextView) view.findViewById(R.id.consommation_D0);
        TextView textView6 = (TextView) view.findViewById(R.id.consommation_DX);

        textView1.setText(String.format(Locale.FRANCE, "Session N°%d", consommation.getID()));

        textView5.setText(format2.format(consommation.getDate()));
        if(consommation.getDateEnd() != 0) {
            textView6.setText(format2.format(consommation.getDateEnd()));
        }else {
            textView6.setText("En cours ...");
        }
        textView2.setText(format1.format(consommation.getDate()));
        textView3.setText(String.valueOf(consommation.getNbreUser()));

        long volume = consommation.getConsommationTx() - consommation.getConsommationT0();
        if(volume > 0) {
            float data = volume / 1000000000.f;
            String limiteData;
            if (data >= 1.0f) {
                limiteData = String.format(Locale.FRANCE, "%.3f Go", data);
            } else {
                limiteData = String.format(Locale.FRANCE, "%.2f Mo", data * 1000.f);
            }
            textView4.setText(String.valueOf(limiteData));
        }
        else {
            textView4.setText("0,00 Mo");
        }

        view.setId(consommation.getID());
        return view;
    }
}
