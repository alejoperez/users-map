package com.users.map.map;

import com.users.map.base.IBaseView;
import com.users.map.storage.model.User;

import java.util.List;

public interface IUsersMapVIew extends IBaseView {
    void init();
    void initView();
    void onUsersSuccess(List<User> userList);
    void onUsersFail();

}
