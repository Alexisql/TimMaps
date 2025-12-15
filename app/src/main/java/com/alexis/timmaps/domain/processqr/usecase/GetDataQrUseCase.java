package com.alexis.timmaps.domain.processqr.usecase;

import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.repository.IProcessQrRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class GetDataQrUseCase {
    private final IProcessQrRepository repository;

    @Inject
    public GetDataQrUseCase(IProcessQrRepository repository) {
        this.repository = repository;
    }

    public Flowable<List<DataQr>> execute() {
        return repository.getAll();
    }
}
