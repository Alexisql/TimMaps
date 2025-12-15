package com.alexis.timmaps.domain.processqr.usecase;

import com.alexis.timmaps.data.remote.processqr.service.ValidateQrService;
import com.alexis.timmaps.domain.processqr.model.Qr;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ValidateQrUseCase {

    private final ValidateQrService service;

    @Inject
    public ValidateQrUseCase(ValidateQrService service) {
        this.service = service;
    }

    public Single<Qr> execute(String data) {
        return service.validateCodeQr(data);
    }

}
