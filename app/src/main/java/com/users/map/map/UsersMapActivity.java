package com.users.map.map;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

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

import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class UsersMapActivity extends BaseActivity implements IUsersMapVIew, OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_LOCATION = 263;
    private static final float ZOOM = 5;

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    public static final int FARTHEST_USER = 0;
    public static final int NEAREST_USER = 1;

    private GoogleMap googleMap;
    private IUsersMapPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_map);
        ButterKnife.bind(this);
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

    @OnClick(R.id.map_random_button)
    public void showRandomUser() {
        List<User> userList = presenter.getAllUsers();
        User randomUser = presenter.getRandomUser(userList);
        showUser(randomUser);
    }

    @OnClick(R.id.map_options_button)
    public void showOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.map_select_options);
        builder.setItems(R.array.map_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case FARTHEST_USER:
                        showFarthestUser();
                    break;
                    case NEAREST_USER:
                        showNearestUser();
                    break;
                }
            }
        });
        builder.create().show();
    }

    private void showNearestUser() {
        User user = presenter.getNearestUser();
        showUser(user);
    }

    private void showFarthestUser() {
        User user = presenter.getFarthestUser();
        showUser(user);
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
