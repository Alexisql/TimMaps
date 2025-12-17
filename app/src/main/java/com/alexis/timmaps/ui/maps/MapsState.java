package com.alexis.timmaps.ui.maps;

import com.alexis.timmaps.domain.maps.model.Route;

public class MapsState {
    public static final class Loading extends MapsState {
    }

    public static final class Success extends MapsState {
        public final Route route;

        public Success(Route route) {
            this.route = route;
        }
    }

    public static final class Error extends MapsState {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
