package com.users.map.storage.model;

import io.realm.RealmObject;

public class Geolocation extends RealmObject {

    private double lat;
    private double lng;

    public Geolocation() {
    }

    public Geolocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
