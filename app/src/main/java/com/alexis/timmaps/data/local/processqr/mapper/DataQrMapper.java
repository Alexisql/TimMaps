package com.alexis.timmaps.data.local.processqr.mapper;

import com.alexis.timmaps.data.local.processqr.entity.BackupEntity;
import com.alexis.timmaps.domain.processqr.model.DataQr;

public class DataQrMapper {
    public static DataQr toDomain(BackupEntity entity) {
        return new DataQr(entity.getLabel(), entity.getLat(), entity.getLon(), entity.getObservation());
    }

    public static BackupEntity toEntity(DataQr dataQr) {
        return new BackupEntity(dataQr.getLabel(), dataQr.getLat(), dataQr.getLon(), dataQr.getObservation());
    }
}
