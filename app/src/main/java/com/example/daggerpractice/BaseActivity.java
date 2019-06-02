package com.example.daggerpractice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.daggerpractice.models.User;
import com.example.daggerpractice.ui.auth.AuthActivity;
import com.example.daggerpractice.ui.auth.AuthResource;

import androidx.lifecycle.Observer;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Inject
    public SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subscribeObserver();
    }

    private void subscribeObserver(){
        sessionManager.getAuthUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                switch (userAuthResource.status){
                    case LOADING:
                        break;

                    case AUTHENTICATED:
                        Log.d(TAG, "onChanged: LOGIN SUCCESS: " + userAuthResource.data.getEmail());
                        break;

                    case ERROR:

                        Log.d(TAG, "onChanged: LOGIN FAILED: " + userAuthResource.message);
                        break;

                    case NOT_AUTHENTICATED:
                        navLoginScreen();
                        break;
                }
            }
        });
    }

    private void navLoginScreen(){
        Intent intentToNaviagteToLOginScreen = new Intent(this, AuthActivity.class);
        startActivity(intentToNaviagteToLOginScreen);
        finish();
    }
}
