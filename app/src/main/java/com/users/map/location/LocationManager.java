package com.users.map.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.users.map.repositories.location.LocationRepository;
import com.users.map.storage.model.UserLocation;

public class LocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final int INTERVAL_IN_MILLIS = 30000;
    public static final int FASTEST_INTERVAL_IN_MILLIS = 10000;
    public static final float MINIMUM_DISTANCE_CHANGE = 10;

    private static LocationManager locationManager;

    private Context context;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;

    public static LocationManager getInstance(Context context) {
        if (locationManager == null) {
            locationManager = new LocationManager(context);
        }
        return locationManager;
    }

    private LocationManager(Context context) {
        this.context = context;
    }

    public synchronized void init() {
        createLocationRequest();
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL_IN_MILLIS);
        locationRequest.setSmallestDisplacement(MINIMUM_DISTANCE_CHANGE);
        locationRequest.setFastestInterval(FASTEST_INTERVAL_IN_MILLIS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        if (isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    private void stopLocationUpdates() {
        if (isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            disconnectGoogleApiClient();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (isConnected()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                saveLocation(lastLocation);
                stopLocationUpdates();
            } else {
                startLocationUpdates();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        saveLocation(lastLocation);
        stopLocationUpdates();
    }

    private void saveLocation(Location lastLocation) {
        UserLocation newUserLocation = new UserLocation(lastLocation.getLatitude(),lastLocation.getLongitude());
        LocationRepository.getInstance(context).updateLocation(newUserLocation);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        connectGoogleApiClient();
    }

    @Override
    public void onConnectionSuspended(int i) {
        connectGoogleApiClient();
    }

    private void connectGoogleApiClient() {
        if (!isConnected()) {
            googleApiClient.connect();
        }
    }

    private boolean isConnected() {
        return googleApiClient != null && googleApiClient.isConnected();
    }

    private void disconnectGoogleApiClient() {
        if (isConnected()) {
            googleApiClient.disconnect();
        }
    }
}
