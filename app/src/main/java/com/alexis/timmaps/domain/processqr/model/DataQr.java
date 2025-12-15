package com.alexis.timmaps.domain.processqr.model;

public class DataQr {

    private String label;
    private String lat;
    private String lon;
    private String observation;

    public DataQr(String label, String lat, String lon, String observation) {
        this.label = label;
        this.lat = lat;
        this.lon = lon;
        this.observation = observation;
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

    public String getObservation() {
        return observation;
    }
}
