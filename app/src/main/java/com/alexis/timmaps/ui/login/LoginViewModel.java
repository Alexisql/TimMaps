package com.alexis.timmaps.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis.timmaps.di.module.Qualifiers;
import com.alexis.timmaps.domain.login.usecase.LoginUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LoginViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private final MutableLiveData<LoginState> state = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase,
                          @Named(Qualifiers.IO_SCHEDULER) Scheduler ioScheduler,
                          @Named(Qualifiers.MAIN_SCHEDULER) Scheduler mainScheduler) {
        this.loginUseCase = loginUseCase;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    public LiveData<LoginState> getState() {
        return state;
    }

    public void login(String username, String password) {
        state.setValue(new LoginState.Loading());
        disposables.add(
                loginUseCase.execute(username, password)
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                user -> state.setValue(new LoginState.Success(user)),
                                throwable -> state.setValue(new LoginState.Error(throwable.getMessage()))
                        )
        );
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}