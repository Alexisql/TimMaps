package com.alexis.timmaps.domain.logout.repository;

import io.reactivex.rxjava3.core.Completable;

public interface ILogoutRepository {
    Completable deleteBackup();
}
