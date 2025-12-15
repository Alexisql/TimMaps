package com.alexis.timmaps.data.remote.maps.service;

import com.alexis.timmaps.BuildConfig;
import com.alexis.timmaps.data.remote.maps.mapper.RouteMapper;
import com.alexis.timmaps.domain.maps.model.Location;
import com.alexis.timmaps.domain.maps.model.Route;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class MapsService {
    private final String API_KEY = BuildConfig.MAPS_API_KEY;
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json";
    private final RequestQueue queue;

    @Inject
    public MapsService(RequestQueue queue) {
        this.queue = queue;
    }

    public Single<Route> getRoute(Location origin, Location destination) {
        return Single.create(emitter -> {
            String url = String.format("%s?origin=%s,%s&destination=%s,%s&key=%s",
                    BASE_URL,
                    origin.getLatitude(),
                    origin.getLongitude(),
                    destination.getLatitude(),
                    destination.getLongitude(),
                    API_KEY);

            Response.Listener<JSONObject> successListener =
                    response -> {
                        try {
                            String status = response.getString("status");
                            if ("OK".equals(status)) {
                                Route route = RouteMapper.toDomain(response);
                                emitter.onSuccess(route);
                            } else if ("ZERO_RESULTS".equals(status)) {
                                emitter.onError(new Exception("No se encontraron rutas disponibles."));
                            } else {
                                emitter.onError(new Exception("Error de la API de Directions: " + status));
                            }
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    successListener,
                    emitter::onError
            );
            queue.add(jsonObjectRequest);
        });
    }
}
