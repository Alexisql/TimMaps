package com.alexis.timmaps.domain.processqr.usecase;

import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.repository.IProcessQrRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class InsertDataQrUseCase {
    private final IProcessQrRepository repository;

    @Inject
    public InsertDataQrUseCase(IProcessQrRepository repository) {
        this.repository = repository;
    }

    public Completable execute(DataQr dataQr) {
        return repository.insert(dataQr);
    }
}
