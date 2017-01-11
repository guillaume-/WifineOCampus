package com.neocampus.wifishared.activity;

import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.fragments.Home;
import com.neocampus.wifishared.fragments.Users;
import com.neocampus.wifishared.listeners.OnFragmentInteractionListener;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private WifiApControl apControl;
    private WifiConfiguration defaultWifiConfiguration;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        this.apControl = WifiApControl.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (apControl != null) {
            defaultWifiConfiguration = apControl.getWifiApConfiguration();
            fragment = showInstance(Home.class);
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
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if(fragment.getClass() != Home.class) {
                fragment = showInstance(Home.class);
            }
        } else if (id == R.id.nav_users) {
            if(fragment.getClass() != Users.class) {
                fragment = showInstance(Users.class);
            }
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickToRunAPWifi(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.pressed_anim));
        Button button = ((Button) v);
        /*String action = button.getText().toString();
        Snackbar.make(v, action, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
        switch (button.getText().toString()) {
            case "Activer le Partage":
                if (apControl.isWifiApEnabled()) {
                    apControl.disable();
                }
                apControl.setEnabled(getUPSWifiConfiguration(), true);
                button.setText("Arrêter le Partage");
                break;
            case "Arrêter le Partage":
                if (apControl.isWifiApEnabled()) {
                    apControl.disable();
                }
                apControl.setEnabled(defaultWifiConfiguration, true);
                apControl.disable();
                button.setText("Lancer le partage");
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public int getClientCount() {
        List<WifiApControl.Client> clients
                = apControl.getClients();
        if(clients == null)
            return 0;
        return clients.size();
    }

    @Override
    public List<WifiApControl.Client> getClients() {
        List<WifiApControl.Client>
                clients = apControl.getClients();
        if(clients == null)
            return new ArrayList<>() ;
        return clients;
    }


    private WifiConfiguration getUPSWifiConfiguration() {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "Wifi neOCampus   ıllıllı";
        wifiConfiguration.preSharedKey = "Wifi neOCampus   ıllıllı";
        wifiConfiguration.wepKeys[0] = "Wifi neOCampus   ıllıllı";
        wifiConfiguration.hiddenSSID = false;

        wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        return wifiConfiguration;
    }

    private Fragment showInstance(Class<?> aClass) {
        /*Check if already create*/
        List<Fragment> fragments =
                getSupportFragmentManager().getFragments();
        Fragment fragment = null;
        if (fragments != null)
        {
            for (Fragment fragmentStored : fragments) {
                if (fragmentStored.getClass() == aClass) {
                    fragment = fragmentStored;
                    break;
                }
            }
        }
        if(fragment == null)
        {
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
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        // Replace current fragment by the new one.
        ft.replace(R.id.iDFragmentShowing, fragment);
        // Null on the back stack to return on the previous fragment when user
        // press on back button.
        ft.addToBackStack(null);
        // Commit changes.
        ft.commit();
        return fragment;
    }
}
