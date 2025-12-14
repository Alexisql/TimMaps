package com.alexis.timmaps.ui.processqr;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.alexis.timmaps.R;
import com.alexis.timmaps.TimMapsApp;
import com.alexis.timmaps.databinding.ActivityMainBinding;
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

        configScanner();
        setupListeners();
        viewModel.getState().observe(this, this::observeViewModel);
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                binding.zxingBarcodeScanner.pause();
                String qrData = encodeToBase64(result.getText());
                binding.etCodeQr.setText(qrData);
                binding.zxingBarcodeScanner.setVisibility(View.GONE);
                viewModel.processQr(qrData);
            }
        }
    };

    private void setupListeners() {
        binding.ivCameraIcon.setOnClickListener(v -> {
            launchScanner();
        });
    }

    private void observeViewModel(ProcessQrState state) {
        if (state instanceof ProcessQrState.Success) {
            Log.e("Process QR", ((ProcessQrState.Success) state).getQrData().toString());
        } else if (state instanceof ProcessQrState.Error) {
            Toast.makeText(this, ((ProcessQrState.Error) state).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void configScanner() {
        binding.zxingBarcodeScanner.setDecoderFactory(
                new DefaultDecoderFactory(Collections.singletonList(BarcodeFormat.QR_CODE)));
        binding.zxingBarcodeScanner.setStatusText(getString(R.string.title_scanner));
    }

    private void launchScanner() {
        binding.zxingBarcodeScanner.setVisibility(View.VISIBLE);
        binding.zxingBarcodeScanner.resume();
        binding.zxingBarcodeScanner.decodeContinuous(callback);
    }

    private String encodeToBase64(String data) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(dataBytes, Base64.NO_WRAP);
    }
}