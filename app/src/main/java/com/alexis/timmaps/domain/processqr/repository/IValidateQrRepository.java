package com.alexis.timmaps.domain.processqr.repository;

import com.alexis.timmaps.domain.processqr.model.Qr;

import io.reactivex.rxjava3.core.Single;

public interface IValidateQrRepository {
    Single<Qr> validateCodeQr(String codeQr);

    String encodeToBase64(String data);
}
