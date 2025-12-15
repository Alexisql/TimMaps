package com.alexis.timmaps.data.local.processqr.repository;

import com.alexis.timmaps.data.local.processqr.dao.BackupDao;
import com.alexis.timmaps.data.local.processqr.entity.BackupEntity;
import com.alexis.timmaps.data.local.processqr.mapper.DataQrMapper;
import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.repository.IProcessQrRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class ProcessQrRepositoryImpl implements IProcessQrRepository {

    private final BackupDao backupDao;

    @Inject
    public ProcessQrRepositoryImpl(BackupDao backupDao) {
        this.backupDao = backupDao;
    }

    @Override
    public Flowable<List<DataQr>> getAll() {
        return backupDao.getAll()
                .map(backupEntities -> {
                    List<DataQr> dataQrs = new ArrayList<>();
                    for (BackupEntity entity : backupEntities) {
                        dataQrs.add(DataQrMapper.toDomain(entity));
                    }
                    return dataQrs;
                });
    }

    @Override
    public Completable insert(DataQr dataQr) {
        return backupDao.insert(DataQrMapper.toEntity(dataQr));
    }

    @Override
    public Completable deleteAll() {
        return backupDao.deleteAll();
    }
}
