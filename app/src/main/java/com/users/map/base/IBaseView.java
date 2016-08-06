package com.users.map.base;

import android.content.Context;

public interface IBaseView {
    Context getViewContext();
    void onNetworkError();
}
