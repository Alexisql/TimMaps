package com.alexis.timmaps.data.local.logout.repository;

import com.alexis.timmaps.data.local.logout.dao.LogoutDao;
import com.alexis.timmaps.domain.logout.repository.ILogoutRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class LogoutRepositoryLocalImpl implements ILogoutRepository {

    private final LogoutDao logoutDao;

    @Inject
    public LogoutRepositoryLocalImpl(LogoutDao logoutDao) {
        this.logoutDao = logoutDao;
    }

    @Override
    public Completable deleteBackup() {
        return logoutDao.deleteBackup();
    }
}
