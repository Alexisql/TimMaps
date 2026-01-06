package com.alexis.timmaps.domain.maps.usecase;

import com.alexis.timmaps.domain.maps.model.Location;
import com.alexis.timmaps.domain.maps.model.Route;
import com.alexis.timmaps.domain.maps.repository.ILocationRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetRouteMapsUseCase {

    private final ILocationRepository repository;

    @Inject
    public GetRouteMapsUseCase(ILocationRepository repository) {
        this.repository = repository;
    }

    public Single<Route> execute(Location destination) {
        return repository.getCurrentLocation()
                .flatMap(location -> repository.getRoute(location, destination));
    }
}
