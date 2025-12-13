package com.alexis.timmaps.domain.usecase;

import com.alexis.timmaps.domain.model.User;
import com.alexis.timmaps.domain.repository.IAuthRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class LoginUseCase {

    private final IAuthRepository repository;

    @Inject
    public LoginUseCase(IAuthRepository repository) {
        this.repository = repository;
    }

    public Single<User> execute(String username, String password) {
        return repository.login(username, password);
    }
}
