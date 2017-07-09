package com.example.siamakmohsenisam.budget.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by siamakmohsenisam on 2017-06-25.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {


    public DatabaseOpenHelper(Context context) {
        this(context, DatabaseSchema.DATABASE_NAME.getValue(), null,Integer.valueOf(DatabaseSchema.VERSION.getValue()));
    }

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DatabaseSchema.CREATE_ACCOUNT.getValue());
        db.execSQL(DatabaseSchema.CREATE_BUDGET.getValue());
        db.execSQL(DatabaseSchema.CREATE_CATEGORY.getValue());

        Log.d("DATABASE","The database  Is created successfully in "+ db.getPath());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
