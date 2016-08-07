package com.users.map.rest.users;

import com.users.map.storage.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IUsersService {

    @GET("/users")
    Call<List<User>> getUsers();

}
