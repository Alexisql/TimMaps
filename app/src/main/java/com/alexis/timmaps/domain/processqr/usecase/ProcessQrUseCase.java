package com.alexis.timmaps.domain.processqr.usecase;

import com.alexis.timmaps.domain.processqr.model.ProcessQr;
import com.alexis.timmaps.domain.processqr.repository.IProcessQrRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ProcessQrUseCase {

    private final IProcessQrRepository repository;

    @Inject
    public ProcessQrUseCase(IProcessQrRepository repository) {
        this.repository = repository;
    }

    public Single<ProcessQr> execute(String data) {
        return repository.processQr(data);
    }

}
