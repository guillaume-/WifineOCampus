package com.neocampus.wifishared.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Created by Guillaume RIPOLL on 26/01/2017.
 */
public class LocationManagment implements LocationListener {

    private Location MetroUPS;
    private LocationManager locationManager;
    Context context;

    public LocationManagment(Context c) {
        context = c;
        MetroUPS = new Location("");
        MetroUPS.setLatitude(43.5609901);
        MetroUPS.setLongitude(1.4630574000000252);
        try {
            locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        } catch (SecurityException e) {
        }
    }

    public boolean isEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isAtUniversity() {
        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled)
            return false;
        else {
            Location loc = find_Location();
            if (loc != null) {
                float distance = loc.distanceTo(MetroUPS);
                boolean res = distance <= 2000.f; //in meters
                return res;
            } else
                return false;
        }
    }

    public Location find_Location() {
        Location loc = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return loc;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        List<String> providers = locationManager.getProviders(true);
        try {
            for (String provider : providers) {
                locationManager.requestLocationUpdates(provider, 1000, 0, new LocationListener() {
                    public void onLocationChanged(Location location) {}
                    public void onProviderDisabled(String provider) {}
                    public void onProviderEnabled(String provider) {}
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                });
                loc = locationManager.getLastKnownLocation(provider);
                if( loc != null)
                    return loc;
            }
        }catch(SecurityException e){}
        return loc;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
