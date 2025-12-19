package com.alexis.timmaps.ui.maps.state;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteState {
    public final PolylineOptions polylineOptions;
    public final MarkerOptions endMarker;
    public final LatLngBounds bounds;

    public RouteState(PolylineOptions polylineOptions, MarkerOptions endMarker, LatLngBounds bounds) {
        this.polylineOptions = polylineOptions;
        this.endMarker = endMarker;
        this.bounds = bounds;
    }
}
