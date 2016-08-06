package com.users.map.repositories.users;


import com.users.map.map.IUsersMapPresenter;
import com.users.map.storage.model.User;

import java.util.List;

public interface IUserRepository {
    void getUsersFromServer(IUsersMapPresenter presenter);
    User getNearestUser();
    User getFarthestUser();
    void saveUsers(List<User> userList);
}
