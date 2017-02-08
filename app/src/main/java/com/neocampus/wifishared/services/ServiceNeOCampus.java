package com.neocampus.wifishared.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.neocampus.wifishared.listeners.OnFragmentSetListener;
import com.neocampus.wifishared.listeners.OnReachableClientListener;
import com.neocampus.wifishared.listeners.OnServiceSetListener;
import com.neocampus.wifishared.observables.BatterieObservable;
import com.neocampus.wifishared.observables.ClientObservable;
import com.neocampus.wifishared.observables.DataObservable;
import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.observables.NetworkObservable;
import com.neocampus.wifishared.observables.TimeObservable;
import com.neocampus.wifishared.receivers.OnAlarmReceiver;
import com.neocampus.wifishared.receivers.OnBatterieReceiver;
import com.neocampus.wifishared.receivers.OnHotspotReceiver;
import com.neocampus.wifishared.receivers.OnNetworkReceiver;
import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.database.TableConsommation;
import com.neocampus.wifishared.sql.database.TableUtilisateur;
import com.neocampus.wifishared.sql.manage.SQLManager;
import com.neocampus.wifishared.utils.BatterieUtils;
import com.neocampus.wifishared.utils.LocationManagment;
import com.neocampus.wifishared.utils.NotificationUtils;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ServiceNeOCampus extends Service implements OnServiceSetListener, Observer {

    private ServiceNeOCampusBinder oCampusBinder;
    private SQLManager sqlManager;
    private LocationManagment locationManagment;

    private ClientObservable clientObservable;
    private HotspotObservable hotspotObservable;
    private BatterieObservable batterieObservable;
    private DataObservable dataObservable;
    private TimeObservable timeObservable;
    private NetworkObservable networkObservable;

    private ServiceDataTraffic serviceData;
    private ServiceTaskClients serviceTaskClients;
    private OnHotspotReceiver onHotspotReceiver;
    private OnBatterieReceiver onBatterieReceiver;
    private OnAlarmReceiver onAlarmReceiver;
    private OnNetworkReceiver onNetworkReceiver;

    public ServiceNeOCampus() {
        this.oCampusBinder = new ServiceNeOCampusBinder();
        this.hotspotObservable = new HotspotObservable();
        this.clientObservable = new ClientObservable();
        this.batterieObservable = new BatterieObservable();
        this.dataObservable = new DataObservable();
        this.timeObservable = new TimeObservable();
        this.networkObservable = new NetworkObservable();
    }


    @Override
    public void onCreate() {
        this.locationManagment = new LocationManagment(this);
        this.sqlManager = new SQLManager(this);
        this.sqlManager.open();

        this.serviceData = new ServiceDataTraffic(this, this.dataObservable);
        this.serviceTaskClients = new ServiceTaskClients(this, this.clientObservable);
        this.onHotspotReceiver = new OnHotspotReceiver(this.hotspotObservable);
        this.onBatterieReceiver = new OnBatterieReceiver(this.batterieObservable);
        this.onAlarmReceiver = new OnAlarmReceiver(this.timeObservable);
        this.onNetworkReceiver = new OnNetworkReceiver(this.networkObservable);

        this.restoreFromDataBase();

        this.addObserver(this);
        this.batterieObservable.setValue((int) BatterieUtils.getBatteryLevel(this));
        this.registerReceiver(this.onBatterieReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.registerReceiver(this.onAlarmReceiver, new IntentFilter(OnAlarmReceiver.ACTION_ALARM_ACTIVATED));
        this.registerReceiver(this.onHotspotReceiver, new IntentFilter(WifiApControl.ACTION_WIFI_AP_CHANGED));
        this.registerReceiver(this.onNetworkReceiver, new IntentFilter(WifiApControl.ACTION_CONNECTIVITY_CHANGE));

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        if (WifiApControl.checkPermission(this, false)) {
            onHotspotReceiver.updateHotspotState(this);
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.serviceData.stopWatchDog();
        this.serviceTaskClients.stopWatchDog();
        this.batterieObservable.deleteObservers();
        this.unregisterReceiver(onAlarmReceiver);
        this.unregisterReceiver(onHotspotReceiver);
        this.unregisterReceiver(onNetworkReceiver);
        this.unregisterReceiver(onBatterieReceiver);
        this.sqlManager.close();
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
        networkObservable.addObserver(observer);
        hotspotObservable.addObserver(observer);
        clientObservable.addObserver(observer);
        dataObservable.addObserver(observer);
        timeObservable.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        batterieObservable.deleteObserver(observer);
        networkObservable.deleteObserver(observer);
        hotspotObservable.deleteObserver(observer);
        clientObservable.deleteObserver(observer);
        dataObservable.deleteObserver(observer);
        timeObservable.deleteObserver(observer);
    }

    @Override
    public void peekDataTraffic(OnFragmentSetListener listener) {
        listener.onRefreshDataTraffic(dataObservable.getValue());
    }

    @Override
    public void peekAllClients(OnReachableClientListener listener) {
        listener.onReachableClients(clientObservable.getHistoriqueClients());
    }

    @Override
    public void peekReachableClients(OnReachableClientListener listener) {
        listener.onReachableClients(clientObservable.getClients());
    }

    @Override
    public void resetBaseT0() {
        dataObservable.setValue(0);
        serviceData.refreshFromDataBase(0);
    }

    @Override
    public void peekTimeValue(OnFragmentSetListener listener) {
        listener.onRefreshTimeValue(timeObservable.getDate());
    }

    @Override
    public void storeInDataBase() {
        sqlManager.setConfigurationS(true);
        long dataT0 = dataObservable.getValue();
        sqlManager.setConfigurationD(dataT0);
    }

    public void restoreFromDataBase() {
        TableConfiguration configuration = sqlManager.getConfiguration();
        if(configuration.isStored()) {
            TableConsommation consommation
                    = sqlManager.getLastConsommation();
            if(consommation != null && consommation.getDateEnd() == 0) {
                hotspotObservable.setSessionId(consommation.getID());
                List<TableUtilisateur> utilisateurs =
                        sqlManager.getUtilisateurs(consommation.getID());
                for(TableUtilisateur utilisateur : utilisateurs)
                    clientObservable.restoreClient(utilisateur);
            }
            sqlManager.setConfigurationS(false);
        }
        dataObservable.setValue(configuration.getDataT0());
        serviceData.refreshFromDataBase(configuration.getDataT0());
    }

    private void startWatchDog() {
        this.serviceData.startWatchDog(1000);
        this.serviceTaskClients.startWatchDog(10000);
        this.onAlarmReceiver.startAlarm(this, sqlManager);
    }

    private void stopWatchDog() {
        this.serviceData.stopWatchDog();
        this.serviceTaskClients.stopWatchDog();
        this.onAlarmReceiver.stopAlarm(this, sqlManager);
    }

    private void createSession() {
        if(hotspotObservable.getSessionId() == -1) {
            long date = new Date().getTime();
            long dataT0 = serviceData.getBaseT0();
            boolean isUPS = locationManagment.isAtUniversity();
            int idConso = sqlManager.newConsommation(date, dataT0, isUPS);
            hotspotObservable.setSessionId(idConso);
        }
    }

    public void setWatchDogState(boolean enable) {
        if (enable) {
            createSession();
            startWatchDog();
        } else {
            saveInDataBase();
            stopWatchDog();
        }
    }

    public void saveInDataBase(){
        int idConso =
                hotspotObservable.getSessionId();
        if (idConso > 0) {
            long date = new Date().getTime();
            long dataTx = dataObservable.getValue();

            sqlManager.setConfigurationD(dataTx);
            sqlManager.updateConsommationDataTx(idConso, dataTx);
            sqlManager.updateConsommationDateEnd(idConso, date);
            hotspotObservable.setSessionId(-1);
        }
    }

    public boolean isOverTimeLimit(long timeValue) {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        return tableConfiguration.getDateAlarm() > 0
                && tableConfiguration.getDateAlarm() < timeValue;
    }

    public boolean isOverDataLimit(long dataLevel) {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        return tableConfiguration.getLimiteConsommation() <= dataLevel;
    }

    public boolean isOverBatterieLimit(int level) {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
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
            HotspotObservable observable = (HotspotObservable) o;
            if (observable.isUPS()) {
                setWatchDogState(observable.isRunning());
            }
        } else if (o instanceof BatterieObservable) {
            if(hotspotObservable.isRunning()
                    && hotspotObservable.isUPS()
                    && isOverBatterieLimit((Integer) arg)) {
                stopHotpost();
                TableConfiguration tableConfiguration = sqlManager.getConfiguration();
                if(NotificationUtils.isBatterieEnabled(tableConfiguration.getNotification())) {
                    NotificationUtils.showBatterieNotify(this, batterieObservable);
                }
            }
        } else if (o instanceof  DataObservable) {
            if(isOverDataLimit((long) arg)) {
                stopHotpost();
                TableConfiguration tableConfiguration = sqlManager.getConfiguration();
                if(NotificationUtils.isDataEnabled(tableConfiguration.getNotification())) {
                    NotificationUtils.showDataNotify(this, dataObservable);
                }
            }
        } else if (o instanceof  TimeObservable) {
            if(isOverTimeLimit((long) arg)) {
                stopHotpost();
                TableConfiguration tableConfiguration = sqlManager.getConfiguration();
                if(NotificationUtils.isTimeEnabled(tableConfiguration.getNotification())) {
                    NotificationUtils.showTimeNotify(this);
                }
            }
        }else if (o instanceof  ClientObservable) {
            WifiApControl.Client client = (WifiApControl.Client) arg;
            if(client.connected) {
                int id = sqlManager.addUtilisateur(hotspotObservable.getSessionId(),
                        client.hwAddr, client.ipAddr, client.date.connected, client.date.disconnected);
                client.date.id = id;
            }
            else if(client.date != null){
                sqlManager.updateDisconnectedTime(client.date.id, client.date.disconnected);
            }
        } else if(o instanceof NetworkObservable)
        {
            if(!networkObservable.isEnabled() &&
                    hotspotObservable.isRunning() && hotspotObservable.isUPS()) {
                stopHotpost();
                NotificationUtils.showNetworkNotify(this);
            }
        }
    }

    public class ServiceNeOCampusBinder extends Binder {
        public OnServiceSetListener getOnServiceSetListener() {
            return ServiceNeOCampus.this;
        }
    }
}
