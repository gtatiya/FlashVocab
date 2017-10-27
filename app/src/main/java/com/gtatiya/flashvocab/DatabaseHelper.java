package com.gtatiya.flashvocab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gyan Tatiya on 26/Oct/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "VocabData.db";
    public static final String TABLE_NAME = "VocabTable";
    public static final String COL_1 = "Word_No";
    public static final String COL_2 = "Card_Key";
    public static final String COL_3 = "Card_Type";
    public static final String COL_4 = "Schedule_Score";
    public static final String COL_5 = "Word";
    public static final String COL_6 = "POS";
    public static final String COL_7 = "Meaning";
    public static final String COL_8 = "Example";
    public static final String COL_9 = "Picture";
    public static final String COL_10 = "Synonym";
    public static final String COL_11 = "Antonym";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); // create the database
        //SQLiteDatabase db = this.getWritableDatabase(); // create database and table
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the table
        db.execSQL("create table " + TABLE_NAME +" (Word_No INTEGER, Card_Key INTEGER PRIMARY KEY, Card_Type TEXT, Schedule_Score INTEGER, Word TEXT, POS TEXT, Meaning TEXT, Example TEXT, Picture TEXT, Synonym TEXT, Antonym TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop the table if it already exists
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String antonym, String card_key, String card_type, String example, String meaning, String pos, String picture2_GCS_links, String schedule_score, String synonym, String word, String word_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_1, word_no);
        cv.put(COL_2, card_key);
        cv.put(COL_3, card_type);
        cv.put(COL_4, schedule_score);
        cv.put(COL_5, word);
        cv.put(COL_6, pos);
        cv.put(COL_7, meaning);
        cv.put(COL_8, example);
        cv.put(COL_9, picture2_GCS_links);
        cv.put(COL_10, synonym);
        cv.put(COL_11, antonym);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            return false;
        } else{
            return true;
        }

        //db.close();
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return res;
    }

    public Cursor getFirstData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE Card_Key = 1", null);
        return res;
    }

    public Cursor getByCardKey(int Card_Key){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE Card_Key ="+Card_Key, null);
        return res;
    }
}