package com.example.daggerpractice.di;

import android.app.Application;

import com.example.daggerpractice.BaseApplication;
import com.example.daggerpractice.SessionManager;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                AppModule.class,
                ActivityBuilderModule.class,
                ViewModelFactoryModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    SessionManager SessionManager();

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
