package com.users.map.map;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.users.map.R;
import com.users.map.base.BaseActivity;
import com.users.map.location.LocationManager;
import com.users.map.storage.model.User;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class UsersMapActivity extends BaseActivity implements IUsersMapVIew, OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_LOCATION = 263;

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private GoogleMap googleMap;

    private IUsersMapPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_map);
        init();
        initView();
    }

    @Override
    public void init() {
        presenter = new UsersMapPresenter(this);
        retrieveLocation();
    }

    private void retrieveLocation() {
        presenter.loadCurrentLocationFromIP();
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            LocationManager.getInstance(this).init();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.map_require_location), REQUEST_CODE_LOCATION, PERMISSIONS);
        }
    }

    @Override
    public void initView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onUsersSuccess(List<User> userList) {
        //TODO: process users
    }

    @Override
    public void onUsersFail() {
        onNetworkError();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;


        //TODO: show random user
        LatLng sydney = new LatLng(-34, 151);
        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            LocationManager.getInstance(this).init();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {

    }
}
