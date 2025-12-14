package com.alexis.timmaps.ui.processqr;

import com.alexis.timmaps.domain.processqr.model.ProcessQr;

public class ProcessQrState {

    public static final class Success extends ProcessQrState {
        private final ProcessQr qrData;

        public Success(ProcessQr qrData) {
            this.qrData = qrData;
        }

        public ProcessQr getQrData() {
            return qrData;
        }
    }

    public static final class Error extends ProcessQrState {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
