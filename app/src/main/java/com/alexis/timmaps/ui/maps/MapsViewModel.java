package com.alexis.timmaps.ui.maps;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis.timmaps.R;
import com.alexis.timmaps.di.module.Qualifiers;
import com.alexis.timmaps.domain.maps.model.Location;
import com.alexis.timmaps.domain.maps.model.Route;
import com.alexis.timmaps.domain.maps.usecase.GetRouteMapsUseCase;
import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.usecase.GetDataQrUseCase;
import com.alexis.timmaps.ui.maps.state.MapsState;
import com.alexis.timmaps.ui.maps.state.MarkersState;
import com.alexis.timmaps.ui.maps.state.RouteState;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MapsViewModel extends ViewModel {

    private final GetRouteMapsUseCase mapsUseCase;
    private final GetDataQrUseCase getDataUseCase;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;
    private final Context context;

    private final MutableLiveData<MapsState> state = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MapsViewModel(Context context,
                         GetRouteMapsUseCase mapsUseCase,
                         GetDataQrUseCase getDataUseCase,
                         @Named(Qualifiers.IO_SCHEDULER) Scheduler ioScheduler,
                         @Named(Qualifiers.MAIN_SCHEDULER) Scheduler mainScheduler) {
        this.context = context;
        this.mapsUseCase = mapsUseCase;
        this.getDataUseCase = getDataUseCase;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    public LiveData<MapsState> getState() {
        return state;
    }

    public void initialize(Intent intent) {
        String lat = intent.getStringExtra(Constants.EXTRA_LAT);
        String lon = intent.getStringExtra(Constants.EXTRA_LON);

        if (lat == null || lon == null) {
            getLocations();
        } else {
            getRoute(new Location(Double.parseDouble(lat), Double.parseDouble(lon)));
        }
    }

    private void getRoute(Location destination) {
        state.setValue(new MapsState.Loading());
        disposables.add(
                mapsUseCase.execute(destination)
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                route -> {
                                    state.setValue(
                                            new MapsState.RouteLoaded(prepareRoute(route)));
                                },
                                throwable -> state.setValue(new MapsState.Error(getMessageError(throwable)))
                        )
        );
    }

    private void getLocations() {
        state.setValue(new MapsState.Loading());
        disposables.add(
                getDataUseCase.execute()
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                listLocations -> {
                                    state.setValue(
                                            new MapsState.MarkersLoaded(prepareMarkers(listLocations)));
                                },
                                throwable ->
                                        state.setValue(new MapsState.Error(getMessageError(throwable)))
                        )
        );
    }

    private RouteState prepareRoute(Route route) {
        List<LatLng> decodedPath = PolyUtil.decode(route.getEncodedPolyline());

        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(decodedPath)
                .width(12f)
                .color(ContextCompat.getColor(context, R.color.primary_color));

        LatLng startPoint = decodedPath.get(0);
        LatLng endPoint = decodedPath.get(decodedPath.size() - 1);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startPoint);
        builder.include(endPoint);
        LatLngBounds bounds = builder.build();

        MarkerOptions endMarker = new MarkerOptions()
                .position(endPoint)
                .title("Destino");

        return new RouteState(polylineOptions, endMarker, bounds);
    }

    private MarkersState prepareMarkers(List<DataQr> locationsList) {
        List<MarkerOptions> markerOptionsList = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (DataQr point : locationsList) {
            double lat = Double.parseDouble(point.getLat());
            double lon = Double.parseDouble(point.getLon());
            LatLng latLng = new LatLng(lat, lon);

            markerOptionsList.add(new MarkerOptions().position(latLng).title(point.getLabel()));
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();

        return new MarkersState(markerOptionsList, bounds);
    }

    private String getMessageError(Throwable throwable) {
        return throwable.getMessage() != null ? throwable.getMessage() : "Error desconocido";
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

}
