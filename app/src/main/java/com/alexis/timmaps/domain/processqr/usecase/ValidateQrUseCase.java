package com.alexis.timmaps.domain.processqr.usecase;

import com.alexis.timmaps.domain.processqr.model.Qr;
import com.alexis.timmaps.domain.processqr.repository.IValidateQrRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ValidateQrUseCase {

    private final IValidateQrRepository repository;

    @Inject
    public ValidateQrUseCase(IValidateQrRepository repository) {
        this.repository = repository;
    }

    public Single<Qr> execute(String data) {
        return repository.validateCodeQr(data);
    }

}
