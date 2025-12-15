package com.alexis.timmaps.ui.processqr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis.timmaps.di.module.Qualifier;
import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.usecase.DeleteDataQrUseCase;
import com.alexis.timmaps.domain.processqr.usecase.GetDataQrUseCase;
import com.alexis.timmaps.domain.processqr.usecase.InsertDataQrUseCase;
import com.alexis.timmaps.domain.processqr.usecase.ReadQrUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ProcessQrViewModel extends ViewModel {

    private final ReadQrUseCase readUseCase;
    private final InsertDataQrUseCase insertUseCase;
    private final DeleteDataQrUseCase deleteUseCase;
    private final GetDataQrUseCase getDataUseCase;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private final MutableLiveData<ProcessQrState> state = new MutableLiveData<>(ProcessQrState.loading());
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ProcessQrViewModel(ReadQrUseCase readUseCase,
                              InsertDataQrUseCase insertUseCase,
                              DeleteDataQrUseCase deleteUseCase,
                              GetDataQrUseCase getDataUseCase,
                              @Named(Qualifier.IO_SCHEDULER) Scheduler ioScheduler,
                              @Named(Qualifier.MAIN_SCHEDULER) Scheduler mainScheduler) {
        this.readUseCase = readUseCase;
        this.insertUseCase = insertUseCase;
        this.deleteUseCase = deleteUseCase;
        this.getDataUseCase = getDataUseCase;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        getAllQr();
    }

    public LiveData<ProcessQrState> getState() {
        return state;
    }

    public void processQr(String codeQr) {
        state.setValue(ProcessQrState.loading());
        disposables.add(
                readUseCase.execute(codeQr)
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                qrData ->
                                        state.setValue(ProcessQrState.qrProcessed(qrData)),
                                throwable ->
                                        state.setValue(ProcessQrState.error(getMessageError(throwable)))
                        )
        );
    }

    public void getAllQr() {
        state.setValue(ProcessQrState.loading());
        disposables.add(
                getDataUseCase.execute()
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                qrList ->
                                        state.setValue(ProcessQrState.qrListLoaded(qrList)),
                                throwable ->
                                        state.setValue(ProcessQrState.error(getMessageError(throwable)))
                        )
        );
    }

    public void insertDataQr(DataQr dataQr) {
        state.setValue(ProcessQrState.loading());
        disposables.add(
                insertUseCase.execute(dataQr)
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                () -> state.setValue(ProcessQrState.operationSuccess("Dato insertado correctamente.")),
                                throwable ->
                                        state.setValue(ProcessQrState.error(getMessageError(throwable)))
                        )
        );
    }

    public void deleteAllQrs() {
        state.setValue(ProcessQrState.loading());
        disposables.add(
                deleteUseCase.execute()
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                () -> state.setValue(ProcessQrState.operationSuccess("Todos los datos han sido eliminados.")),
                                throwable ->
                                        state.setValue(ProcessQrState.error(getMessageError(throwable)))
                        )
        );
    }

    private String getMessageError(Throwable throwable) {
        return throwable.getMessage() != null ? throwable.getMessage() : "Error desconocido";
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
