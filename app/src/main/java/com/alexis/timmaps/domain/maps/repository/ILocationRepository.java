package com.alexis.timmaps.domain.maps.repository;

import com.alexis.timmaps.domain.maps.model.Location;
import com.alexis.timmaps.domain.maps.model.Route;

import io.reactivex.rxjava3.core.Single;

public interface ILocationRepository {
    Single<Route> getRoute(Location origin, Location destination);

    Single<Location> getCurrentLocation();
}
