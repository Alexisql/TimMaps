package com.alexis.timmaps.domain.processqr.model;

import java.util.List;

public class Backup {

    private String serial;
    private String date;
    private List<DataQr> dataQr;

    public Backup(String serial, String date, List<DataQr> dataQr) {
        this.serial = serial;
        this.date = date;
        this.dataQr = dataQr;
    }

    public String getSerial() {
        return serial;
    }

    public String getDate() {
        return date;
    }

    public List<DataQr> getData() {
        return dataQr;
    }
}
