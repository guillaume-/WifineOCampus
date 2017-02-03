package com.neocampus.wifishared.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

/**
 * Created by Guillaume RIPOLL on 26/01/2017.
 */
public class LocationManagment {
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
        } catch (SecurityException e) {}
    }

    public boolean isAtUniversity(){
        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!isGPSEnabled && !isNetworkEnabled)
            return false;
        else{
            Location loc = find_Location();
            if(loc != null)
                return loc.distanceTo(MetroUPS) <= 2000.f; //in meters
            else
                return false;
        }
    }

    public Location find_Location() {
        Location loc = null;
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
}
