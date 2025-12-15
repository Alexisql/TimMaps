package com.alexis.timmaps.domain.login.repository;

import io.reactivex.rxjava3.core.Completable;

public interface IAuthRepository {
    Completable login(String username, String password);
}
