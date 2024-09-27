package com.example.qrlector;


import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract() {}

    public static class QREntry implements BaseColumns {
        public static final String TABLE_NAME = "qr_results";
        public static final String COLUMN_NAME_RESULT = "result";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + QREntry.TABLE_NAME + " (" +
                    QREntry._ID + " INTEGER PRIMARY KEY," +
                    QREntry.COLUMN_NAME_RESULT + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + QREntry.TABLE_NAME;
}
