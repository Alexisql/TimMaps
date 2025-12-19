package com.alexis.timmaps.ui.maps.state;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MarkersState {
    public final List<MarkerOptions> markerList;
    public final LatLngBounds bounds;

    public MarkersState(List<MarkerOptions> markerList, LatLngBounds bounds) {
        this.markerList = markerList;
        this.bounds = bounds;
    }
}
