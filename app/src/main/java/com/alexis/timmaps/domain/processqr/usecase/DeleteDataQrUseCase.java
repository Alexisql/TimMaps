package com.alexis.timmaps.domain.processqr.usecase;

import com.alexis.timmaps.domain.processqr.repository.IProcessQrRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class DeleteDataQrUseCase {
    private final IProcessQrRepository repository;

    @Inject
    public DeleteDataQrUseCase(IProcessQrRepository repository) {
        this.repository = repository;
    }

    public Completable execute() {
        return repository.deleteAll();
    }
}
