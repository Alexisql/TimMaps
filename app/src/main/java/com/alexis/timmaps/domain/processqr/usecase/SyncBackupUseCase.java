package com.alexis.timmaps.domain.processqr.usecase;

import com.alexis.timmaps.domain.processqr.model.Backup;
import com.alexis.timmaps.domain.processqr.model.DataQr;
import com.alexis.timmaps.domain.processqr.repository.ISyncRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class SyncBackupUseCase {

    private final ISyncRepository repository;

    @Inject
    public SyncBackupUseCase(ISyncRepository repository) {
        this.repository = repository;
    }

    public Completable execute(List<DataQr> listBackup) {
        return getCurrentUserId()
                .flatMapCompletable(userId -> {
                    List<Backup> backup = new ArrayList<>();
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    backup.add(new Backup(userId, currentDate, listBackup));
                    return repository.insert(userId, backup);
                });
    }

    private Single<String> getCurrentUserId() {
        return Single.create(emitter -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                emitter.onSuccess(currentUser.getUid());
            } else {
                emitter.onError(new IllegalStateException("No se encontró un usuario autenticado, para la sincronizacion."));
            }
        });
    }
}
