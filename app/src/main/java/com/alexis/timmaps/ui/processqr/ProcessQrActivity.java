package com.alexis.timmaps.ui.processqr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
                    Toast.makeText(this, "El permiso de cámara es obligatorio para escanear el código QR.", Toast.LENGTH_LONG).show();
                    closeScanner();
                }
            });

    private final ActivityResultLauncher<String> requestLocationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    navigateToMap(location);
                } else {
                    Toast.makeText(this, "El permiso de ubicación es necesario para ver la ruta.", Toast.LENGTH_LONG).show();
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
        switch (state.status) {
            case LOADING:
                hideRecycler();
                break;

            case QR_PROCESSED:
                showRecycler();
                viewModel.insertDataQr(state.qrData.getData());
                break;

            case QR_LIST_LOADED:
                showRecycler();
                dataQrAdapter.submitList(state.qrList);
                break;

            case OPERATION_SUCCESS:
                showRecycler();
                clearEditText();
                Toast.makeText(this, state.successMessage, Toast.LENGTH_SHORT).show();
                break;

            case ERROR:
                showRecycler();
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void setupRecyclerView() {
        dataQrAdapter = new DataQrAdapter(dataQr -> {
            location = dataQr;
            checkAndRequestLocationPermission(location);
        });
        binding.rvItemList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvItemList.setAdapter(dataQrAdapter);
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

    private void showRecycler() {
        binding.progressBar.setVisibility(View.GONE);
        binding.rvItemList.setVisibility(View.VISIBLE);
    }

    private void hideRecycler() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvItemList.setVisibility(View.GONE);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void checkAndRequestLocationPermission(DataQr location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            navigateToMap(location);
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void navigateToMap(DataQr dataQr) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(Constants.EXTRA_LAT, dataQr.getLat());
        intent.putExtra(Constants.EXTRA_LON, dataQr.getLon());
        startActivity(intent);
    }
}