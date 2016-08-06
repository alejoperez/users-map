package com.users.map.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.users.map.R;

public class BaseActivity extends AppCompatActivity implements IBaseView {

    @Override
    public void onNetworkError() {
        Toast.makeText(this, R.string.network_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

}
