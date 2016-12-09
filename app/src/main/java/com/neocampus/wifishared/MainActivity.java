package com.neocampus.wifishared;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {

    public static int CODE_WRITE_SETTINGS_PERMISSION = 1555;
    private WifiConfiguration defaultWifiConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("*** Création ***********************");

        this.defaultWifiConfiguration = getDefaultWifiConfiguration();
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        final TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(this.defaultWifiConfiguration.SSID);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiApControl apControl = WifiApControl.getInstance(MainActivity.this);
                if(defaultWifiConfiguration.SSID.equals(apControl.getConfiguration().SSID))
                {
                    if (apControl.isWifiApEnabled()) {
                        apControl.disable();
                    }
                    apControl.setEnabled(getUPSWifiConfiguration(), true);
                    tv.setText(apControl.getConfiguration().SSID);
                }
                else
                {
                    if (apControl.isWifiApEnabled()) {
                        apControl.disable();
                    }
                    apControl.setEnabled(defaultWifiConfiguration, true);
                    tv.setText(defaultWifiConfiguration.SSID);
                    apControl.disable();
                }
            }
        });



//        List<WifiApControl.Client> clients = apControl.getReachableClients(1000,
//                new WifiApControl.ReachableClientListener() {
//            @Override
//            public void onReachableClient(WifiApControl.Client c) {
//                System.out.println("*** " + c);
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println("*** Complete" );
//            }
//        });
//
//        System.out.println("*** " + clients);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("*** Destroyed " );
        WifiApControl apControl = WifiApControl.getInstance(this);
        if (apControl != null) {
            apControl.disable();
            apControl.setEnabled(defaultWifiConfiguration, false);
        }
        System.out.println("*** Restore " + defaultWifiConfiguration);

    }

    private WifiConfiguration getUPSWifiConfiguration() {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "UPS-Shared   ıllıllı";
        wifiConfiguration.preSharedKey = "UPS-Shared   ıllıllı";
        wifiConfiguration.wepKeys[0] = "UPS-Shared   ıllıllı";
        wifiConfiguration.hiddenSSID = false;

        wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        return wifiConfiguration;
    }

    private WifiConfiguration getDefaultWifiConfiguration() {
        WifiManager wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
        Method[] methods = wifimanager.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals("getWifiApConfiguration")) {
                try {
                    WifiConfiguration config = (WifiConfiguration) m.invoke(wifimanager);
                    return config;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(this);
        } else {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_SETTINGS},
                    MainActivity.CODE_WRITE_SETTINGS_PERMISSION);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_WRITE_SETTINGS_PERMISSION
                && Settings.System.canWrite(this)) {
            int i = 0;
            int s = i;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_WRITE_SETTINGS_PERMISSION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do your code
            int i = 0;
            int s = i;
        }
    }

}
