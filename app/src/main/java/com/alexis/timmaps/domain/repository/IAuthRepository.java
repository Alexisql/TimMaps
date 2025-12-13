package com.alexis.timmaps.domain.repository;

import com.alexis.timmaps.domain.model.User;

import io.reactivex.rxjava3.core.Single;

public interface IAuthRepository {
    Single<User> login(String username, String password);
}
