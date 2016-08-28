package com.sorry.band.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class Database {
    private static SQLiteDatabase db = openOrCreateDatabase("BodyData.db", null);

    public static Cursor query(String sql){
        return db.rawQuery(sql, null);
    }

    public static void insert(String sql, ContentValues contentValues){
        db.insert(sql, null, contentValues);
    }


}
