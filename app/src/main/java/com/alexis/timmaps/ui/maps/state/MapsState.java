package com.alexis.timmaps.ui.maps.state;

import androidx.annotation.NonNull;

public abstract class MapsState {

    private MapsState() {
    }

    public static class Loading extends MapsState {
    }

    public static class RouteLoaded extends MapsState {
        @NonNull
        public final RouteState routeState;

        public RouteLoaded(@NonNull RouteState routeState) {
            this.routeState = routeState;
        }
    }

    public static class MarkersLoaded extends MapsState {
        @NonNull
        public final MarkersState markersState;

        public MarkersLoaded(@NonNull MarkersState markersState) {
            this.markersState = markersState;
        }
    }

    public static class Error extends MapsState {
        @NonNull
        public final String message;

        public Error(@NonNull String message) {
            this.message = message;
        }
    }
}
