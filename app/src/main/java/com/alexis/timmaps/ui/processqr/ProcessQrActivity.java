package com.alexis.timmaps.ui.processqr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alexis.timmaps.R;
import com.alexis.timmaps.TimMapsApp;
import com.alexis.timmaps.databinding.ActivityMainBinding;
import com.alexis.timmaps.databinding.DialogConfirmLogoutBinding;
import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.model.Qr;
import com.alexis.timmaps.ui.login.LoginActivity;
import com.alexis.timmaps.ui.maps.Constants;
import com.alexis.timmaps.ui.maps.MapsActivity;
import com.alexis.timmaps.ui.processqr.adapter.DataQrAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class ProcessQrActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ProcessQrViewModel viewModel;
    private DataQrAdapter dataQrAdapter;
    private DataQr location;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TimMapsApp) getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, viewModelFactory).get(ProcessQrViewModel.class);

        setupRecyclerView();
        configScanner();
        setupListeners();
        viewModel.getState().observe(this, this::observeViewModel);
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.zxingBarcodeScanner.pause();
    }

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startScanner();
                } else {
                    Toast.makeText(this, "El permiso de cámara es obligatorio para escanear el código QR.", Toast.LENGTH_SHORT).show();
                    closeScanner();
                }
            });

    private final ActivityResultLauncher<String> requestLocationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (isGpsEnabled()) {
                        navigateToMap(location);
                    } else {
                        showGpsActivationDialog();
                    }
                } else {
                    Toast.makeText(this, "El permiso de ubicación es necesario para ver la ruta.", Toast.LENGTH_SHORT).show();
                }
            });

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                String qrData = result.getText();
                closeScanner();
                viewModel.processQr(qrData);
            }
        }
    };

    private void setupListeners() {
        binding.ivCameraIcon.setOnClickListener(v -> {
            checkAndRequestCameraPermission();
        });

        binding.ivCloseScanner.setOnClickListener(v -> {
            closeScanner();
        });

        binding.ivLogout.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });

        binding.saveButton.setOnClickListener(v -> {
            viewModel.processQr(String.valueOf(binding.etCodeQr.getText()));
        });

        if (Objects.requireNonNull(binding.etCodeQr.getText()).length() > 0) {
            binding.saveButton.setVisibility(View.VISIBLE);
        }

        binding.etCodeQr.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    binding.saveButton.setVisibility(View.VISIBLE);
                } else {
                    binding.saveButton.setVisibility(View.GONE);
                }
            }

        });
    }

    private void observeViewModel(ProcessQrState state) {
        binding.progressBar.setVisibility(state instanceof ProcessQrState.Loading ? View.VISIBLE : View.GONE);
        binding.rvItemList.setVisibility(state instanceof ProcessQrState.Loading ? View.GONE : View.VISIBLE);

        if (state instanceof ProcessQrState.QrProcessed) {
            Qr qrData = ((ProcessQrState.QrProcessed) state).qrData;
            viewModel.insertDataQr(qrData.getData());
        } else if (state instanceof ProcessQrState.QrListLoaded) {
            List<DataQr> qrList = ((ProcessQrState.QrListLoaded) state).qrList;
            dataQrAdapter.submitList(qrList);
        } else if (state instanceof ProcessQrState.OperationSuccess) {
            clearEditText();
            String message = ((ProcessQrState.OperationSuccess) state).message;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (state instanceof ProcessQrState.Error) {
            String message = ((ProcessQrState.Error) state).message;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    private void setupRecyclerView() {
        dataQrAdapter = new DataQrAdapter(this::showPositions, this::showRoute);
        binding.rvItemList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvItemList.setAdapter(dataQrAdapter);
    }

    private void showPositions() {
        navigateToMap(null);
    }

    private void showRoute(DataQr dataQr) {
        location = dataQr;
        checkAndRequestLocationPermission(location);
    }

    private void configScanner() {
        binding.zxingBarcodeScanner.setDecoderFactory(
                new DefaultDecoderFactory(Collections.singletonList(BarcodeFormat.QR_CODE)));
        binding.zxingBarcodeScanner.setStatusText(getString(R.string.title_scanner));
    }

    private void startScanner() {
        binding.processQrFormContainer.setVisibility(View.VISIBLE);
        binding.zxingBarcodeScanner.resume();
        binding.zxingBarcodeScanner.decodeContinuous(callback);
    }

    private void closeScanner() {
        binding.zxingBarcodeScanner.pause();
        binding.processQrFormContainer.setVisibility(View.GONE);
    }

    private void clearEditText() {
        binding.etCodeQr.setText("");
    }

    private void showLogoutConfirmationDialog() {
        DialogConfirmLogoutBinding bindingLogout = DialogConfirmLogoutBinding.inflate(getLayoutInflater());
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(bindingLogout.getRoot());

        final AlertDialog dialog = builder.create();

        bindingLogout.buttonCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        bindingLogout.buttonConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            logout();
        });

        dialog.show();
    }

    private void logout() {
        viewModel.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void checkAndRequestLocationPermission(DataQr location) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && isGpsEnabled()) {
            navigateToMap(location);
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showGpsActivationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("GPS Desactivado")
                .setMessage("Para ver la ruta en el mapa, necesitas activar el GPS. ¿Deseas activarlo ahora?")
                .setPositiveButton("Activar", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                    Toast.makeText(this, "El GPS es necesario para mostrar la ubicación",
                            Toast.LENGTH_LONG).show();
                })
                .show();
    }


    private void navigateToMap(DataQr dataQr) {
        Intent intent = new Intent(this, MapsActivity.class);
        if (dataQr != null) {
            intent.putExtra(Constants.EXTRA_LAT, dataQr.getLat());
            intent.putExtra(Constants.EXTRA_LON, dataQr.getLon());
        }
        startActivity(intent);
    }
}