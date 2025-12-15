package com.alexis.timmaps.data.local;

import android.provider.BaseColumns;

public final class PersistenceContract {

    private PersistenceContract() {
    }

    public static class Backup implements BaseColumns {
        public static final String TABLE_NAME = "backup_local";
        public static final String COLUMN_NAME_LABEL = "etiqueta1d";
        public static final String COLUMN_NAME_LAT = "latitud";
        public static final String COLUMN_NAME_LON = "longitud";
        public static final String COLUMN_NAME_OBSERVATION = "observacion";
    }
}
