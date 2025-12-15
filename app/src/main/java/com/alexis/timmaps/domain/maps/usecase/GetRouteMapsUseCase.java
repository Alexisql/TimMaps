package com.alexis.timmaps.domain.maps.usecase;

import com.alexis.timmaps.data.remote.maps.service.MapsService;
import com.alexis.timmaps.domain.maps.model.Location;
import com.alexis.timmaps.domain.maps.model.Route;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetRouteMapsUseCase {

    private final MapsService service;

    @Inject
    public GetRouteMapsUseCase(MapsService service) {
        this.service = service;
    }

    public Single<Route> execute(Location origin, Location destination) {
        return service.getRoute(origin, destination);
    }
}
