package com.alexis.timmaps;

import android.app.Application;

import com.alexis.timmaps.di.AppComponent;
import com.alexis.timmaps.di.DaggerAppComponent;

public class TimMapsApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
