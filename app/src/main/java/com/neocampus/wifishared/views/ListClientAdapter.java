package com.neocampus.wifishared.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.neocampus.wifishared.listeners.OnAdapterViewListener;

import java.util.List;

/**
 * ListClientAdapter permet de gérer la liste des clients
 * @author Hirochi ? 
 */
public class ListClientAdapter extends ArrayAdapter {

    private int resLayout;
    private LayoutInflater inflater;
    private Context context;
    private OnAdapterViewListener listener;

    public ListClientAdapter(Context context,
                             OnAdapterViewListener listener,int resLayout, List<?> objects) {

        super(context, 0, objects);
        this.context = context;
        this.listener = listener;
        this.resLayout = resLayout;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resLayout, parent, false);
        }
        return listener.showView(convertView, getItem(position));
    }

    public void swap(List<?> plist) {
        clear();
        addAll(plist);
        notifyDataSetChanged();
    }
}