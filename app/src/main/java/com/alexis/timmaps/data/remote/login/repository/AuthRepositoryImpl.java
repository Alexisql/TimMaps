package com.alexis.timmaps.data.remote.login.repository;

import com.alexis.timmaps.data.remote.login.mapper.UserMapper;
import com.alexis.timmaps.data.remote.login.model.UserDto;
import com.alexis.timmaps.domain.login.model.User;
import com.alexis.timmaps.domain.login.repository.IAuthRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Singleton
public class AuthRepositoryImpl implements IAuthRepository {

    private static final String COLLECTION_USERS_FIREBASE = "Usuarios";
    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    @Inject
    public AuthRepositoryImpl(FirebaseFirestore firestore, FirebaseAuth auth) {
        this.firestore = firestore;
        this.auth = auth;
    }

    @Override
    public Single<User> login(String username, String password) {
        return signInAnonymously()
                .andThen(queryUser(username, password));
    }


    private Completable signInAnonymously() {
        return Completable.create(emitter -> {
            auth.signInAnonymously()
                    .addOnSuccessListener(result -> {
                        emitter.onComplete();
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }


    private Single<User> queryUser(String username, String password) {
        return Single.create(emitter -> {
            firestore.collection(COLLECTION_USERS_FIREBASE)
                    .whereEqualTo("username", username)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        if (snapshot.isEmpty()) {
                            emitter.onError(new Exception("Usuario no existe"));
                            return;
                        }

                        UserDto dto = snapshot.getDocuments()
                                .get(0)
                                .toObject(UserDto.class);

                        if (dto == null || !dto.getPassword().equals(password)) {
                            emitter.onError(new Exception("Credenciales incorrectas"));
                            return;
                        }


                        emitter.onSuccess(UserMapper.toDomain(dto));
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}
