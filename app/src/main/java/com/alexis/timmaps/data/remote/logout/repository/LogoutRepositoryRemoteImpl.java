package com.alexis.timmaps.data.remote.logout.repository;

import com.alexis.timmaps.domain.logout.repository.ILogoutRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class LogoutRepositoryRemoteImpl implements ILogoutRepository {

    private static final String COLLECTION_BACKUP_FIREBASE = "backup";

    @Inject
    public LogoutRepositoryRemoteImpl() {
    }

    @Override
    public Completable deleteBackup() {
        return Completable.create(emitter -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore.getInstance().collection(COLLECTION_BACKUP_FIREBASE)
                    .document(currentUser.getUid())
                    .delete()
                    .addOnSuccessListener(aVoid -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }
}
