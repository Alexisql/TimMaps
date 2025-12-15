package com.alexis.timmaps.di.module;

import com.alexis.timmaps.data.local.processqr.repository.ProcessQrRepositoryImpl;
import com.alexis.timmaps.data.remote.login.repository.AuthRepositoryImpl;
import com.alexis.timmaps.data.remote.processqr.repository.ReadQrRepositoryImpl;
import com.alexis.timmaps.domain.login.repository.IAuthRepository;
import com.alexis.timmaps.domain.processqr.repository.IProcessQrRepository;
import com.alexis.timmaps.domain.processqr.repository.IReadQrRepository;

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
    abstract IReadQrRepository bindReadQrRepository(ReadQrRepositoryImpl impl);

    @Binds
    @Singleton
    abstract IProcessQrRepository bindProcessQrRepository(ProcessQrRepositoryImpl impl);
}
