package com.users.map.repositories.location;

import android.content.Context;

import com.users.map.BuildConfig;
import com.users.map.rest.location.ILocationService;
import com.users.map.rest.location.LocationResponse;
import com.users.map.storage.database.RealmManager;
import com.users.map.storage.model.UserLocation;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationRepository implements ILocationRepository {

    private static LocationRepository locationRepository;

    private Context context;

    public static LocationRepository getInstance(Context context) {
        if (locationRepository == null) {
            locationRepository = new LocationRepository(context.getApplicationContext());
        }
        return locationRepository;
    }

    private LocationRepository(Context context) {
        this.context = context;
    }

    @Override
    public void initLocationFromIP() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(BuildConfig.IP_LOCATION_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();
        ILocationService locationService = retrofit.create(ILocationService.class);
        Call<LocationResponse> call = locationService.getLocationFromIP();
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()) {
                    LocationResponse body = response.body();
                    updateLocation(new UserLocation(body.getLatitude(), body.getLongitude()));
                } else {
                    updateLocation(new UserLocation());
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                updateLocation(new UserLocation());
            }
        });
    }

    @Override
    public UserLocation getLocation() {
        UserLocation location = RealmManager.getInstance(context).getRealm().where(UserLocation.class).findFirst();

        if (location == null) {
            location = new UserLocation();
        }

        return location;
    }

    @Override
    public void updateLocation(final UserLocation newLocation) {
        RealmManager.getInstance(context).getRealm().executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(newLocation);
                    }
                }
        );
    }
}
