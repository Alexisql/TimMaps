package com.alexis.timmaps.ui.maps;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.alexis.timmaps.R;
import com.alexis.timmaps.TimMapsApp;
import com.alexis.timmaps.databinding.ActivityMapBinding;
import com.alexis.timmaps.domain.maps.model.Location;
import com.alexis.timmaps.domain.maps.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;
import java.util.Objects;

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
        viewModel.getRoute(getDestination());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }

    private void observeViewModel(MapsState state) {
        if (state instanceof MapsState.Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else if (state instanceof MapsState.Success) {
            drawRoute(((MapsState.Success) state).route);
            binding.progressBar.setVisibility(View.GONE);
        } else if (state instanceof MapsState.Error) {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(this, ((MapsState.Error) state).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Location getDestination() {
        return new Location(
                Double.parseDouble(Objects.requireNonNull(
                        getIntent().getStringExtra(Constants.EXTRA_LAT), "0.0")),
                Double.parseDouble(Objects.requireNonNull(
                        getIntent().getStringExtra(Constants.EXTRA_LON), "0.0"))
        );
    }

    private void drawRoute(Route route) {
        List<LatLng> decodedPath = PolyUtil.decode(route.getEncodedPolyline());
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(decodedPath)
                .width(12f)
                .color(ContextCompat.getColor(this, R.color.primary_color));

        LatLng startPoint = decodedPath.get(0);
        LatLng endPoint = decodedPath.get(decodedPath.size() - 1);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startPoint);
        builder.include(endPoint);
        LatLngBounds bounds = builder.build();

        int padding = 150;
        googleMap.addPolyline(polylineOptions);
        googleMap.addMarker(new MarkerOptions()
                .position(endPoint)
                .title("Destino"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
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