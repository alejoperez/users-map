package com.users.map.repositories.users;

import android.content.Context;

import com.users.map.map.IUsersMapPresenter;
import com.users.map.rest.ServiceGenerator;
import com.users.map.rest.users.IUsersService;
import com.users.map.storage.database.RealmManager;
import com.users.map.storage.model.User;

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
    public User getFarthestUser() {
        //TODO:
        return null;
    }

    @Override
    public User getNearestUser() {
        //TODO:
        return null;
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
