package com.users.map.rest.location;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ILocationService {

    @GET("/json")
    Call<LocationResponse> getLocationFromIP();

}
