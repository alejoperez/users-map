package com.users.map.map;

import com.users.map.repositories.location.LocationRepository;
import com.users.map.repositories.users.UserRepository;
import com.users.map.storage.model.User;

import java.util.List;
import java.util.Random;

public class UsersMapPresenter implements IUsersMapPresenter {

    private IUsersMapView usersMapView;

    public UsersMapPresenter(IUsersMapView usersMapView) {
        this.usersMapView = usersMapView;
    }

    @Override
    public void loadCurrentLocationFromIP() {
        LocationRepository.getInstance(usersMapView.getViewContext()).initLocationFromIP();
    }

    @Override
    public void loadUsersFromServer() {
        UserRepository.getInstance(usersMapView.getViewContext()).getUsersFromServer(this);
    }

    @Override
    public List<User> getAllUsers() {
        return UserRepository.getInstance(usersMapView.getViewContext()).getUsers();
    }

    @Override
    public User getFarthestUser() {
        return UserRepository.getInstance(usersMapView.getViewContext()).getFarthestUser();
    }

    @Override
    public User getNearestUser() {
        return UserRepository.getInstance(usersMapView.getViewContext()).getNearestUser();
    }

    @Override
    public User getRandomUser(List<User> userList) {
        int size = userList.size();
        return userList.get(new Random().nextInt(size));
    }

    @Override
    public void onUsersSuccess(List<User> userList) {
        usersMapView.onUsersSuccess(userList);
    }

    @Override
    public void onUsersFail() {
        usersMapView.onUsersFail();
    }
}
