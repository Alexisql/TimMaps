package com.alexis.timmaps.di.module;

import android.app.Application;
import android.content.Context;

import com.alexis.timmaps.data.local.processqr.dao.BackupDao;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    FirebaseFirestore provideFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    FirebaseAuth provideAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    @Named(Qualifier.IO_SCHEDULER)
    Scheduler provideIoScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    @Named(Qualifier.MAIN_SCHEDULER)
    Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    RequestQueue provideRequestQueue(Context context) {
        return Volley.newRequestQueue(context);
    }

}
