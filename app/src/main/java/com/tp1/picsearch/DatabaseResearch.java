package com.tp1.picsearch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseResearch extends SQLiteOpenHelper {
    /**
     * @goals This class aims to show the constant to use for the DBOpenHelper
     */
    public static class Constants implements BaseColumns {

        // The database name
        public static final String DATABASE_NAME = "myData.db";

        // The database version
        public static final int DATABASE_VERSION = 1;

        // The table Name
        public static final String MY_TABLE = "Recherche";


        // ## Column name ##
        public static final String KEY_COL_DATE = "date";// Mandatory

        public static final String KEY_COL_RECHERCHE = "recherche";


        // Indexes des colonnes
        // The index of the column RECHERCHE
        public static final int DATE_RECHERCHE = 1;

        // The index of the column WEBSITE
        public static final int RECHERCHE_COLUMN = 2;
    }

    /**
     * The static string to create the database.
     */
    private static final String DATABASE_CREATE = "create table "
            + Constants.MY_TABLE + "(" + Constants.KEY_COL_DATE
            + " TEXT, " + Constants.KEY_COL_RECHERCHE
            + " TEXT)";


    /**
     * @param context
     *            = the context of the caller
     * @param name
     *            = Database's name
     * @param factory
     *            = null
     * @param version
     *            = Database's version
     */
    public DatabaseResearch(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the new database using the SQL string Database_create
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DBOpenHelper", "Mise à jour de la version " + oldVersion
                + " vers la version " + newVersion
                + ", les anciennes données seront détruites ");
        // Drop the old database
        db.execSQL("DROP TABLE IF EXISTS " + Constants.MY_TABLE);
        // Create the new one
        onCreate(db);
        // or do a smartest stuff
    }
}

