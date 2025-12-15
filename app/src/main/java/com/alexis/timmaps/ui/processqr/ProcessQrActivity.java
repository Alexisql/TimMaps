package com.alexis.timmaps.ui.processqr;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alexis.timmaps.R;
import com.alexis.timmaps.TimMapsApp;
import com.alexis.timmaps.databinding.ActivityMainBinding;
import com.alexis.timmaps.databinding.DialogConfirmLogoutBinding;
import com.alexis.timmaps.ui.login.LoginActivity;
import com.alexis.timmaps.ui.processqr.adapter.DataQrAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.inject.Inject;

public class ProcessQrActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ProcessQrViewModel viewModel;
    private DataQrAdapter dataQrAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TimMapsApp) getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

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

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startScanner();
                } else {
                    Toast.makeText(this, "Permiso de cámara denegado.", Toast.LENGTH_LONG).show();
                    closeScanner();
                }
            });

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                String qrData = encodeToBase64(result.getText());
                binding.etCodeQr.setText(qrData);
                closeScanner();
                viewModel.processQr(qrData);
            }
        }
    };

    private void setupListeners() {
        binding.ivCameraIcon.setOnClickListener(v -> {
            launchScanner();
        });

        binding.ivCloseScanner.setOnClickListener(v -> {
            closeScanner();
        });

        binding.ivLogout.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
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
                Toast.makeText(this, state.successMessage, Toast.LENGTH_SHORT).show();
                break;

            case ERROR:
                showRecycler();
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void setupRecyclerView() {
        dataQrAdapter = new DataQrAdapter();
        binding.rvItemList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvItemList.setAdapter(dataQrAdapter);
    }

    private void configScanner() {
        binding.zxingBarcodeScanner.setDecoderFactory(
                new DefaultDecoderFactory(Collections.singletonList(BarcodeFormat.QR_CODE)));
        binding.zxingBarcodeScanner.setStatusText(getString(R.string.title_scanner));
    }

    private void launchScanner() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        }
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

    private String encodeToBase64(String data) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(dataBytes, Base64.NO_WRAP);
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
}