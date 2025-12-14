package com.alexis.timmaps.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexis.timmaps.R;
import com.alexis.timmaps.TimMapsApp;
import com.alexis.timmaps.databinding.ActivityLoginBinding;
import com.alexis.timmaps.ui.processqr.ProcessQrActivity;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TimMapsApp) getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        setupListeners();
        viewModel.getState().observe(this, this::observeViewModel);
    }

    private void setupListeners() {
        binding.loginButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.message_edittext_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.login(username, password);
        });
    }

    private void observeViewModel(LoginState state) {
        if (state instanceof LoginState.Loading) {
            binding.loginFormContainer.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else if (state instanceof LoginState.Success) {
            binding.progressBar.setVisibility(View.GONE);
            startActivity(new Intent(this, ProcessQrActivity.class));
            finish();
        } else if (state instanceof LoginState.Error) {
            binding.loginFormContainer.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(this, ((LoginState.Error) state).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}