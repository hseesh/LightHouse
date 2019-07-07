package com.hse.yatoufang.MissionItemUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
     private Context context;
     private int dateBaseType;
     private static String[] name={"missionItem","missionItemState","diary"};

    public SQLiteHelper(Context context, int version,int type) {
        super(context, name[type - 1], null, version);
        this.context = context;
        this.dateBaseType = type;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        switch (dateBaseType){
            case 1:
                sqLiteDatabase.execSQL("create table if not exists missionItem (missionName varchar(10), missionLevel integer, missionCreattime char(10) primary key)");
                break;
            case 2:
                sqLiteDatabase.execSQL("create table if not exists missionItemState (diaryTime char(10), missionsSavetime char(19)primary key,missionItems varchar(400), missionLevels varchar(40), missiona_state  varchar(26))");
                break;
            case 3:
                sqLiteDatabase.execSQL("create table if not exists diary (diaryTime char(8),diaryCreattime char(10) primary key, diary varchar(600))");
                break;
            case 4:
                sqLiteDatabase.execSQL("create table if not exists myphoto(photoSavetime  char(10) primary key, photo blob )");
                break;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MiaTianBase.db");
        onCreate(sqLiteDatabase);
    }
}
