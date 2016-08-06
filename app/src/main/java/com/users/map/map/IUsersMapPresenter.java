package com.users.map.map;

import com.users.map.storage.model.User;

import java.util.List;

public interface IUsersMapPresenter {
    void loadCurrentLocationFromIP();
    void getUsers();
    void onUsersSuccess(List<User> userList);
    void onUsersFail();
}
