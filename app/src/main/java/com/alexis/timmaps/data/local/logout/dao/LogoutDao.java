package com.alexis.timmaps.data.local.logout.dao;

import static com.alexis.timmaps.data.local.PersistenceContract.Backup;

import android.database.sqlite.SQLiteDatabase;

import com.alexis.timmaps.data.local.AppDataBase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class LogoutDao {

    private final AppDataBase appDataBase;
    private final BehaviorSubject<Object> databaseChanges;

    @Inject
    public LogoutDao(AppDataBase appDataBase, BehaviorSubject<Object> databaseChanges) {
        this.appDataBase = appDataBase;
        this.databaseChanges = databaseChanges;
    }

    public Completable deleteBackup() {
        return Completable.fromAction(() -> {
            SQLiteDatabase db = appDataBase.getWritableDatabase();
            db.delete(Backup.TABLE_NAME, null, null);
            databaseChanges.onNext(new Object());
        });
    }
}
