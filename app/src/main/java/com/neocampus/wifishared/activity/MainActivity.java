package com.neocampus.wifishared.activity;

import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import com.neocampus.wifishared.utils.WifiApControl;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Home.OnFragmentInteractionListener {

    private WifiApControl apControl;
    private WifiConfiguration defaultWifiConfiguration;

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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

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
            case "Lancer le partage":
                if (apControl.isWifiApEnabled()) {
                    apControl.disable();
                }
                apControl.setEnabled(getUPSWifiConfiguration(), true);
                button.setText("Arrêter le partage");
                break;
            case "Arrêter le partage":
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
}
