package com.alexis.timmaps.di.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Module
public class RxModule {

    @Provides
    @Singleton
    @Named(Qualifier.IO_SCHEDULER)
    static Scheduler provideIoScheduler() {
        return Schedulers.io();
    }


    @Provides
    @Singleton
    @Named(Qualifier.MAIN_SCHEDULER)
    static Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
