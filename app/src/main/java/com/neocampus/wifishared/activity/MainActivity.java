package com.neocampus.wifishared.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Button;

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
import com.neocampus.wifishared.observables.BatterieObservable;
import com.neocampus.wifishared.observables.ClientObservable;
import com.neocampus.wifishared.observables.DataObservable;
import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.services.ServiceNeOCampus;
import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.manage.SQLManager;
import com.neocampus.wifishared.utils.BatterieUtils;
import com.neocampus.wifishared.utils.ParcelableUtils;
import com.neocampus.wifishared.utils.WifiApControl;
import com.neocampus.wifishared.utils.test;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.setSupportActionBar(toolbar);

        /*Check if permission is enabled for wifi configuration*/
        if (WifiApControl.checkPermission(this, true)) {

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

            this.fragment = showInstance(FragmentHome.class);

            serviceintent = new Intent(this, ServiceNeOCampus.class);
            connectToService();

            test.test(this);

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
                fragment = showInstance(FragmentHome.class);
            }
        } else if (id == R.id.nav_users) {
            if (fragment.getClass() != FragmentUsers.class) {
                fragment = showInstance(FragmentUsers.class);
            }
        } else if (id == R.id.nav_setting) {
            if (fragment.getClass() != FragmentSettings.class) {
                fragment = showInstance(FragmentSettings.class);
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
        if (sqlManager != null) {
            sqlManager.close();
        }
        disconnectToService();
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
            }
        }
        super.onBackPressed();
        fragment = getVisibleFragment();
        if (fragment != null && fragment instanceof OnFragmentSetListener) {
            ((OnFragmentSetListener) fragment).onRefreshAllConfig();
        }
    }

    public void onClickToRunAPWifi(View v) {

        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        Button button = ((Button) v);

        WifiConfiguration configuration
                = WifiApControl.getUPSWifiConfiguration();

        if (isUPSWifiConfiguration(configuration)) {
            if (apControl.isWifiApEnabled()) {
                apControl.disable();
                button.setText("Lancer le Partage");

            } else {
                apControl.setEnabled(configuration, true);
                button.setText("Arrêter le Partage");
            }
        } else {
            WifiConfiguration userConfiguration = apControl.getConfiguration();
            byte[] bytes = ParcelableUtils.marshall(userConfiguration);
            sqlManager.setConfiguration(bytes);
            apControl.setEnabled(configuration, true);
            button.setText("Arrêter le Partage");
        }
    }

    public void onClickToDataConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        float limite_conso = getLimiteDataTrafic();
        Fragment fragment = FragmentTraffic.newInstance(limite_conso);
        this.fragment = showInstance(fragment,
                R.anim.circle_zoom, R.anim.circle_inverse_zoom);
    }

    public void onClickToBatterieConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        int limite_batterie = getLimiteBatterie();
        Fragment fragment = FragmentBatterie.newInstance(limite_batterie);
        this.fragment = showInstance(fragment,
                R.anim.circle_zoom, R.anim.circle_inverse_zoom);
    }

    public void onClickToTimeConfig(final View v) {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        long limite_temps = getLimiteTemps();
        Fragment fragment = FragmentTime.newInstance(limite_temps);
        this.fragment = showInstance(fragment,
                R.anim.circle_zoom, R.anim.circle_inverse_zoom);
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
                mServiceInterraction.forceSave();
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
                listener.onRefreshHotpostState((Boolean) newValue);
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
        if(mServiceInterraction != null){
            if(fragment instanceof FragmentHome){
                mServiceInterraction.peekAllClients((OnReachableClientListener) fragment, true);
            } else if(fragment instanceof FragmentUsers){
                mServiceInterraction.peekAllClients((OnReachableClientListener) fragment, false);
            }
        }
    }

    @Override
    public void postRequestDataTraffic()
    {
        if(mServiceInterraction != null){
            if(fragment instanceof FragmentHome){
                mServiceInterraction.peekDataTraffic((OnFragmentSetListener) fragment);
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

    private Fragment showInstance(Class<?> aClass, Integer... animations) {
        /*Check if already create*/
        List<Fragment> fragments =
                getSupportFragmentManager().getFragments();
        Fragment fragment = null;
        if (fragments != null) {
            for (Fragment fragmentStored : fragments) {
                if (fragmentStored != null && fragmentStored.getClass() == aClass) {
                    fragment = fragmentStored;
                    break;
                }
            }
        }
        if (fragment == null) {
            try {
                fragment = (Fragment) aClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        // We can also animate the changing of fragment.
        if (animations.length == 0) {
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (animations.length == 1) {
            ft.setCustomAnimations(animations[0], animations[0]);
        } else {
            ft.setCustomAnimations(animations[0], animations[1]);
        }
        // Replace current fragment by the new one.
        ft.replace(R.id.iDFragmentShowing, fragment);
        // Null on the back stack to return on the previous fragment when user
        // press on back button.
        ft.addToBackStack(null);
        // Commit changes.
        ft.commit();
        return fragment;
    }

    private Fragment showInstance(Fragment fragment, Integer... animations) {
        /*Check if already create*/
        if (fragment == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        // We can also animate the changing of fragment.
        if (animations.length == 0) {
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (animations.length == 1) {
            ft.setCustomAnimations(animations[0], animations[0]);
        } else {
            ft.setCustomAnimations(animations[0], animations[1]);
        }
        // Replace current fragment by the new one.
        ft.replace(R.id.iDFragmentShowing, fragment);
        // Null on the back stack to return on the previous fragment when user
        // press on back button.
        ft.addToBackStack(null);
        // Commit changes.
        ft.commit();
        return fragment;
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }


}
