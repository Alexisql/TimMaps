package com.alexis.timmaps.domain.processqr.repository;

import com.alexis.timmaps.domain.processqr.model.DataQr;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface IProcessQrRepository {
    Flowable<List<DataQr>> getAll();

    Completable insert(DataQr dataQr);
}
