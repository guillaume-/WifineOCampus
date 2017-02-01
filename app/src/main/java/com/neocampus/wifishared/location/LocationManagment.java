package com.neocampus.wifishared.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Guillaume RIPOLL on 26/01/2017.
 */
public class LocationManagment {
    private Location lastKnownLocation;
    private Location MetroUPS;
    private LocationListener locationListener;
    private LocationManager locationManager;
    Context context;
    private boolean isOk = true;

    public LocationManagment(Context c) {
        context = c;
        MetroUPS = new Location(LocationManager.GPS_PROVIDER);
        MetroUPS.setLatitude(43.5609901);
        MetroUPS.setLongitude(1.4630574000000252);
        try {
            locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        } catch (SecurityException e) {
            isOk = false;
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastKnownLocation = location;
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {
                isOk = true;
            }
            @Override
            public void onProviderDisabled(String provider) {
                isOk = false;
            }
        };
    }

    private boolean isAtUniversity(){
        if(isOk) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, locationListener);
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return lastKnownLocation.distanceTo(MetroUPS) <= 2000.f; //in meters
            } catch (SecurityException e) {
                return false;
            }
        }
        return false;
    }

}
