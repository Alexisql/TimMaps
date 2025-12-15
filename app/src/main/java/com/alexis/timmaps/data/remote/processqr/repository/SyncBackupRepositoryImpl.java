package com.alexis.timmaps.data.remote.processqr.repository;

import com.alexis.timmaps.domain.processqr.model.Backup;
import com.alexis.timmaps.domain.processqr.repository.ISyncRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class SyncBackupRepositoryImpl implements ISyncRepository {

    private static final String COLLECTION_BACKUP_FIREBASE = "backup";
    private final FirebaseFirestore firestore;

    @Inject
    public SyncBackupRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Completable insert(String userUid, List<Backup> backup) {
        Map<String, Object> syncDocument = new HashMap<>();
        syncDocument.put("data", backup);
        return Completable.create(emitter -> {
            firestore.collection(COLLECTION_BACKUP_FIREBASE)
                    .document(userUid)
                    .set(syncDocument, SetOptions.merge())
                    .addOnSuccessListener(documentReference -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }
}
