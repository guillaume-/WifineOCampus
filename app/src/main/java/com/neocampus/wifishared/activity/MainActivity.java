package com.neocampus.wifishared.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.neocampus.wifishared.R;
import com.neocampus.wifishared.fragments.FragmentBatterie;
import com.neocampus.wifishared.fragments.FragmentHome;
import com.neocampus.wifishared.fragments.FragmentSettings;
import com.neocampus.wifishared.fragments.FragmentTime;
import com.neocampus.wifishared.fragments.FragmentTraffic;
import com.neocampus.wifishared.fragments.FragmentUsers;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentConfigListener;
import com.neocampus.wifishared.listeners.OnFragmentSetListener;
import com.neocampus.wifishared.listeners.OnReachableClientListener;
import com.neocampus.wifishared.listeners.OnServiceSetListener;
import com.neocampus.wifishared.location.LocationManagment;
import com.neocampus.wifishared.observables.BatterieObservable;
import com.neocampus.wifishared.observables.ClientObservable;
import com.neocampus.wifishared.observables.DataObservable;
import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.observables.TimeObservable;
import com.neocampus.wifishared.services.ServiceNeOCampus;
import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.manage.SQLManager;
import com.neocampus.wifishared.utils.BatterieUtils;
import com.neocampus.wifishared.utils.FragmentUtils;
import com.neocampus.wifishared.utils.ParcelableUtils;
import com.neocampus.wifishared.utils.WifiApControl;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements ServiceConnection,
        NavigationView.OnNavigationItemSelectedListener, OnActivitySetListener, Observer {

    private SQLManager sqlManager;
    private WifiApControl apControl;
    private Fragment fragment = null;
    private View mAppBarContent;
    private Intent serviceintent;
    private OnServiceSetListener mServiceInterraction;
    private LocationManagment locManage;

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

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    protected void onDestroy() {
        disconnectToService();
        if (sqlManager != null) {
            sqlManager.close();
        }
        super.onDestroy();
    }

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
        }
        super.onBackPressed();
        fragment = FragmentUtils.getForegroundFragment(this);
        if (fragment != null && fragment instanceof OnFragmentSetListener) {
            ((OnFragmentSetListener) fragment).onRefreshAllConfig();
        }
    }

    public void onClickToRunAPWifi(View v) {
        if(! locManage.isAtUniversity()){
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.gps_popup, null);
            final PopupWindow mPopupWindow = new PopupWindow(
                    customView,
                    DrawerLayout.LayoutParams.WRAP_CONTENT,
                    DrawerLayout.LayoutParams.WRAP_CONTENT
            );
            ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    mPopupWindow.dismiss();
                }
            });
            mPopupWindow.showAtLocation(this.getCurrentFocus(), Gravity.CENTER, 0, 0);
        }

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

    public void onClickToDataConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (verifyWifiDisabled()) {
            float limite_conso = getLimiteDataTrafic();
            Fragment fragment = FragmentTraffic.newInstance(limite_conso);
            this.fragment = FragmentUtils.showFragment(this, fragment,
                    R.anim.circle_zoom, R.anim.circle_inverse_zoom);
        }
    }

    public void onClickToBatterieConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (verifyWifiDisabled()) {
            int limite_batterie = getLimiteBatterie();
            Fragment fragment = FragmentBatterie.newInstance(limite_batterie);
            this.fragment = FragmentUtils.showFragment(this, fragment,
                    R.anim.circle_zoom, R.anim.circle_inverse_zoom);
        }
    }

    public void onClickToTimeConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (verifyWifiDisabled()) {
            long limite_temps = getLimiteTemps();
            Fragment fragment = FragmentTime.newInstance(limite_temps);
            this.fragment = FragmentUtils.showFragment(this, fragment,
                    R.anim.circle_zoom, R.anim.circle_inverse_zoom);
        }
    }

    public void onClickToResetDataBase(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if (verifyWifiDisabled()) {
            View view = LayoutInflater.from(this).
                    inflate(R.layout.app_progress_layout, null, false);
            View surfaceView = view.findViewById(R.id.progress);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            builder.setCancelable(false);
            AlertDialog alert = builder.create();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alert.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            surfaceView.setTag(alert);
            alert.show();
            alert.getWindow().setAttributes(lp);
            alert.getWindow().setBackgroundDrawable(new
                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            sqlManager.removeAllUtilisateur();
            sqlManager.removeAllConsommations();
            sqlManager.setConfigurationD(0);
            if(mServiceInterraction != null) {
                mServiceInterraction.resetBaseT0();
            }
        }
    }

    private void connectToService() {
        if (!isServiceRunning(ServiceNeOCampus.class)) {
            startService(serviceintent);
        }
        bindService(serviceintent, this, Context.BIND_AUTO_CREATE);
    }

    private void disconnectToService() {
        if (isServiceRunning(ServiceNeOCampus.class)) {
            if (mServiceInterraction != null) {
                mServiceInterraction.storeInDataBase();
            }
            unbindService(this);
        }
    }

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

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mServiceInterraction = null;
    }

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

    @Override
    public float getLimiteDataTrafic() {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        long consommation = tableConfiguration.getLimiteConsommation();
        float value = consommation / (1000.0f * 1000.0f * 1000.0f);
        return value;
    }

    @Override
    public int getLimiteBatterie() {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        if (tableConfiguration != null) {
            return tableConfiguration.getLimiteBatterie();
        }
        return BatterieUtils.BATTERIE_DEFAULT_LIMIT;
    }

    @Override
    public long getLimiteTemps() {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        return tableConfiguration.getLimiteTemps();
    }

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

    @Override
    public void postRequestDataTraffic() {
        if (mServiceInterraction != null) {
            if (fragment instanceof FragmentHome) {
                mServiceInterraction.peekDataTraffic((OnFragmentSetListener) fragment);
            }
        }
    }

    @Override
    public void postRequestTimeValue() {
        if (mServiceInterraction != null) {
            if (fragment instanceof FragmentHome) {
                mServiceInterraction.peekTimeValue((OnFragmentSetListener) fragment);
            }
        }
    }

    @Override
    public void hideAppBarRefresh() {
        mAppBarContent.findViewById(R.id.app_bar_refresh).setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAppBarRefresh() {
        mAppBarContent.findViewById(R.id.app_bar_refresh).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAppBarSaveConfig() {
        mAppBarContent.findViewById(R.id.app_bar_save_config).setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAppBarSaveConfig() {
        mAppBarContent.findViewById(R.id.app_bar_save_config).setVisibility(View.VISIBLE);
    }

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
        return true;
    }

    private boolean verifyWifiDisabled()
    {
        if (apControl.isEnabled()
                && apControl.isUPSWifiConfiguration()) {
            Toast.makeText(this, R.string.condition_shared, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

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

    private boolean isUPSWifiConfiguration(WifiConfiguration upsConfig) {
        WifiConfiguration
                configuration = apControl.getWifiApConfiguration();
        return WifiApControl.equals(configuration, upsConfig);
    }
}