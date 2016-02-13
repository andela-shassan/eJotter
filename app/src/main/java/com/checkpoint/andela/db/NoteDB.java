package com.checkpoint.andela.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.checkpoint.andela.model.NoteModel;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by andela on 03/02/2016.
 */
public class NoteDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "note.db";
    private static final int DB_VERSION = 1;

    public NoteDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    static {
        cupboard().register(NoteModel.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

}