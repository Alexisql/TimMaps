package com.alexis.timmaps.ui.processqr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis.timmaps.di.module.Qualifiers;
import com.alexis.timmaps.domain.logout.usecase.LogoutUseCase;
import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.usecase.GetDataQrUseCase;
import com.alexis.timmaps.domain.processqr.usecase.InsertDataQrUseCase;
import com.alexis.timmaps.domain.processqr.usecase.ValidateQrUseCase;
import com.alexis.timmaps.domain.processqr.usecase.SyncBackupUseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ProcessQrViewModel extends ViewModel {

    private final ValidateQrUseCase readUseCase;
    private final InsertDataQrUseCase insertUseCase;
    private final LogoutUseCase logoutUseCase;
    private final GetDataQrUseCase getDataUseCase;
    private final SyncBackupUseCase syncBackupUseCase;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private final MutableLiveData<ProcessQrState> state = new MutableLiveData<>(ProcessQrState.loading());
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ProcessQrViewModel(ValidateQrUseCase readUseCase,
                              InsertDataQrUseCase insertUseCase,
                              LogoutUseCase logoutUseCase,
                              GetDataQrUseCase getDataUseCase,
                              SyncBackupUseCase syncBackupUseCase,
                              @Named(Qualifiers.IO_SCHEDULER) Scheduler ioScheduler,
                              @Named(Qualifiers.MAIN_SCHEDULER) Scheduler mainScheduler) {
        this.readUseCase = readUseCase;
        this.insertUseCase = insertUseCase;
        this.logoutUseCase = logoutUseCase;
        this.getDataUseCase = getDataUseCase;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        this.syncBackupUseCase = syncBackupUseCase;
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
                                qrList -> {
                                    state.setValue(ProcessQrState.qrListLoaded(qrList));
                                    syncBackup(qrList);
                                },
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

    public void logout() {
        state.setValue(ProcessQrState.loading());
        disposables.add(
                logoutUseCase.execute()
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                () -> state.setValue(ProcessQrState.operationSuccess("Todos los datos han sido eliminados.")),
                                throwable ->
                                        state.setValue(ProcessQrState.error(getMessageError(throwable)))
                        )
        );
    }

    private void syncBackup(List<DataQr> backup) {
        int backupSize = backup.size();
        if (backupSize > 0 && backupSize % 5 == 0) {
            disposables.add(
                    syncBackupUseCase.execute(backup)
                            .subscribeOn(ioScheduler)
                            .observeOn(mainScheduler)
                            .subscribe(
                                    () -> state.setValue(ProcessQrState.operationSuccess("Sincronizacion completada.")),
                                    throwable ->
                                            state.setValue(ProcessQrState.error(getMessageError(throwable)))
                            )
            );
        }
    }

    private String getMessageError(Throwable throwable) {
        return throwable.getMessage() != null ? throwable.getMessage() : "Error desconocido";
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
