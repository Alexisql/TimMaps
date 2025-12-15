package com.alexis.timmaps.data.remote.processqr.mapper;

import com.alexis.timmaps.data.remote.processqr.model.QrResponse;
import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.model.Qr;

public class QrMapper {

    public static Qr toDomain(QrResponse response) {
        return new Qr(response.getCorrect(), dataToDomain(response.getData()));
    }

    private static DataQr dataToDomain(String qrData) {
        String[] data = qrData.split(":");
        String label = data[1].split("-")[0];
        String lat = data[2].split("-l")[0];
        String lon = data[3].split("-o")[0];
        String observation = data[4];
        return new DataQr(label, lat, lon, observation);
    }

}


