package com.alexis.timmaps.di.module;

import com.alexis.timmaps.data.remote.login.repository.AuthRepositoryImpl;
import com.alexis.timmaps.data.remote.processqr.repository.ProcessQrRepositoryImpl;
import com.alexis.timmaps.domain.login.repository.IAuthRepository;
import com.alexis.timmaps.domain.processqr.repository.IProcessQrRepository;

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
}
