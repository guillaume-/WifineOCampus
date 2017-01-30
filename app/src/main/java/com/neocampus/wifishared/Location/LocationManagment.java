package com.neocampus.wifishared.Location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Guillaume RIPOLL on 26/01/2017.
 */
public class LocationManagment {
    private Location lastKnownLocation;
    private Location UPS;
    private LocationManager locationManager;

    public LocationManagment(Context c) {
        try {
            locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            UPS = new Location(LocationManager.GPS_PROVIDER);
            UPS.setLatitude(43.5609901);
            UPS.setLongitude(1.4630574000000252);
        } catch (SecurityException e) {
        }
    }

    private boolean isAtUniversity(){
        try{
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).distanceTo(new Location("118 Route de Narbonne, Toulouse")) <= 2000.f; //in meters
        } catch (SecurityException e) {
            return false;
        }
    }

}
