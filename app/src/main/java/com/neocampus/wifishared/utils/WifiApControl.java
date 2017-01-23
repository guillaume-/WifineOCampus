/**
 * Copyright 2015 Daniel Martí
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.neocampus.wifishared.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.neocampus.wifishared.listeners.OnReachableClientListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

// WifiApControl provides control over Wi-Fi APs using the singleton pattern.
// Even though isSupported should be reliable, the underlying hidden APIs that
// are obtained via reflection to provide the main features may not work as
// expected.
final public class WifiApControl extends Observable {

    private static final String TAG = "WifiApControl";

    private static Method getWifiApConfigurationMethod;
    private static Method getWifiApStateMethod;
    private static Method isWifiApEnabledMethod;
    private static Method setWifiApEnabledMethod;
    private static Method setWifiApConfiguration;

    static {
        for (Method method : WifiManager.class.getDeclaredMethods()) {
            switch (method.getName()) {
                case "getWifiApConfiguration":
                    getWifiApConfigurationMethod = method;
                    break;
                case "setWifiApConfiguration":
                    setWifiApConfiguration = method;
                    break;
                case "getWifiApState":
                    getWifiApStateMethod = method;
                    break;
                case "isWifiApEnabled":
                    isWifiApEnabledMethod = method;
                    break;
                case "setWifiApEnabled":
                    setWifiApEnabledMethod = method;
                    break;
            }
        }
    }



    public static int CODE_WRITE_SETTINGS_PERMISSION = 1555;

    public static final String ACTION_WIFI_AP_CHANGED = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";

    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_DISABLED  = 11;
    public static final int WIFI_AP_STATE_ENABLING  = 12;
    public static final int WIFI_AP_STATE_ENABLED   = 13;
    public static final int WIFI_AP_STATE_FAILED    = 14;

    public static final int STATE_DISABLING = WIFI_AP_STATE_DISABLING;
    public static final int STATE_DISABLED  = WIFI_AP_STATE_DISABLED;
    public static final int STATE_ENABLING  = WIFI_AP_STATE_ENABLING;
    public static final int STATE_ENABLED   = WIFI_AP_STATE_ENABLED;
    public static final int STATE_FAILED    = WIFI_AP_STATE_FAILED;

    private static boolean isSoftwareSupported() {
        return (getWifiApStateMethod != null
                && isWifiApEnabledMethod != null
                && setWifiApEnabledMethod != null
                && getWifiApConfigurationMethod != null);
    }

    private static boolean isHardwareSupported() {
        // TODO: implement via native code
        return true;
    }

    // isSupported reports whether Wi-Fi APs are supported by this device.
    public static boolean isSupported() {
        return isSoftwareSupported() && isHardwareSupported();
    }

    private static final String FALLBACK_DEVICE = "wlan0";

    private final WifiManager wm;
    private final String deviceName;

    private static WifiApControl instance = null;

    private WifiApControl(Context context) {
        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        deviceName = getDeviceName(wm);
    }

    // getInstance is a standard singleton instance getter, constructing
    // the actual class when first called.
    public static WifiApControl getInstance(Context context) {
        if (instance == null) {

            instance = new WifiApControl(context);
        }
        return instance;
    }

    public static boolean checkPermission(Context context, boolean... request)
    {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (!permission) {
            if (request.length > 0 && request[0]) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.WRITE_SETTINGS},
                            CODE_WRITE_SETTINGS_PERMISSION);
                }
            }
            return false;
        }
        return true;
    }

    public static WifiConfiguration getUPSWifiConfiguration() {

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

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static String getDeviceName(WifiManager wifiManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            Log.w(TAG, "Older device - falling back to the default device TableName: " + FALLBACK_DEVICE);
            return FALLBACK_DEVICE;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.w(TAG, "6.0 or later, unaccessible MAC - falling back to the default device TableName: " + FALLBACK_DEVICE);
            return FALLBACK_DEVICE;
        }

        String macString = wifiManager.getConnectionInfo().getMacAddress();
        if (macString == null) {
            Log.w(TAG, "MAC Address not found - Wi-Fi disabled? Falling back to the default device TableName: " + FALLBACK_DEVICE);
            return FALLBACK_DEVICE;
        }
        byte[] macBytes = macAddressToByteArray(macString);

        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface iface = ifaces.nextElement();

                byte[] hardwareAddress = iface.getHardwareAddress();
                if (hardwareAddress != null && Arrays.equals(macBytes, hardwareAddress)) {
                    return iface.getName();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }

        Log.w(TAG, "None found - falling back to the default device TableName: " + FALLBACK_DEVICE);
        return FALLBACK_DEVICE;
    }

    private static byte[] macAddressToByteArray(String macString) {
        String[] mac = macString.split("[:\\s-]");
        byte[] macAddress = new byte[6];
        for (int i = 0; i < mac.length; i++) {
            macAddress[i] = Integer.decode("0x" + mac[i]).byteValue();
        }
        return macAddress;
    }

    private static Object invokeQuietly(Method method, Object receiver, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    // isWifiApEnabled returns whether the Wi-Fi AP is currently enabled.
    // If an error occured invoking the method via reflection, false is
    // returned.
    public boolean isWifiApEnabled() {
        Object result = invokeQuietly(isWifiApEnabledMethod, wm);
        if (result == null) {
            return false;
        }
        return (Boolean) result;
    }

    // isEnabled is a commodity function alias for isWifiApEnabled.
    public boolean isEnabled() {
        return isWifiApEnabled();
    }

    // newStateNumber adapts the state constants to the current values in
    // the SDK. They were changed on 4.0 to have higher integer values.
    public static int newStateNumber(int state) {
        if (state < 10) {
            return state + 10;
        }
        return state;
    }

    // getWifiApState returns the current Wi-Fi AP state.
    // If an error occured invoking the method via reflection, -1 is
    // returned.
    public int getWifiApState() {
        Object result = invokeQuietly(getWifiApStateMethod, wm);
        if (result == null) {
            return -1;
        }
        return newStateNumber((Integer) result);
    }

    // getState is a commodity function alias for getWifiApState.
    public int getState() {
        return getWifiApState();
    }

    // getWifiApConfiguration returns the current Wi-Fi AP configuration.
    // If an error occured invoking the method via reflection, null is
    // returned.
    public WifiConfiguration getWifiApConfiguration() {
        Object result = invokeQuietly(getWifiApConfigurationMethod, wm);
        if (result == null) {
            return null;
        }
        return (WifiConfiguration) result;
    }

    public void setWifiApConfiguration(WifiConfiguration configuration) {
        invokeQuietly(setWifiApConfiguration, wm, configuration);
    }

    // getWifiConfiguration is a commodity function alias for
    // getWifiApConfiguration.
    public WifiConfiguration getConfiguration() {
        return getWifiApConfiguration();
    }

    // setWifiApEnabled starts a Wi-Fi AP with the specified
    // configuration. If one is already running, start using the new
    // configuration. You should call WifiManager.setWifiEnabled(false)
    // yourself before calling this method.
    // If an error occured invoking the method via reflection, false is
    // returned.
    public boolean setWifiApEnabled(WifiConfiguration config, boolean enabled) {
        Object result = invokeQuietly(setWifiApEnabledMethod, wm, config, enabled);
        if (result == null) {
            return false;
        }
        return (Boolean) result;
    }

    // setEnabled is a commodity function alias for setWifiApEnabled.
    public boolean setEnabled(WifiConfiguration config, boolean enabled) {
        return setWifiApEnabled(config, enabled);
    }

    // enable starts the currently configured Wi-Fi AP.
    public boolean enable() {
        return setEnabled(getConfiguration(), true);
    }

    // disable stops any currently running Wi-Fi AP.
    public boolean disable() {
        return setEnabled(null, false);
    }

    // getInet6Address returns the IPv6 address that the device has in its
    // own Wi-Fi AP local network. Will return null if no Wi-Fi AP is
    // currently enabled.
    public Inet6Address getInet6Address() {
        if (!isEnabled()) {
            return null;
        }
        return getInetAddress(Inet6Address.class);
    }

    // getInet4Address returns the IPv4 address that the device has in its
    // own Wi-Fi AP local network. Will return null if no Wi-Fi AP is
    // currently enabled.
    public Inet4Address getInet4Address() {
        if (!isEnabled()) {
            return null;
        }
        return getInetAddress(Inet4Address.class);
    }


    private <T extends InetAddress> T getInetAddress(Class<T> addressType) {
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface iface = ifaces.nextElement();

                if (!iface.getName().equals(deviceName)) {
                    continue;
                }

                Enumeration<InetAddress> addrs = iface.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();

                    if (addressType.isInstance(addr)) {
                        return addressType.cast(addr);
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    public static boolean equals(WifiConfiguration configuration1,
                                 WifiConfiguration configuration2)
    {
        byte[] bytes1 = ParcelableUtils.marshall(configuration1);
        byte[] bytes2 = ParcelableUtils.marshall(configuration2);
        return Arrays.equals(bytes1, bytes2);
    }

    public boolean isUPSWifiConfiguration()
    {
        WifiConfiguration
                configuration = getWifiApConfiguration();
        WifiConfiguration upsConfig = WifiApControl.getUPSWifiConfiguration();
        if (WifiApControl.equals(configuration, upsConfig)) {
            return true;
        }
        return false;
    }

    // Client describes a Wi-Fi AP device connected to the network.
    public static class Client {

        public boolean connected;
        // ipAddr is the raw string of the IP Address client
        public String ipAddr;

        // hwAddr is the raw string of the MAC of the client
        public String hwAddr;

        public long date_connected;

        public long date_disconnected;

        public Client(String ipAddr, String hwAddr) {
            this.ipAddr = ipAddr;
            this.hwAddr = hwAddr;
            this.connected = true;
            this.date_connected = 0;
            this.date_disconnected = 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Client) {
                return synchronised((Client) obj);
            }
            return super.equals(obj);
        }

        private boolean synchronised(Client client)
        {
            if(client.hwAddr.equals(hwAddr)) {
                long date_connected = this.date_connected == 0 ? client.date_connected : this.date_connected;
                long date_disconnected = this.date_disconnected < client.date_disconnected ?
                        client.date_disconnected : this.date_disconnected;
                this.date_connected = date_connected;
                client.date_connected = date_connected;
                this.date_disconnected = date_disconnected;
                client.date_disconnected = date_disconnected;
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    // getReachableClients returns a list of all clients connected to the network.
    // Since the information is pulled from ARP, which is cached for up to
    // five minutes, this method may yield clients that disconnected
    // minutes ago.
    public List<Client> getClients() {
        if (!isEnabled()) {
            return null;
        }
        List<Client> result = new ArrayList<>();

        // Basic sanity checks
        Pattern macPattern = Pattern.compile("..:..:..:..:..:..");

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" +");
                if (parts.length < 6) {
                    continue;
                }

                String ipAddr = parts[0];
                String hwAddr = parts[3].toUpperCase();
                String device = parts[5];

                if (!device.equals(deviceName)) {
                    continue;
                }

                if (!macPattern.matcher(parts[3]).find()) {
                    continue;
                }

                result.add(new Client(ipAddr, hwAddr));
            }
        } catch (IOException e) {
            Log.e(TAG, "", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
        }

        return result;
    }

    // getReachableClients fetches the clients connected to the network
    // much like getReachableClients, but only those which are reachable. Since
    // checking for reachability requires network I/O, the reachable
    // clients are returned via callbacks. All the clients are returned
    // like in getReachableClients so that the callback returns a subset of the
    // same objects.
    public List<Client> getReachableClients(final int timeout,
                                            final OnReachableClientListener listener) {
        final List<Client> clients = getClients();
        if (clients == null) {
            return null;
        }
        final CountDownLatch latch = new CountDownLatch(clients.size());
        ExecutorService es = Executors.newCachedThreadPool();
        for (final Client c : clients) {
            es.submit(new Runnable() {
                public void run() {
                    try {
                        InetAddress ip = InetAddress.getByName(c.ipAddr);
                        if (ip.isReachable(timeout)) {
//                            listener.onReachableClient(c);
                        }
                        else {
                            clients.remove(c);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "", e);
                    }
                    latch.countDown();
                }
            });
        }
        new Thread() {
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Log.e(TAG, "", e);
                }
                listener.onReachableClients(clients);
            }
        }.start();
        return clients;
    }
}
