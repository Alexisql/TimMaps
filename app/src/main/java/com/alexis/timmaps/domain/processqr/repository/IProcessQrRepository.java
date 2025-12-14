package com.alexis.timmaps.domain.processqr.repository;

import com.alexis.timmaps.domain.processqr.model.ProcessQr;

import io.reactivex.rxjava3.core.Single;

public interface IProcessQrRepository {
    Single<ProcessQr> processQr(String codeQr);
}
