package com.users.map.storage.model;

import io.realm.RealmObject;

public class Address extends RealmObject {

    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geolocation geo;

    public Address() {
    }

    public Address(String street, String suite, String city, String zipcode, Geolocation geo) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.geo = geo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Geolocation getGeo() {
        return geo;
    }

    public void setGeo(Geolocation geo) {
        this.geo = geo;
    }

    @Override
    public String toString() {
        return city + ": " + geo.getLat() + "," + geo.getLng();
    }
}
