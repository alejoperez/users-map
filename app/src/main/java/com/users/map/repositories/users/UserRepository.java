package com.users.map.repositories.users;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.users.map.map.IUsersMapPresenter;
import com.users.map.repositories.location.LocationRepository;
import com.users.map.rest.ServiceGenerator;
import com.users.map.rest.users.IUsersService;
import com.users.map.storage.database.RealmManager;
import com.users.map.storage.model.User;
import com.users.map.storage.model.UserLocation;
import com.users.map.util.GeoUtil;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository implements IUserRepository {

    private Context context;
    private static UserRepository userRepository;

    public static UserRepository getInstance(Context context) {
        if (userRepository == null) {
            userRepository = new UserRepository(context.getApplicationContext());
        }
        return userRepository;
    }

    private UserRepository(Context context) {
        this.context = context;
    }

    @Override
    public void getUsersFromServer(final IUsersMapPresenter presenter) {
        IUsersService userService = ServiceGenerator.createService(IUsersService.class);
        Call<List<User>> call = userService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    saveUsers(response.body());
                    presenter.onUsersSuccess(response.body());
                } else {
                    presenter.onUsersFail();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                presenter.onUsersFail();
            }
        });
    }

    @Override
    public List<User> getUsers() {
        return RealmManager.getInstance(context).getRealm().where(User.class).findAll();
    }

    @Override
    public User getFarthestUser() {
        UserLocation currentLocation = LocationRepository.getInstance(context).getLocation();
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        List<User> userList = getUsers();

        User farthestUser = null;
        float distance = 0;

        for(User user : userList) {
            LatLng latLng = new LatLng(user.getAddress().getGeo().getLat(),user.getAddress().getGeo().getLng());
            float userDistance = GeoUtil.getDistanceBetweenLocationsInMeters(currentLatLng,latLng);
            if (userDistance > distance) {
                distance = userDistance;
                farthestUser = user;
            }
        }

        return farthestUser;
    }

    @Override
    public User getNearestUser() {
        UserLocation currentLocation = LocationRepository.getInstance(context).getLocation();
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        List<User> userList = getUsers();

        User nearestUser = null;
        float distance = Float.MAX_VALUE;

        for(User user : userList) {
            LatLng latLng = new LatLng(user.getAddress().getGeo().getLat(),user.getAddress().getGeo().getLng());
            float userDistance = GeoUtil.getDistanceBetweenLocationsInMeters(currentLatLng,latLng);
            if (userDistance < distance) {
                distance = userDistance;
                nearestUser = user;
            }
        }

        return nearestUser;
    }

    @Override
    public void saveUsers(final List<User> userList) {
        RealmManager.getInstance(context).getRealm().executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(userList);
                    }
                }
        );
    }
}
