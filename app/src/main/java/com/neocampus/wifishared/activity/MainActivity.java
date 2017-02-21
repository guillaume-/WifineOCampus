package com.neocampus.wifishared.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.fragments.FragmentBatterie;
import com.neocampus.wifishared.fragments.FragmentHome;
import com.neocampus.wifishared.fragments.FragmentSession;
import com.neocampus.wifishared.fragments.FragmentSettings;
import com.neocampus.wifishared.fragments.FragmentTime;
import com.neocampus.wifishared.fragments.FragmentTraffic;
import com.neocampus.wifishared.fragments.FragmentUsers;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentConfigListener;
import com.neocampus.wifishared.listeners.OnFragmentSetListener;
import com.neocampus.wifishared.listeners.OnReachableClientListener;
import com.neocampus.wifishared.listeners.OnServiceSetListener;
import com.neocampus.wifishared.observables.BatterieObservable;
import com.neocampus.wifishared.observables.ClientObservable;
import com.neocampus.wifishared.observables.DataObservable;
import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.observables.TimeObservable;
import com.neocampus.wifishared.services.ServiceNeOCampus;
import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.database.TableConsommation;
import com.neocampus.wifishared.sql.database.TableUtilisateur;
import com.neocampus.wifishared.sql.manage.SQLManager;
import com.neocampus.wifishared.utils.BatterieUtils;
import com.neocampus.wifishared.utils.FragmentUtils;
import com.neocampus.wifishared.utils.LocationManagment;
import com.neocampus.wifishared.utils.NetworkUtils;
import com.neocampus.wifishared.utils.ParcelableUtils;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * <b>MainActivity est l'activité lancé au démarrage de l'application.</b>
 * <p>
 * Cette classe représente le conteneur de la vue utilisateur,
 * et centralise l'ensemble des actions entre les fonctionnalités.
 *
 * </p>
 *
 * @see OnActivitySetListener
 */
public class MainActivity extends AppCompatActivity implements ServiceConnection,
        NavigationView.OnNavigationItemSelectedListener, OnActivitySetListener, Observer {

    /**
     * Classe de communication avec la base de données
     * @see SQLManager
     */
    private SQLManager sqlManager;

    /**
     * Classe de configuration du partage WI-FI
     * @see WifiApControl
     */
    private WifiApControl apControl;

    /**
     * Fragment courant affiché par l'activité
     * @see Fragment
     */
    private Fragment fragment = null;

    /**
     *  Objet graphique contenant le toolbar en entete
     */
    private View mAppBarContent;

    /**
     * Intent de connexion au service principal de l'application
     * @see ServiceNeOCampus
     * */
    private Intent serviceintent;

    /**
     * Interface de communication avec le service principal
     * @see ServiceNeOCampus
     */
    private OnServiceSetListener mServiceInterraction;

    /**
     * Utilitaire de géo-localisation
     */
    private LocationManagment locManage;

    /**
     * Initialise les variables membres
     * @see AppCompatActivity#onCreate(Bundle)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.setSupportActionBar(toolbar);

        /*Check if permission is enabled for wifi configuration*/
        if (WifiApControl.checkPermission(this, true)) {
            this.locManage = new LocationManagment(this);
            this.sqlManager = new SQLManager(this);
            this.sqlManager.open();

            this.apControl = WifiApControl.getInstance(this);

            this.mAppBarContent = LayoutInflater.from(this)
                    .inflate(R.layout.app_bar_content, null, false);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            toolbar.addView(mAppBarContent, params);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            this.fragment = FragmentUtils.showFragment(this, FragmentHome.class);

            serviceintent = new Intent(this, ServiceNeOCampus.class);
            connectToService();

            if(!locManage.isEnabled()){
                buildAlertMessageNoGps();
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    /**
     * Ferme le volet des menus s'il est ouvert, ou restaure la vue précédente
     * @see AppCompatActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Cette méthode annule la création de menu
     * @param menu
     * @return vrai
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Change la vue du fragment actuel en cas de clique sur un menu du volet des menus
     * @param item menu sur lequel l'utilisateur a cliqué
     * @return toujours vrai
     * @see NavigationView.OnNavigationItemSelectedListener
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (fragment.getClass() != FragmentHome.class) {
                fragment = FragmentUtils.showFragment(this, FragmentHome.class);
            }
        } else if (id == R.id.nav_users) {
            if (fragment.getClass() != FragmentUsers.class) {
                fragment = FragmentUtils.showFragment(this, FragmentUsers.class);
            }
        } else if (id == R.id.nav_historique) {
            if (fragment.getClass() != FragmentSession.class) {
                fragment = FragmentUtils.showFragment(this, FragmentSession.class);
            }
        } else if (id == R.id.nav_setting) {
            if (fragment.getClass() != FragmentSettings.class) {
                fragment = FragmentUtils.showFragment(this, FragmentSettings.class);
            }
        } else {
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Cloture la connexion de l'activité au service principal {@link ServiceNeOCampus} et ferme la base de données
     *
     * @see MainActivity#disconnectToService()
     * @see AppCompatActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        disconnectToService();
        if (sqlManager != null) {
            sqlManager.close();
        }
        super.onDestroy();
    }

    /**
     * Rafraéchie l'affichage du fragment actuel s'il implemente {@link OnFragmentSetListener}
     * @param v View sur laquel l'utilisateur a cliqué
     *
     * @see OnFragmentSetListener#onRefreshAll()
     */
    public void onClickToRefresh(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (fragment instanceof OnFragmentSetListener) {
            ((OnFragmentSetListener) fragment).onRefreshAll();
        }
        v.findViewById(R.id.updatebutton).setVisibility(View.INVISIBLE);
        v.findViewById(R.id.progress).setVisibility(View.VISIBLE);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.findViewById(R.id.progress).setVisibility(View.INVISIBLE);
                v.findViewById(R.id.updatebutton).setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    /**
     * Sauvegarde dans la base de données, la limite courante configurée par l'utilisateur, en cas de clique
     * @param v View sur laquelle l'utilisateur a cliqué
     *
     * @see OnFragmentConfigListener#getLimiteDataTraffic()
     * @see OnFragmentConfigListener#getLimiteBatterie()
     * @see OnFragmentConfigListener#getLimiteTemps()
     */
    public void onClickToSaveConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (fragment instanceof OnFragmentConfigListener) {
            if (fragment instanceof FragmentTraffic) {
                float limite_data = ((OnFragmentConfigListener) fragment).getLimiteDataTraffic();
                long limite_in_octet = (long) ((1000.0f * 1000.0f * 1000.0f) * limite_data);
                this.sqlManager.setConfigurationC(limite_in_octet);
            } else if (fragment instanceof FragmentBatterie) {
                int limite_batterie = ((OnFragmentConfigListener) fragment).getLimiteBatterie();
                this.sqlManager.setConfigurationB(limite_batterie);
            } else if (fragment instanceof FragmentTime) {
                long limite_temps = ((OnFragmentConfigListener) fragment).getLimiteTemps();
                this.sqlManager.setConfigurationT(limite_temps);
            }
            this.sqlManager.setConfigurationN(
                    ((OnFragmentConfigListener) fragment).getNotificationCode());
        }
        super.onBackPressed();
        fragment = FragmentUtils.getForegroundFragment(this);
        if (fragment != null && fragment instanceof OnFragmentSetListener) {
            ((OnFragmentSetListener) fragment).onRefreshAllConfig();
        }
    }

    /**
     * Démarrage et arrêt du partage en cas de clique
     * @param v View sur laquel l'utilisateur a cliqué
     */
    public void onClickToRunAPWifi(View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        WifiConfiguration configuration
                = WifiApControl.getUPSWifiConfiguration();
        if (isUPSWifiConfiguration(configuration)) {
            if (apControl.isWifiApEnabled()) {
                apControl.disable();
            } else if(verifyConditions()){
                apControl.enable();
            }
        } else if (apControl.isEnabled()) {
            apControl.disable();
        } else if(verifyConditions()){
            WifiConfiguration userConfiguration = apControl.getConfiguration();
            byte[] bytes = ParcelableUtils.marshall(userConfiguration);
            sqlManager.setConfiguration(bytes);
            apControl.setWifiApConfiguration(configuration);
            apControl.enable();
        }
    }

    /**
     * Affiche un fragment pour définir le seuil de transmission
     * @param v View sur laquel l'utilisateur a cliqué
     */
    public void onClickToDataConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (verifyWifiDisabled()) {
            float limite_conso = getLimiteDataTrafic();
            Fragment fragment = FragmentTraffic.
                    newInstance(limite_conso, getNotificationCode());
            this.fragment = FragmentUtils.showFragment(this, fragment,
                    R.anim.circle_zoom, R.anim.circle_inverse_zoom);
        }
    }

    /**
     * Affiche un fragment pour définir le seuil de la batterie
     * @param v View sur laquel l'utilisateur a cliqué
     */
    public void onClickToBatterieConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (verifyWifiDisabled()) {
            int limite_batterie = getLimiteBatterie();
            Fragment fragment = FragmentBatterie.
                    newInstance(limite_batterie, getNotificationCode());
            this.fragment = FragmentUtils.showFragment(this, fragment,
                    R.anim.circle_zoom, R.anim.circle_inverse_zoom);
        }
    }

    /**
     * Affiche un fragment pour définir le seuil du temps d'utilisation
     * @param v View sur laquel l'utilisateur a cliqué
     */
    public void onClickToTimeConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (verifyWifiDisabled()) {
            long limite_temps = getLimiteTemps();
            Fragment fragment = FragmentTime.
                    newInstance(limite_temps, getNotificationCode());
            this.fragment = FragmentUtils.showFragment(this, fragment,
                    R.anim.circle_zoom, R.anim.circle_inverse_zoom);
        }
    }

    /**
     * Efface tous les informations d'historique de connexion
     * @param v View sur laquel l'utilisateur a cliqué
     */
    public void onClickToResetDataBase(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (verifyWifiDisabled()) {
            View view = LayoutInflater.from(this).
                    inflate(R.layout.app_progress_layout, null, false);
            View surfaceView = view.findViewById(R.id.progress);
            Dialog alert = new Dialog(this, R.style.AppTheme_AlertDialog);
//            alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alert.setContentView(view);
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.setCancelable(true);
            surfaceView.setTag(alert);
            alert.show();

            sqlManager.removeAllUtilisateur();
            sqlManager.removeAllConsommations();
            sqlManager.setConfigurationD(0);
            if(mServiceInterraction != null) {
                mServiceInterraction.resetBaseT0();
            }
        }
    }

    /**
     * Se connecte au service principal {@link ServiceNeOCampus}
     */
    private void connectToService() {
        if (!isServiceRunning(ServiceNeOCampus.class)) {
            startService(serviceintent);
        }
        bindService(serviceintent, this, Context.BIND_AUTO_CREATE);
    }

    /**
     * Se deconnecte du service principal {@link ServiceNeOCampus} et force la sauvegarde
     *
     */
    private void disconnectToService() {
        if (isServiceRunning(ServiceNeOCampus.class)) {
            if (mServiceInterraction != null) {
                mServiceInterraction.storeInDataBase();
            }
            unbindService(this);
        }
    }

    /**
     * <p>Est appelé lorsque la connexion au service principal {@link ServiceNeOCampus} est établie.</p>
     * <p>Affiche les données initiales</p>
     *
     * @param name
     * @param service
     * @see ServiceConnection#onServiceConnected(ComponentName, IBinder)
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        ServiceNeOCampus.ServiceNeOCampusBinder binder =
                (ServiceNeOCampus.ServiceNeOCampusBinder) service;
        mServiceInterraction = binder.getOnServiceSetListener();
        mServiceInterraction.addObserver(this);

        postRequestTimeValue();
        postRequestListClients();
        postRequestDataTraffic();
    }

    /**
     * Est appelé lors de la déconnexion du service principal {@link ServiceNeOCampus}.
     * @param name
     * @see ServiceConnection#onServiceDisconnected(ComponentName)
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        mServiceInterraction = null;
    }

    /**
     * Est appelé lorsque l'état d'un observable a changé.
     * @param o
     * @param newValue
     * @see Observer#update(Observable, Object)
     */
    @Override
    public void update(Observable o, Object newValue) {
        if (fragment != null
                && fragment instanceof OnFragmentSetListener) {
            OnFragmentSetListener listener = (OnFragmentSetListener) fragment;
            if (o instanceof DataObservable) {
                ((OnFragmentSetListener) fragment).onRefreshDataTraffic((Long) newValue);
            } else if (o instanceof BatterieObservable) {
                listener.onRefreshBatterieLevel((Integer) newValue);
            } else if (o instanceof ClientObservable) {
                listener.onRefreshClient((WifiApControl.Client) newValue);
                listener.onRefreshClientCount(((ClientObservable) o).getCount());
            } else if (o instanceof HotspotObservable) {
                listener.onRefreshHotpostState((HotspotObservable) o);
            } else if (o instanceof TimeObservable) {
                listener.onRefreshTimeValue((Long) newValue);
            }
        }
        System.out.println("Activity : I am notified " + newValue);
    }

    /**
     * Récupère dans la base de données le seuil de transmission
     * @return le seuil de transmission
     *
     * @see SQLManager#getConfiguration()
     * @see TableConfiguration#getLimiteConsommation()
     */
    @Override
    public float getLimiteDataTrafic() {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        long consommation = tableConfiguration.getLimiteConsommation();
        float value = consommation / (1000.0f * 1000.0f * 1000.0f);
        return value;
    }

    /**
     * Récupère dans la base de données le seuil de la batterie
     * @return le seuil de la batterie
     *
     * @see SQLManager#getConfiguration()
     * @see TableConfiguration#getLimiteBatterie()
     */
    @Override
    public int getLimiteBatterie() {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        if (tableConfiguration != null) {
            return tableConfiguration.getLimiteBatterie();
        }
        return BatterieUtils.BATTERIE_DEFAULT_LIMIT;
    }

    /**
     * Récupère dans la base de données le seuil du temps de partage
     * @return le seuil du temps de partage
     *
     * @see SQLManager#getConfiguration()
     * @see TableConfiguration#getLimiteTemps()
     */
    @Override
    public long getLimiteTemps() {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        return tableConfiguration.getLimiteTemps();
    }

    /**
     * Récupère dans la base de données le code de notification
     * @return le code de notification
     */
    public int getNotificationCode(){
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        return tableConfiguration.getNotification();
    }
    /**
     * Post une demande de mise à jour des clients, au service principale {@link ServiceNeOCampus}
     * @see OnServiceSetListener#peekReachableClients(OnReachableClientListener)
     * @see OnServiceSetListener#peekAllClients(OnReachableClientListener)
     */
    @Override
    public void postRequestListClients() {
        if (mServiceInterraction != null) {
            if (fragment instanceof FragmentHome) {
                mServiceInterraction.peekReachableClients((OnReachableClientListener) fragment);
            } else if (fragment instanceof FragmentUsers) {
                mServiceInterraction.peekAllClients((OnReachableClientListener) fragment);
            }
        }
    }

    /**
     * Post une demande de mise à jour de la consommation de données, au service principale {@link ServiceNeOCampus}
     * @see OnServiceSetListener#peekDataTraffic(OnFragmentSetListener)
     */
    @Override
    public void postRequestDataTraffic() {
        if (mServiceInterraction != null) {
            if (fragment instanceof FragmentHome) {
                mServiceInterraction.peekDataTraffic((OnFragmentSetListener) fragment);
            }
        }
    }

    /**
     * Post une demande de mise à jour du temps d'activation du partage, au service principale {@link ServiceNeOCampus}
     * @see OnServiceSetListener#peekTimeValue(OnFragmentSetListener)
     */
    @Override
    public void postRequestTimeValue() {
        if (mServiceInterraction != null) {
            if (fragment instanceof FragmentHome) {
                mServiceInterraction.peekTimeValue((OnFragmentSetListener) fragment);
            }
        }
    }

    /**
     * @see OnActivitySetListener#hideAppBarRefresh()
     */
    @Override
    public void hideAppBarRefresh() {
        mAppBarContent.findViewById(R.id.app_bar_refresh).setVisibility(View.INVISIBLE);
    }

    /**
     * @see OnActivitySetListener#showAppBarRefresh()
     */
    @Override
    public void showAppBarRefresh() {
        mAppBarContent.findViewById(R.id.app_bar_refresh).setVisibility(View.VISIBLE);
    }

    /**
     * @see OnActivitySetListener#hideAppBarSaveConfig()
     */
    @Override
    public void hideAppBarSaveConfig() {
        mAppBarContent.findViewById(R.id.app_bar_save_config).setVisibility(View.INVISIBLE);
    }

    /**
     * @see OnActivitySetListener#showAppBarSaveConfig()
     */
    @Override
    public void showAppBarSaveConfig() {
        mAppBarContent.findViewById(R.id.app_bar_save_config).setVisibility(View.VISIBLE);
    }

    /**
     * Récupère dans la base de données la liste des partages de l'utilisateur
     * @return la liste des partages de l'utilisateur
     *
     * @see SQLManager#getAllConsommations()
     */
    @Override
    public List<TableConsommation> getAllConsommations() {
        return sqlManager.getAllConsommations();
    }

    /**
     * Vérifie l'état du hotspot wifi, démarre ou arrête celui-ci
     */
    private void verifyAndRunAPWifi() {
        WifiConfiguration configuration
                = WifiApControl.getUPSWifiConfiguration();
        if (isUPSWifiConfiguration(configuration)) {
            if (apControl.isWifiApEnabled()) {
                apControl.disable();
            } else {
                apControl.enable();
            }
        } else if (apControl.isEnabled()) {
            apControl.disable();
        } else {
            WifiConfiguration userConfiguration = apControl.getConfiguration();
            byte[] bytes = ParcelableUtils.marshall(userConfiguration);
            sqlManager.setConfiguration(bytes);
            apControl.setWifiApConfiguration(configuration);
            apControl.enable();
        }
    }

    /**
     * Récupère dans la base de données la liste des clients d'un partage
     * @param iDConso identifiant de la session de partage
     * @return la liste des clients d'un partage
     *
     * @see SQLManager#getUtilisateurs(int)
     */
    @Override
    public List<TableUtilisateur> getUtilisateurs(int iDConso) {
        return sqlManager.getUtilisateurs(iDConso);
    }

    /**
     * Vérifie si les conditions de lancements sont valides
     * @return resultat de la vérification
     */
    private boolean verifyConditions() {
        TableConfiguration configuration
                = sqlManager.getConfiguration();
        if(configuration.getLimiteBatterie()
                > BatterieUtils.getBatteryLevel(this))
        {
            Toast.makeText(this, R.string.condition_batterie, Toast.LENGTH_LONG).show();
            return false;
        }
        else if(configuration.getLimiteTemps() == 0)
        {
            Toast.makeText(this, R.string.condition_temps, Toast.LENGTH_LONG).show();
            return false;
        }
        else if(configuration.getLimiteConsommation() == 0
                || configuration.getLimiteConsommation() < configuration.getDataT0())
        {
            Toast.makeText(this, R.string.condition_data, Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!WifiApControl.isSupported())
        {
            Toast.makeText(this, R.string.condition_supported, Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!NetworkUtils.isNetworkAvailable(this))
        {
            Toast.makeText(this, R.string.condition_internet, Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!verifyGPSEnabled())
        {
            return false;
        }
        return true;
    }


    /**
     * Vérifie si une session de partage WI-FI neOCampus est activé
     * @return resultat de la vérification
     *
     * @see WifiApControl#isEnabled()
     * @see WifiApControl#isUPSWifiConfiguration()
     */
    private boolean verifyWifiDisabled() {
        if (apControl.isEnabled()
                && apControl.isUPSWifiConfiguration()) {
            Toast.makeText(this, R.string.condition_shared, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Vérifie si la localisation de l'utilisateur est possible et qu'il/elle est dans la zone du campus UPS
     * affiche un message informant que le lancement du partage n'est pas pris en charge si l'utilisateur n'est pas dans le compus
     * @return resultat de la vérification
     */
    private boolean verifyGPSEnabled() {
        if(!locManage.isAtUniversity()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Info. localisation");
            alertDialogBuilder
                    .setMessage("Hors UPS (GPS désactivé ?), neOCampus décline toute responsabilité quant à l'utilisation de l'application.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            verifyAndRunAPWifi();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Vérifie si le service principale {@link ServiceNeOCampus} est en cours d'execution
     * @return resultat de la vérification
     *
     */
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo>
                runningServiceInfos = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : runningServiceInfos) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si la configuration WI-FI AP est celle de neOCampus
     * @return resultat de la vérification
     *
     * @see WifiApControl#getWifiApConfiguration()
     */
    private boolean isUPSWifiConfiguration(WifiConfiguration upsConfig) {
        WifiConfiguration
                configuration = apControl.getWifiApConfiguration();
        return WifiApControl.equals(configuration, upsConfig);
    }

    /**
     * demande à l'utilisateur s'il souhaite activer sa géo-localisation
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS désactivé. Voulez-vous l'activer ?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(i);
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}
