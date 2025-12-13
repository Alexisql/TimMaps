package com.alexis.timmaps.di.module;

import com.alexis.timmaps.data.datasource.repository.AuthRepositoryImpl;
import com.alexis.timmaps.domain.repository.IAuthRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract IAuthRepository bindAuthRepository(AuthRepositoryImpl impl);
}
