package com.neocampus.wifishared.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

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

/**
 * ServiceNeOCampus est une classe de service g�n�rale de l'application.
 * <p>
 *     Le service contient tous les services d'arri�re plan de l'application et
 *     s'occupe de leur lancement ou de leur arr�t, tous les services sont associ�s a un observateur d'�tat.
 * </p>
 */
public class ServiceNeOCampus extends Service implements OnServiceSetListener, Observer {

    /**
     * {@link Binder} permet de se connect� au service afin de communiquer avec cell-ci
     */
    private ServiceNeOCampusBinder oCampusBinder;

    /**
     * Objet permettant de communication avec la base de donn�es
     */
    private SQLManager sqlManager;

    /**
     * Objet permettant de savoir si le fournisseur de la borne se trouve dans le campus UPS
     */
    private LocationManagment locationManagment;

    /**
     * Observable permettant d'�tre notifi� sur un �v�nnement de connexion
     */
    private ClientObservable clientObservable;

    /**
     * Observable permettant d'�tre notifi� sur un changement de la configuration WIFI-AP
     */
    private HotspotObservable hotspotObservable;

    /**
     * Observable permettant d'�tre notifi� sur un changement du niveau de la batterie
     */
    private BatterieObservable batterieObservable;

    /**
     * Observable permettant d'�tre notifi� sur un changement de la consommation de donn�es
     */
    private DataObservable dataObservable;

    /**
     * Observable permettant d'�tre notifi� sur un d�clenchement d'alarme
     */
    private TimeObservable timeObservable;

    /**
     * Observable permettant d'�tre notifi� sur un changement de l'acc�s internet
     */
    private NetworkObservable networkObservable;

    /**
     * Service d'observation de la consommation de donn�es
     */
    private ServiceDataTraffic serviceData;

    /**
     * Service d'observation de la connexion au partage
     */
    private ServiceTaskClients serviceTaskClients;

    /**
     * Service d'observation de la configuration WIFI-AP
     */
    private OnHotspotReceiver onHotspotReceiver;

    /**
     * Service d'observation du niveau de la batterie
     */
    private OnBatterieReceiver onBatterieReceiver;

    /**
     * Service de gestion du temps d'activation du partage
     */
    private OnAlarmReceiver onAlarmReceiver;

    /**
     * Service d'observation de l'acc�s internet
     */
    private OnNetworkReceiver onNetworkReceiver;

    /**
     * Constructeur du service g�n�rale
     */
    public ServiceNeOCampus() {
        this.oCampusBinder = new ServiceNeOCampusBinder();
        this.hotspotObservable = new HotspotObservable();
        this.clientObservable = new ClientObservable();
        this.batterieObservable = new BatterieObservable();
        this.dataObservable = new DataObservable();
        this.timeObservable = new TimeObservable();
        this.networkObservable = new NetworkObservable();
    }


    /**
     * Cette m�thode initialise les services
     */
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

//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        if (WifiApControl.checkPermission(this, false)) {
            onHotspotReceiver.updateHotspotState(this);
        }
        super.onCreate();
    }

    /**
     * Permet le red�marrage du service en cas de fermetture de l'interface de l'application
     * @param intent
     * @param flags
     * @param startId
     * @return identifiant permettant d'indiquer que le service doit se relancer
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    /**
     * Permet la d�sactivation compl�te les services
     */
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

    /**
     * Installe un {@link Binder} permettant la communication avec ce service
     * @param intent
     * @return return le binder a installer
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return oCampusBinder;
    }

    /**
     * permet de d�sinstaller le {@link Binder} permettant la communication
     * @param conn {@link Binder} � d�sinstaller
     */
    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    /**
     * Cette m�thode permet de s'inscrire sur la liste des notifi�s de tous les observables
     * @param observer objet recevant la notification
     */
    @Override
    public void addObserver(Observer observer) {
        batterieObservable.addObserver(observer);
        networkObservable.addObserver(observer);
        hotspotObservable.addObserver(observer);
        clientObservable.addObserver(observer);
        dataObservable.addObserver(observer);
        timeObservable.addObserver(observer);
    }

    /**
     * Cette m�thode permet de se d�sinscrire de la liste des notifi�s de tous les observables
     * @param observer objet recevant la notification
     */
    @Override
    public void removeObserver(Observer observer) {
        batterieObservable.deleteObserver(observer);
        networkObservable.deleteObserver(observer);
        hotspotObservable.deleteObserver(observer);
        clientObservable.deleteObserver(observer);
        dataObservable.deleteObserver(observer);
        timeObservable.deleteObserver(observer);
    }


    /**
     * Cette m�thode actualise sur l'interface graphique la consommation de donn�es si l'utilisateur en fait la demande
     * @param listener interface de communication avec fragment
     */
    @Override
    public void peekDataTraffic(OnFragmentSetListener listener) {
        listener.onRefreshDataTraffic(dataObservable.getValue());
    }

    /**
     * Cette m�thode actualise sur l'interface graphique les clients connect�s depuis le d�but du partage
     * @param listener interface de communication avec fragment
     */
    @Override
    public void peekAllClients(OnReachableClientListener listener) {
        listener.onReachableClients(clientObservable.getHistoriqueClients());
    }

    /**
     * Cette m�thode actualise sur l'interface graphique les clients actuellement connect�s
     * @param listener interface de communication avec fragment
     */
    @Override
    public void peekReachableClients(OnReachableClientListener listener) {
        listener.onReachableClients(clientObservable.getClients());
    }

    /**
     * Permet de reinitialiser le service d'observation de la consommation de donn�es
     */
    @Override
    public void resetBaseT0() {
        dataObservable.setValue(0);
        serviceData.refreshFromDataBase(0);
    }

    /**
     * Cette m�thode actualise sur l'interface graphique le temps pour lequel l'alarme se d�clenchera
     * @param listener interface de communication avec fragment
     */
    @Override
    public void peekTimeValue(OnFragmentSetListener listener) {
        listener.onRefreshTimeValue(timeObservable.getDate());
    }

    /**
     * Cette m�thode permet de sauvegarder en urgence la consommation actuelle de donn�es
     */
    @Override
    public void storeInDataBase() {
        sqlManager.setConfigurationS(true);
        long dataT0 = dataObservable.getValue();
        sqlManager.setConfigurationD(dataT0);
    }

    /**
     * Cette m�thode permet de restaurer la consommation de donn�es sauvegard� en urgence
     */
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

    /**
     * Cette m�thode permet de lancer tous les services d'observations
     */
    private void startWatchDog() {
        this.serviceData.startWatchDog(1000);
        this.serviceTaskClients.startWatchDog(10000);
        this.onAlarmReceiver.startAlarm(this, sqlManager);
    }

    /**
     * Cette m�thode permet de d'arr�ter tous les services d'observations
     */
    private void stopWatchDog() {
        this.serviceData.stopWatchDog();
        this.serviceTaskClients.stopWatchDog();
        this.onAlarmReceiver.stopAlarm(this, sqlManager);
    }

    /**
     * Cette m�thode permet d'initialiser une ligne d'une session de partage
     * dans la base de donn�es
     */
    private void createSession() {
        if(hotspotObservable.getSessionId() == -1) {
            long date = new Date().getTime();
            long dataT0 = serviceData.getBaseT0();
            boolean isUPS = locationManagment.isAtUniversity();
            int idConso = sqlManager.newConsommation(date, dataT0, isUPS);
            hotspotObservable.setSessionId(idConso);
        }
    }

    /**
     * Cette m�thode permet de pr�-configurer le d�marrage et l'arr�t d'un partage
     * @param enable etat du partage
     */
    public void setWatchDogState(boolean enable) {
        if (enable) {
            createSession();
            startWatchDog();
        } else {
            saveInDataBase();
            stopWatchDog();
        }
    }

    /**
     * Cette m�thode permet de sauvegarder dans
     * la base de donn�es tous les informations observ�es avant l'arr�t d'un partage
     */
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

    /**
     * Cette m�thode teste si le temps d'activation du
     * partage a �t� d�pass� au d�clenchement d'une alarme
     * @param timeValue temps au d�clenchement de l'alarme
     * @return vrai si temps d'activation s'est �coul� faux sinon
     */
    public boolean isOverTimeLimit(long timeValue) {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        return tableConfiguration.getDateAlarm() > 0
                && tableConfiguration.getDateAlarm() < timeValue;
    }

    /**
     * Cette m�thode teste si la consommation de donn�es a atteint la limite d�finie par l'utilisateur
     * @param dataLevel consommation actuelle de donn�es
     * @return vrai si le seuil de consommation de donn�es a �t� atteint faux sinon
     */
    public boolean isOverDataLimit(long dataLevel) {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        return tableConfiguration.getLimiteConsommation() <= dataLevel;
    }

    /**
     * Cette m�thode teste si le niveau de la batterie a atteint la limite d�finie par l'utilisateur
     * @param level niveau actuel de la batterie
     * @return
     */
    public boolean isOverBatterieLimit(int level) {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        return tableConfiguration.getLimiteBatterie() >= level;
    }

    /**
     * Permet d'arr�ter le partage
     */
    private void stopHotpost() {
        if(WifiApControl.checkPermission(this)) {
            WifiApControl apControl = WifiApControl.getInstance(this);
            if(apControl.isUPSWifiConfiguration()) {
                apControl.disable();
            }
        }
    }

    /**
     * Cette m�thode est appel� par les observables pour notifier un changement
     * @param o observable source de la notification
     * @param arg information sur le changement observ�
     */
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
