package com.alexis.timmaps.di;

import android.app.Application;

import com.alexis.timmaps.di.module.AppModule;
import com.alexis.timmaps.di.module.RepositoryModule;
import com.alexis.timmaps.di.module.viewmodel.ViewModelModule;
import com.alexis.timmaps.ui.login.LoginActivity;
import com.alexis.timmaps.ui.maps.MapsActivity;
import com.alexis.timmaps.ui.processqr.ProcessQrActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        RepositoryModule.class,
        ViewModelModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(LoginActivity activity);

    void inject(ProcessQrActivity activity);

    void inject(MapsActivity activity);
}
