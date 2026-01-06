package com.alexis.timmaps.di.module;

import com.alexis.timmaps.data.local.logout.repository.LogoutRepositoryLocalImpl;
import com.alexis.timmaps.data.local.processqr.repository.ProcessQrRepositoryImpl;
import com.alexis.timmaps.data.remote.login.repository.AuthRepositoryImpl;
import com.alexis.timmaps.data.remote.logout.repository.LogoutRepositoryRemoteImpl;
import com.alexis.timmaps.data.remote.maps.repository.LocationRepositoryImpl;
import com.alexis.timmaps.data.remote.processqr.repository.SyncBackupRepositoryImpl;
import com.alexis.timmaps.data.remote.processqr.repository.ValidateQrRepositoryImpl;
import com.alexis.timmaps.domain.login.repository.IAuthRepository;
import com.alexis.timmaps.domain.logout.repository.ILogoutRepository;
import com.alexis.timmaps.domain.maps.repository.ILocationRepository;
import com.alexis.timmaps.domain.processqr.repository.IProcessQrRepository;
import com.alexis.timmaps.domain.processqr.repository.ISyncRepository;
import com.alexis.timmaps.domain.processqr.repository.IValidateQrRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract IAuthRepository bindAuthRepository(AuthRepositoryImpl impl);

    @Binds
    @Singleton
    abstract IProcessQrRepository bindProcessQrRepository(ProcessQrRepositoryImpl impl);

    @Binds
    @Singleton
    abstract ISyncRepository bindSyncRepository(SyncBackupRepositoryImpl impl);

    @Binds
    @Singleton
    @Named(Qualifiers.LOGOUT_REPOSITORY_LOCAL)
    abstract ILogoutRepository bindLogoutRepositoryLocal(LogoutRepositoryLocalImpl impl);

    @Binds
    @Singleton
    @Named(Qualifiers.LOGOUT_REPOSITORY_REMOTE)
    abstract ILogoutRepository bindLogoutRepositoryRemote(LogoutRepositoryRemoteImpl impl);

    @Binds
    @Singleton
    abstract IValidateQrRepository bindValidateQrRepository(ValidateQrRepositoryImpl impl);

    @Binds
    @Singleton
    abstract ILocationRepository bindLocationRepository(LocationRepositoryImpl impl);
}
