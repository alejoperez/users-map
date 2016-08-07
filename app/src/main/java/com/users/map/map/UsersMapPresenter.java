package com.users.map.map;

import com.users.map.repositories.location.LocationRepository;
import com.users.map.repositories.users.UserRepository;
import com.users.map.storage.model.User;

import java.util.List;
import java.util.Random;

public class UsersMapPresenter implements IUsersMapPresenter {

    private IUsersMapView usersMapVIew;

    public UsersMapPresenter(IUsersMapView usersMapVIew) {
        this.usersMapVIew = usersMapVIew;
    }

    @Override
    public void loadCurrentLocationFromIP() {
        LocationRepository.getInstance(usersMapVIew.getViewContext()).initLocationFromIP();
    }

    @Override
    public void loadUsersFromServer() {
        UserRepository.getInstance(usersMapVIew.getViewContext()).getUsersFromServer(this);
    }

    @Override
    public List<User> getAllUsers() {
        return UserRepository.getInstance(usersMapVIew.getViewContext()).getUsers();
    }

    @Override
    public User getFarthestUser() {
        return UserRepository.getInstance(usersMapVIew.getViewContext()).getFarthestUser();
    }

    @Override
    public User getNearestUser() {
        return UserRepository.getInstance(usersMapVIew.getViewContext()).getNearestUser();
    }

    @Override
    public User getRandomUser(List<User> userList) {
        int size = userList.size();
        return userList.get(new Random().nextInt(size));
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
