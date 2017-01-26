package com.neocampus.wifishared.Location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Guillaume RIPOLL on 26/01/2017.
 */
public class LocationManagment {
    private boolean isAtUniversity;

    public LocationManagment() {
        try {
            isAtUniversity = LocationManager.getLastKnownLocation().distanceTo(new Location("118 Route de Narbonne, Toulouse")) <= 2000.f; //in meters
        }catch (SecurityException e){
            isAtUniversity = false;
        }
    }
}
