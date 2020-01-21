package com.talentjoko.todoapplication;

import android.app.Application;

import com.talentjoko.todoapplication.di.ApplicationComponent;
import com.talentjoko.todoapplication.di.ApplicationModule;
import com.talentjoko.todoapplication.di.DaggerApplicationComponent;


public class TodoApplication extends Application {

    public ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
    }
}
