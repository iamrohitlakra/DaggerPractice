package com.example.daggerpractice.ui.auth;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerAppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.RequestManager;
import com.example.daggerpractice.R;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.ui.main.MainActivity;
import com.example.daggerpractice.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    private static final String TAG = AuthActivity.class.getSimpleName();
    private AuthViewModel viewModel;

    private ProgressBar progressBar;

    private EditText userId;
    private Button login;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    Drawable logo;

    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        viewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel.class);
        progressBar = findViewById(R.id.progress_bar);
        login=findViewById(R.id.login_button);

        login.setOnClickListener(this);

        userId = findViewById(R.id.user_id_input);

        setLogo();

        subscribeObserver();
    }

    private void setLogo(){
        requestManager.load(logo).into((ImageView) findViewById(R.id.login_logo));
    }

    private void subscribeObserver(){
        viewModel.observeAuthState().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                switch (userAuthResource.status){
                    case LOADING:
                        showProgressBar(true);
                        break;
                    case AUTHENTICATED:
                        showProgressBar(false);
                        Log.d(TAG, "onChanged: LOGIN SUCCESS: " + userAuthResource.data.getEmail());
                        onLoginSucess();
                        break;
                    case ERROR:
                        showProgressBar(false);
                        Log.d(TAG, "onChanged: LOGIN FAILED: " + userAuthResource.message);
                        break;
                    case NOT_AUTHENTICATED:
                        showProgressBar(false);
                        break;
                }
            }
        });
    }

    private void showProgressBar(boolean isVisible){
        if (isVisible){
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button: {
                 attemptLogin();
                 break;
            }
        }
    }

    private void attemptLogin() {
        if(TextUtils.isEmpty(userId.getText().toString())){
            return;
        }
        viewModel.authenticateWithId(Integer.parseInt(userId.getText().toString()));
    }

    private void onLoginSucess(){
        Intent intentToGoToMainScreen = new Intent(this, MainActivity.class);
        startActivity(intentToGoToMainScreen);
        finish();
    }
}
