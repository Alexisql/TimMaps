package com.alexis.timmaps.data.remote.logout.repository;

import com.alexis.timmaps.domain.logout.repository.ILogoutRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class LogoutRepositoryRemoteImpl implements ILogoutRepository {

    private static final String COLLECTION_BACKUP_FIREBASE = "backup";
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    @Inject
    public LogoutRepositoryRemoteImpl(FirebaseAuth auth, FirebaseFirestore firestore) {
        this.auth = auth;
        this.firestore = firestore;
    }

    @Override
    public Completable deleteBackup() {
        return Completable.create(emitter -> {
            firestore.collection(COLLECTION_BACKUP_FIREBASE)
                    .document(auth.getCurrentUser().getUid())
                    .delete()
                    .addOnSuccessListener(aVoid -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }
}
