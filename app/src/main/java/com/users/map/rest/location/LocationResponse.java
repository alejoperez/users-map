package com.users.map.rest.location;

import com.google.gson.annotations.SerializedName;

public class LocationResponse {

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lon")
    private double longitude;

    public LocationResponse() {
    }

    public LocationResponse(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
