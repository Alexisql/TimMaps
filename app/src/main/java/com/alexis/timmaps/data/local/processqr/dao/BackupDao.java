package com.alexis.timmaps.data.local.processqr.dao;

import static com.alexis.timmaps.data.local.PersistenceContract.Backup;
import static io.reactivex.rxjava3.core.BackpressureStrategy.LATEST;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.alexis.timmaps.data.local.AppDataBase;
import com.alexis.timmaps.data.local.processqr.entity.BackupEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class BackupDao {

    private final AppDataBase appDataBase;
    private final BehaviorSubject<Object> databaseChanges;

    @Inject
    public BackupDao(AppDataBase appDataBase, BehaviorSubject<Object> databaseChanges) {
        this.appDataBase = appDataBase;
        this.databaseChanges = databaseChanges;
    }

    public Flowable<List<BackupEntity>> getAll() {
        return databaseChanges.toFlowable(LATEST)
                .flatMap(ignored -> Flowable.fromCallable(() -> {
                    SQLiteDatabase db = appDataBase.getReadableDatabase();
                    List<BackupEntity> backup = new ArrayList<>();
                    Cursor cursor = db.rawQuery("SELECT * FROM " + Backup.TABLE_NAME +
                            " ORDER BY " + BaseColumns._ID + " DESC", null);
                    while (cursor.moveToNext()) {
                        String label = cursor.getString(cursor.getColumnIndexOrThrow(Backup.COLUMN_NAME_LABEL));
                        String lat = cursor.getString(cursor.getColumnIndexOrThrow(Backup.COLUMN_NAME_LAT));
                        String lon = cursor.getString(cursor.getColumnIndexOrThrow(Backup.COLUMN_NAME_LON));
                        String observation = cursor.getString(cursor.getColumnIndexOrThrow(Backup.COLUMN_NAME_OBSERVATION));

                        backup.add(new BackupEntity(label, lat, lon, observation));
                    }
                    return backup;
                }));
    }

    public Completable insert(BackupEntity backup) {
        return Completable.fromAction(() -> {
            SQLiteDatabase db = appDataBase.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Backup.COLUMN_NAME_LABEL, backup.getLabel());
            values.put(Backup.COLUMN_NAME_LAT, backup.getLat());
            values.put(Backup.COLUMN_NAME_LON, backup.getLon());
            values.put(Backup.COLUMN_NAME_OBSERVATION, backup.getObservation());
            db.insert(Backup.TABLE_NAME, null, values);
            databaseChanges.onNext(new Object());
        });
    }

}
