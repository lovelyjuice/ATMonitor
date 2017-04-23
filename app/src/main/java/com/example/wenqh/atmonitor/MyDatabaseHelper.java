package com.example.wenqh.atmonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wenqh on 2017/3/15.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper
{
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table UserInfo(id integer primary key autoincrement,name text,passwd text,balance real)");
        db.execSQL("create table OperationHistory(id integer,mount real,balance real,time integer,operation integer)");
        db.execSQL("insert into UserInfo(id,name,balance,passwd) values(?,?,?,?)",new String[]{"10000","wenqihui","66.66","123456"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
