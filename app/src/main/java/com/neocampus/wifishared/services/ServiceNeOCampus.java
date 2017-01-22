package com.neocampus.wifishared.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.neocampus.wifishared.listeners.OnServiceSetListener;
import com.neocampus.wifishared.observables.BatterieObservable;
import com.neocampus.wifishared.observables.ClientObservable;
import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.receivers.OnBatterieReceiver;
import com.neocampus.wifishared.receivers.OnHotspotReceiver;
import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.manage.SQLManager;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.Observable;
import java.util.Observer;

public class ServiceNeOCampus extends Service implements
        OnServiceSetListener, Observer {

    private ServiceNeOCampusBinder oCampusBinder;
    private SQLManager sqlManager;

    private ClientObservable clientObservable;
    private HotspotObservable hotspotObservable;
    private BatterieObservable batterieObservable;

    private ServiceTaskClients serviceTaskClients;
    private OnHotspotReceiver onHotspotReceiver;
    private OnBatterieReceiver onBatterieReceiver;

    public ServiceNeOCampus() {
        this.oCampusBinder = new ServiceNeOCampusBinder();
        this.hotspotObservable = new HotspotObservable();
        this.clientObservable = new ClientObservable();
        this.batterieObservable = new BatterieObservable();

    }

    @Override
    public void onCreate() {
        this.sqlManager = new SQLManager(this);
        this.sqlManager.open();

        this.serviceTaskClients = new ServiceTaskClients(this, this.clientObservable);
        this.onHotspotReceiver = new OnHotspotReceiver(this.hotspotObservable);
        this.onBatterieReceiver = new OnBatterieReceiver(this.batterieObservable);


        this.addObserver(this);
        this.registerReceiver(this.onBatterieReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.registerReceiver(this.onHotspotReceiver, new IntentFilter(WifiApControl.ACTION_WIFI_AP_CHANGED));

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        if (WifiApControl.checkPermission(this, false)) {
            onHotspotReceiver.updateHotspotState(this);
        }
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        this.serviceTaskClients.stopWatchDog();
        this.batterieObservable.deleteObservers();
        this.unregisterReceiver(onHotspotReceiver);
        this.unregisterReceiver(onBatterieReceiver);
        this.sqlManager.close();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return oCampusBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }


    @Override
    public void addObserver(Observer observer) {
        batterieObservable.addObserver(observer);
        hotspotObservable.addObserver(observer);
        clientObservable.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        batterieObservable.deleteObserver(observer);
        hotspotObservable.deleteObserver(observer);
        clientObservable.deleteObserver(observer);
    }

    @Override
    public void forceSave() {

    }

    private void startWatchDog() {
        this.serviceTaskClients.startWatchDog(10000);
    }

    private void stopWatchDog() {
        this.serviceTaskClients.stopWatchDog();
    }

    public void setWatchDogState(boolean enable) {
        if (enable) {
            startWatchDog();
        } else {
            stopWatchDog();
        }
    }

    public boolean isOverBatterieLimit(int level) {
        TableConfiguration tableConfiguration
                = sqlManager.getConfiguration();
        return tableConfiguration.getLimiteBatterie() >= level;
    }

    private void stopHotpost() {
        if(WifiApControl.checkPermission(this)) {
            WifiApControl apControl = WifiApControl.getInstance(this);
            if(apControl.isUPSWifiConfiguration()) {
                apControl.disable();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof HotspotObservable) {
            setWatchDogState((boolean) arg);
        } else if (o instanceof BatterieObservable) {
            if(isOverBatterieLimit((Integer) arg)) {
                stopHotpost();
            }
        }
    }

    public class ServiceNeOCampusBinder extends Binder {
        public OnServiceSetListener getOnServiceSetListener() {
            return ServiceNeOCampus.this;
        }
    }
}
