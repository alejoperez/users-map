package com.users.map.storage.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserLocation extends RealmObject {

    public static final long UNIQUE_ID = 1;
    public static final double DEFAULT_LATITUDE = 4.718924;
    public static final double DEFAULT_LONGITUDE = -74.053745;

    @PrimaryKey
    private long id = UNIQUE_ID;
    private double latitude = DEFAULT_LATITUDE;
    private double longitude = DEFAULT_LONGITUDE;

    public UserLocation() {}

    public UserLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
