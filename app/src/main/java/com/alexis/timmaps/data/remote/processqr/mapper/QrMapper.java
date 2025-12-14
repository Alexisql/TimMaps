package com.alexis.timmaps.data.remote.processqr.mapper;

import com.alexis.timmaps.data.remote.processqr.model.QrResponse;
import com.alexis.timmaps.domain.processqr.model.ProcessQr;

public class QrMapper {

    public static ProcessQr toDomain(QrResponse response) {
        return new ProcessQr(response.getCorrect(), response.getData());
    }
}


