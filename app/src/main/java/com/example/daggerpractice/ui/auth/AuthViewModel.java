package com.example.daggerpractice.ui.auth;

import android.util.Log;

import com.example.daggerpractice.SessionManager;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.network.Auth.AuthApi;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AuthViewModel extends ViewModel {

    private final AuthApi authApi;

    private final SessionManager sessionManager;

    
    @Inject
    public AuthViewModel(AuthApi authApi, SessionManager sessionManager) {
        this.authApi=authApi;
        this.sessionManager=sessionManager;
        Log.d(TAG, "AuthViewModel: View model is working");

    }

    public void authenticateWithId(int userId){

        Log.d(TAG, "authenticateWithId: attemting login");

        sessionManager.authenticateWithId(queryUserId(userId));
    }

    private LiveData<AuthResource<User>> queryUserId(int userId){
        final LiveData<AuthResource<User>> source = LiveDataReactiveStreams.fromPublisher(
                authApi.getUser(userId)
                        .onErrorReturn(new Function<Throwable, User>() {
                            @Override
                            public User apply(Throwable throwable) throws Exception {
                                User errorUser = new User();
                                errorUser.setId(-1);
                                return errorUser;
                            }
                        }).map(new Function<User, AuthResource<User>>() {
                    @Override
                    public AuthResource<User> apply(User user) throws Exception {
                        if(user.getId()==-1){
                            return AuthResource.error("Could not authenticate", (User) null);
                        }
                        return AuthResource.authenticated(user);
                    }
                })
                        .subscribeOn(Schedulers.io())
        );

        return source;
    }

    public LiveData<AuthResource<User>> observeAuthState(){
        return sessionManager.getAuthUser();
    }
}
