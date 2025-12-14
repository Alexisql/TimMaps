package com.alexis.timmaps.domain.processqr.model;

public class ProcessQr {

    private String correct;
    private String dataQr;

    public ProcessQr(String correct, String data) {
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
