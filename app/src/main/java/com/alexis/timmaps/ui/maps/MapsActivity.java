package com.alexis.timmaps.ui.maps;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexis.timmaps.TimMapsApp;
import com.alexis.timmaps.databinding.ActivityMapBinding;
import com.alexis.timmaps.ui.maps.state.MapsState;
import com.alexis.timmaps.ui.maps.state.MarkersState;
import com.alexis.timmaps.ui.maps.state.RouteState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityMapBinding binding;
    private GoogleMap googleMap;
    private MapsViewModel viewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TimMapsApp) getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(this);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(MapsViewModel.class);
        viewModel.getState().observe(this, this::observeViewModel);
        viewModel.initialize(getIntent());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }

    private void observeViewModel(MapsState state) {
        binding.progressBar.setVisibility(state instanceof MapsState.Loading ? View.VISIBLE : View.GONE);

        if (state instanceof MapsState.RouteLoaded) {
            drawRoute(((MapsState.RouteLoaded) state).routeState);
        } else if (state instanceof MapsState.MarkersLoaded) {
            showMarkers(((MapsState.MarkersLoaded) state).markersState);
        } else if (state instanceof MapsState.Error) {
            Toast.makeText(this, ((MapsState.Error) state).message, Toast.LENGTH_SHORT).show();
        }
    }

    private void drawRoute(RouteState routeState) {
        if (googleMap == null) return;
        googleMap.clear();
        googleMap.addPolyline(routeState.polylineOptions);
        googleMap.addMarker(routeState.endMarker);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(routeState.bounds, 150));

    }

    private void showMarkers(MarkersState markersState) {
        if (googleMap == null) return;
        googleMap.clear();
        for (MarkerOptions marker : markersState.markerList) {
            googleMap.addMarker(marker);
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(markersState.bounds, 150));
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }
}