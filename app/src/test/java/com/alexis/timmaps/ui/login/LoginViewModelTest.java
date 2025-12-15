package com.alexis.timmaps.ui.login;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.alexis.timmaps.domain.login.usecase.LoginUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private LoginUseCase mockLoginUseCase;

    @Mock
    private Observer<LoginState> mockObserver;

    @Captor
    private ArgumentCaptor<LoginState> stateCaptor;

    private LoginViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new LoginViewModel(mockLoginUseCase, Schedulers.trampoline(), Schedulers.trampoline());
        viewModel.getState().observeForever(mockObserver);
    }

    @Test
    public void login_success_updatesStateToLoadingThenSuccess() {
        String username = "252525";
        String password = "1234";

        when(mockLoginUseCase.execute(username, password)).thenReturn(Completable.complete());
        viewModel.login(username, password);

        verify(mockObserver, times(2)).onChanged(stateCaptor.capture());

        List<LoginState> capturedStates = stateCaptor.getAllValues();

        assertTrue("Loading", capturedStates.get(0) instanceof LoginState.Loading);

        LoginState successState = capturedStates.get(1);
        assertTrue("Success", successState instanceof LoginState.Success);
    }

    @Test
    public void login_error_updatesStateToLoadingThenError() {
        String username = "12345";
        String password = "0000";
        String errorMessage = "Usuario no existe";
        Throwable error = new Throwable(errorMessage);

        when(mockLoginUseCase.execute(username, password)).thenReturn(Completable.error(error));

        viewModel.login(username, password);

        verify(mockObserver, times(2)).onChanged(stateCaptor.capture());

        List<LoginState> capturedStates = stateCaptor.getAllValues();

        assertTrue("Loading", capturedStates.get(0) instanceof LoginState.Loading);

        LoginState errorState = capturedStates.get(1);
        assertTrue("Error", errorState instanceof LoginState.Error);
        assertEquals("Mensaje de error", errorMessage, ((LoginState.Error) errorState).getMessage());
    }

}