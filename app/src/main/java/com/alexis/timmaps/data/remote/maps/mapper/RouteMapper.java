package com.alexis.timmaps.data.remote.maps.mapper;

import com.alexis.timmaps.domain.maps.model.Route;

import org.json.JSONArray;
import org.json.JSONObject;

public class RouteMapper {
    public static Route toDomain(JSONObject jsonResponse) throws Exception {
        JSONArray routes = jsonResponse.getJSONArray("routes");
        if (routes.length() > 0) {
            JSONObject routeObject = routes.getJSONObject(0);
            JSONObject overviewPolyline = routeObject.getJSONObject("overview_polyline");
            String points = overviewPolyline.getString("points");
            return new Route(points);
        }
        throw new Exception("No se encontraron rutas en la respuesta.");
    }
}
