package com.alexis.timmaps.ui.processqr;

import androidx.annotation.NonNull;

import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.model.Qr;

import java.util.List;

public class ProcessQrState {

    public static final class Loading extends ProcessQrState {
    }

    public static final class QrProcessed extends ProcessQrState {
        @NonNull
        public final Qr qrData;

        public QrProcessed(@NonNull Qr qrData) {
            this.qrData = qrData;
        }
    }

    public static final class QrListLoaded extends ProcessQrState {
        @NonNull
        public final List<DataQr> qrList;

        public QrListLoaded(@NonNull List<DataQr> qrList) {
            this.qrList = qrList;
        }
    }

    public static final class OperationSuccess extends ProcessQrState {
        @NonNull
        public final String message;

        public OperationSuccess(@NonNull String message) {
            this.message = message;
        }
    }

    public static final class Error extends ProcessQrState {
        @NonNull
        public final String message;

        public Error(@NonNull String message) {
            this.message = message;
        }
    }
}
