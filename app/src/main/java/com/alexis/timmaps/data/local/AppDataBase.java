package com.alexis.timmaps.data.local;

import static com.alexis.timmaps.data.local.PersistenceContract.Backup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppDataBase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TimMaps.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Backup.TABLE_NAME + " (" +
                    Backup._ID + " INTEGER PRIMARY KEY," +
                    Backup.COLUMN_NAME_LABEL + " TEXT," +
                    Backup.COLUMN_NAME_LAT + " TEXT," +
                    Backup.COLUMN_NAME_LON + " TEXT," +
                    Backup.COLUMN_NAME_OBSERVATION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Backup.TABLE_NAME;

    @Inject
    public AppDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
