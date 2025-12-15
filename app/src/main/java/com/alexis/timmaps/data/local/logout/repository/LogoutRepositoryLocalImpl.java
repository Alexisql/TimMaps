package com.alexis.timmaps.data.local.logout.repository;

import com.alexis.timmaps.data.local.processqr.dao.BackupDao;
import com.alexis.timmaps.domain.logout.repository.ILogoutRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class LogoutRepositoryLocalImpl implements ILogoutRepository {

    private final BackupDao backupDao;

    @Inject
    public LogoutRepositoryLocalImpl(BackupDao backupDao) {
        this.backupDao = backupDao;
    }

    @Override
    public Completable deleteBackup() {
        return backupDao.deleteBackup();
    }
}
