package com.alexis.timmaps.ui.processqr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis.timmaps.di.module.Qualifier;
import com.alexis.timmaps.domain.processqr.usecase.ProcessQrUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ProcessQrViewModel extends ViewModel {

    private final ProcessQrUseCase useCase;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private final MutableLiveData<ProcessQrState> state = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ProcessQrViewModel(ProcessQrUseCase useCase,
                              @Named(Qualifier.IO_SCHEDULER) Scheduler ioScheduler,
                              @Named(Qualifier.MAIN_SCHEDULER) Scheduler mainScheduler) {
        this.useCase = useCase;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    public LiveData<ProcessQrState> getState() {
        return state;
    }

    public void processQr(String codeQr) {
        disposables.add(
                useCase.execute(codeQr)
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                qrData ->
                                        state.setValue(new ProcessQrState.Success(qrData)),
                                throwable ->
                                        state.setValue(new ProcessQrState.Error(throwable.getMessage()))
                        )
        );
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
