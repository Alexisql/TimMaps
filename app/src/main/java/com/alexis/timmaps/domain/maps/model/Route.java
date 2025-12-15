package com.alexis.timmaps.domain.maps.model;

public class Route {
    private String encodedPolyline;

    public Route(String encodedPolyline) {
        this.encodedPolyline = encodedPolyline;
    }

    public String getEncodedPolyline() {
        return encodedPolyline;
    }
}
