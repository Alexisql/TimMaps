package com.alexis.timmaps.domain.processqr.usecase;

import com.alexis.timmaps.domain.processqr.model.Qr;
import com.alexis.timmaps.domain.processqr.repository.IReadQrRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ReadQrUseCase {

    private final IReadQrRepository repository;

    @Inject
    public ReadQrUseCase(IReadQrRepository repository) {
        this.repository = repository;
    }

    public Single<Qr> execute(String data) {
        return repository.readQr(data);
    }

}
