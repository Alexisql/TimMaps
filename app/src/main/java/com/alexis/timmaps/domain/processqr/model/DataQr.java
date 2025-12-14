package com.alexis.timmaps.domain.processqr.model;

public class DataQr {

    private String label;
    private String lat;
    private String lon;
    private String observations;

    public DataQr(String label, String lat, String lon, String observations) {
        this.label = label;
        this.lat = lat;
        this.lon = lon;
        this.observations = observations;
    }

    public String getLabel() {
        return label;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getObservations() {
        return observations;
    }
}
