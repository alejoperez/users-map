package com.users.map.map;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
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
    private static final float ZOOM = 14;

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
        User user = presenter.getRandomUser(userList);
        showUser(user);
    }

    private void showUser(User user) {
        LatLng latLng = new LatLng(user.getAddress().getGeo().getLat(), user.getAddress().getGeo().getLng());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(ZOOM).build();

        MarkerOptions marker = new MarkerOptions();
        marker.title(user.getName());
        marker.snippet(user.getAddress().toString());
        marker.position(new LatLng(user.getAddress().getGeo().getLat(), user.getAddress().getGeo().getLng()));

        googleMap.addMarker(marker);

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onUsersFail() {
        onNetworkError();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        presenter.loadUsersFromServer();
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
