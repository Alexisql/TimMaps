package com.alexis.timmaps.domain.processqr.model;

public class Qr {

    private String correct;
    private DataQr dataQr;

    public Qr(String correct, DataQr data) {
        this.correct = correct;
        this.dataQr = data;
    }

    public String getCorrect() {
        return correct;
    }

    public DataQr getData() {
        return dataQr;
    }
}
