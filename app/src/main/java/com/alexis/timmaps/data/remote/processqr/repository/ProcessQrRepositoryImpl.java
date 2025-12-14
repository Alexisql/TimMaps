package com.alexis.timmaps.data.remote.processqr.repository;

import com.alexis.timmaps.data.remote.processqr.mapper.QrMapper;
import com.alexis.timmaps.data.remote.processqr.model.QrResponse;
import com.alexis.timmaps.data.remote.processqr.service.ProcessQrService;
import com.alexis.timmaps.domain.processqr.model.ProcessQr;
import com.alexis.timmaps.domain.processqr.repository.IProcessQrRepository;

import org.json.JSONException;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ProcessQrRepositoryImpl implements IProcessQrRepository {

    private final ProcessQrService service;

    @Inject
    public ProcessQrRepositoryImpl(ProcessQrService service) {
        this.service = service;
    }

    @Override
    public Single<ProcessQr> processQr(String codeQr) {
        return Single.create(emitter -> {
            service.validateCodeQr(codeQr,
                    response -> {
                        try {
                            String correcto = response.getString("Correcto");
                            String data = response.getString("data");
                            QrResponse qrResponse = new QrResponse(correcto, data);
                            emitter.onSuccess(QrMapper.toDomain(qrResponse));
                        } catch (JSONException exception) {
                            emitter.onError(new IllegalArgumentException("Estructura Incorrecta", exception));
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    },
                    emitter::onError
            );
        });
    }
}
