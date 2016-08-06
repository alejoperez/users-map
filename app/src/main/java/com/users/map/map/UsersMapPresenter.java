package com.users.map.map;

import com.users.map.repositories.location.LocationRepository;
import com.users.map.repositories.users.UserRepository;
import com.users.map.storage.model.User;

import java.util.List;

public class UsersMapPresenter implements IUsersMapPresenter {

    private IUsersMapVIew usersMapVIew;

    public UsersMapPresenter(IUsersMapVIew usersMapVIew) {
        this.usersMapVIew = usersMapVIew;
    }

    @Override
    public void loadCurrentLocationFromIP() {
        LocationRepository.getInstance(usersMapVIew.getViewContext()).initLocationFromIP();
    }

    @Override
    public void getUsers() {
        UserRepository.getInstance(usersMapVIew.getViewContext()).getUsersFromServer(this);
    }

    @Override
    public void onUsersSuccess(List<User> userList) {
        usersMapVIew.onUsersSuccess(userList);
    }

    @Override
    public void onUsersFail() {
        usersMapVIew.onUsersFail();
    }
}
