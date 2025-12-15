package com.alexis.timmaps.domain.processqr.repository;

import com.alexis.timmaps.domain.processqr.model.Backup;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public interface ISyncRepository {
    Completable insert(String userUid, List<Backup> backup);
}

