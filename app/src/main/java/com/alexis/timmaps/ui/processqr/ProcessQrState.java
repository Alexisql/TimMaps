package com.alexis.timmaps.ui.processqr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.model.Qr;

import java.util.List;

public class ProcessQrState {

    @NonNull
    public final Status status;

    @Nullable
    public final Qr qrData;

    @Nullable
    public final List<DataQr> qrList;

    @Nullable
    public final String successMessage;

    @Nullable
    public final String errorMessage;

    private ProcessQrState(@NonNull Status status, @Nullable Qr qrData,
                           @Nullable List<DataQr> qrList, @Nullable String successMessage,
                           @Nullable String errorMessage) {
        this.status = status;
        this.qrData = qrData;
        this.qrList = qrList;
        this.successMessage = successMessage;
        this.errorMessage = errorMessage;
    }

    public static ProcessQrState loading() {
        return new ProcessQrState(Status.LOADING, null, null, null, null);
    }

    public static ProcessQrState qrProcessed(@NonNull Qr qrData) {
        return new ProcessQrState(Status.QR_PROCESSED, qrData, null, null, null);
    }

    public static ProcessQrState qrListLoaded(@NonNull List<DataQr> qrList) {
        return new ProcessQrState(Status.QR_LIST_LOADED, null, qrList, null, null);
    }

    public static ProcessQrState operationSuccess(@NonNull String message) {
        return new ProcessQrState(Status.OPERATION_SUCCESS, null, null, message, null);
    }

    public static ProcessQrState error(@NonNull String message) {
        return new ProcessQrState(Status.ERROR, null, null, null, message);
    }

    public enum Status {
        LOADING,
        QR_PROCESSED,
        QR_LIST_LOADED,
        OPERATION_SUCCESS,
        ERROR
    }
}
