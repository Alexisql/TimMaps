package com.alexis.timmaps.data.remote.processqr.model;

public class QrResponse {

    private String correct;
    private String dataQr;

    public QrResponse(String correct, String data) {
        this.correct = correct;
        this.dataQr = data;
    }

    public String getCorrect() {
        return correct;
    }

    public String getData() {
        return dataQr;
    }
}
