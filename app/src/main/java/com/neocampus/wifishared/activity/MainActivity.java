package com.neocampus.wifishared.activity;

import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Bundle;
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
import com.neocampus.wifishared.fragments.Home;
import com.neocampus.wifishared.fragments.Users;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentSetListener;
import com.neocampus.wifishared.listeners.OnReachableClientListener;
import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.manage.SQLManager;
import com.neocampus.wifishared.utils.BatterieUtils;
import com.neocampus.wifishared.utils.ParcelableUtil;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnActivitySetListener {

    private SQLManager sqlManager;
    private WifiApControl apControl;
    private Fragment fragment = null;

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

            View bar_content = LayoutInflater.from(this)
                    .inflate(R.layout.app_bar_content, null, false);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            toolbar.addView(bar_content, params);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            this.fragment = showInstance(Home.class);

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

    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (fragment.getClass() != Home.class) {
                fragment = showInstance(Home.class);
            }
        } else if (id == R.id.nav_users) {
            if (fragment.getClass() != Users.class) {
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

    @Override
    protected void onDestroy() {
        if (sqlManager != null) {
            sqlManager.close();
        }
        super.onDestroy();
    }

    public void onClickToRefresh(final View v)
    {
        v.startAnimation(AnimationUtils.
                loadAnimation(v.getContext(), R.anim.pressed_anim));
        if(fragment instanceof OnFragmentSetListener) {
            ((OnFragmentSetListener) fragment).onRefreshNotify();
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
            byte[] bytes = ParcelableUtil.marshall(userConfiguration);
            sqlManager.setConfiguration(bytes);
            apControl.setEnabled(configuration, true);
            button.setText("Arrêter le Partage");
        }
    }

    @Override
    public List<WifiApControl.Client> getReachableClients(
            OnReachableClientListener listener) {
        List<WifiApControl.Client>
                clients = apControl.getReachableClients(1000, listener);
        if (clients == null)
            return new ArrayList<>();
        return clients;
    }

    @Override
    public int getLimiteBatterieLevel() {
        TableConfiguration tableConfiguration = sqlManager.getConfiguration();
        if(tableConfiguration != null) {
            return tableConfiguration.getLimiteBatterie();
        }
        return BatterieUtils.BATTERIE_DEFAULT_LIMIT;
    }

    @Override
    public int getCurrentBatterieLevel() {
        return (int) BatterieUtils.getBatteryLevel(this);
    }


    private boolean isUPSWifiConfiguration(WifiConfiguration upsConfig) {
        WifiConfiguration
                configuration = apControl.getWifiApConfiguration();
        return WifiApControl.equals(configuration, upsConfig);
    }

    private Fragment showInstance(Class<?> aClass) {
        /*Check if already create*/
        List<Fragment> fragments =
                getSupportFragmentManager().getFragments();
        Fragment fragment = null;
        if (fragments != null) {
            for (Fragment fragmentStored : fragments) {
                if (fragmentStored.getClass() == aClass) {
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
