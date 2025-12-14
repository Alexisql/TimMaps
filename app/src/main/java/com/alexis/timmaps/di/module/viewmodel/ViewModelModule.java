package com.alexis.timmaps.di.module.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.alexis.timmaps.ui.login.LoginViewModel;
import com.alexis.timmaps.ui.processqr.ProcessQrViewModel;
import com.alexis.timmaps.ui.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProcessQrViewModel.class)
    abstract ViewModel bindProcessQrViewModel(ProcessQrViewModel processQrViewModel);

}
