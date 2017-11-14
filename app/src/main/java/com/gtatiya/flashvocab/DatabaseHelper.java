package com.gtatiya.flashvocab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static final String TABLE_NAME2 = "Settings";
    public static final String COL_1_2 = "Setting_Key";
    public static final String COL_2_2 = "No_New_cards";
    public static final String COL_3_2 = "No_Review_Cards";

    public static final String TABLE_NAME3 = "ScheduledCards";
    public static final String COL_1_3 = "Date_Key";
    public static final String COL_2_3 = "No_New_cards";
    public static final String COL_3_3 = "No_Review_Cards";
    public static final String COL_4_3 = "Quiz_Type";
    public static final String COL_5_3 = "STUDIED_X";
    public static final String COL_6_3 = "STUDIED_Y";
    public static final String COL_7_3 = "SEQUENCE_SCORE";
    public static final String COL_8_3 = "Selected_New_Cards";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); // create the database

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertData(String antonym, String card_key, String card_type, String example, String meaning, String pos, String picture2_GCS_links, String schedule_score, String synonym, String word, String word_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (Word_No INTEGER, Card_Key INTEGER PRIMARY KEY, Card_Type TEXT, Schedule_Score INTEGER, Word TEXT, POS TEXT, Meaning TEXT, Example TEXT, Picture TEXT, Synonym TEXT, Antonym TEXT);");

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

        db.insert(TABLE_NAME, null, cv);
    }

    // VocabTable Table

    // https://stackoverflow.com/questions/3058909/how-does-one-check-if-a-table-exists-in-an-android-sqlite-database
    // check if a table exists
    public boolean checkTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public Cursor getByCardKey(int Card_Key){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE Card_Key ="+Card_Key, null);
        return res;
    }

    public void updateScoreAgain(int Card_Key){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_4, 0); // setting score to 0

        db.update(TABLE_NAME, cv, "Card_Key = "+Card_Key, null);
    }

    public void updateScoreGood(int Card_Key){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor res = db.rawQuery("SELECT Schedule_Score FROM "+TABLE_NAME+" WHERE Card_Key = "+Card_Key, null);
        res.moveToFirst();
        int newScore = res.getInt(0)+1;

        cv.put(COL_4, newScore); // increasing score by 1

        db.update(TABLE_NAME, cv, "Card_Key = "+Card_Key, null);
    }

    public void updateScoreEasy(int Card_Key){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor res = db.rawQuery("SELECT Schedule_Score FROM "+TABLE_NAME+" WHERE Card_Key = "+Card_Key, null);
        res.moveToFirst();
        int newScore = res.getInt(0)+2;

        cv.put(COL_4, newScore); // increasing score by 2

        db.update(TABLE_NAME, cv, "Card_Key = "+Card_Key, null);
    }

    public void undoScore(int Card_Key, int lastScore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_4, lastScore);

        db.update(TABLE_NAME, cv, "Card_Key = "+Card_Key, null);
    }

    // Settings Table
    public void updateSettings(int noNewCArds, int noReviewCards){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2_2, noNewCArds); //These Fields should be your String values of actual column names
        cv.put(COL_3_2, noReviewCards);

        db.update(TABLE_NAME2, cv, "Setting_Key = 1", null);
    }

    public Cursor getSettings(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME2+" WHERE Setting_Key = 1", null);
        return res;
    }

    public void createSetting(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 +" (Setting_Key INTEGER PRIMARY KEY, No_New_cards INTEGER, No_Review_Cards INTEGER);");

        ContentValues cv = new ContentValues();
        cv.put(COL_1_2, 1); // key will always be 1
        cv.put(COL_2_2, 10); // default value
        cv.put(COL_3_2, 15); // default value

        db.insert(TABLE_NAME2, null, cv);
    }

    // ScheduledCards Table
    // https://stackoverflow.com/questions/32895645/how-to-apply-select-datenow-query-in-android
    // https://stackoverflow.com/questions/4847875/android-database-cursorindexoutofboundsexception-index-1-requested-with-a-siz
    // https://stackoverflow.com/questions/13446641/android-sqlite-how-do-i-check-if-the-value-in-database-already-exists
    public void createScheduledCardsFirstTime(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME3 +" (Date_Key TEXT PRIMARY KEY, No_New_cards INTEGER, No_Review_Cards INTEGER, Quiz_Type TEXT, STUDIED_X INTEGER DEFAULT 0, STUDIED_Y INTEGER DEFAULT 0, SEQUENCE_SCORE TEXT, Selected_New_Cards TEXT);");

        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        ContentValues cv = new ContentValues();
        Cursor cursor = getSettings();
        cursor.moveToFirst();
        int x = cursor.getInt(1);
        int y = cursor.getInt(2);

        cursor  = db.rawQuery("SELECT * FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        if(count == 0) {
            //Date_Key not present
            cv.put(COL_1_3, newDate); // key will be today's date
            cv.put(COL_8_3, "");

            db.insert(TABLE_NAME3, null, cv);
        }

    }

    public void createScheduledCardsLater(){
        SQLiteDatabase db = this.getWritableDatabase();

        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        ContentValues cv = new ContentValues();
        Cursor cursor = getSettings();
        cursor.moveToFirst();
        int x = cursor.getInt(1);
        int y = cursor.getInt(2);

        cv.put(COL_2_3, x);
        cv.put(COL_3_3, y);

        Cursor res = db.rawQuery("SELECT STUDIED_X, STUDIED_Y, Selected_New_Cards FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        int studiedX, studiedY;
        String newCardsKey;
        res.moveToNext();
        studiedX = res.getInt(0);
        studiedY = res.getInt(1);
        newCardsKey = res.getString(2);
        int newX = x - studiedX;
        int newY = y - studiedY;

        // Selecting New Cards
        cursor  = db.rawQuery("SELECT Card_Key, Schedule_Score FROM "+TABLE_NAME+" WHERE Card_Type = 'Type 1' AND Schedule_Score = 0 ORDER BY Card_Key LIMIT "+newX, null);
        String s, newCardsKey2;
        s= "";
        newCardsKey2="";
        while (cursor.moveToNext()){
            s += cursor.getString(0)+","+cursor.getString(1)+",";
            newCardsKey += "'"+cursor.getString(0)+"',";
        }
        if (newCardsKey.length()>0){
            newCardsKey2 = newCardsKey.substring(0, newCardsKey.length() - 1); // removing last ","
        }
        // Selecting Review Cards except New cards selected today
        cursor  = db.rawQuery("SELECT Card_Key, Schedule_Score FROM "+TABLE_NAME+" WHERE Card_Type = 'Type 1' AND Schedule_Score > 0 EXCEPT SELECT Card_Key, Schedule_Score FROM "+TABLE_NAME+" WHERE Card_Key IN ("+newCardsKey2+") ORDER BY Schedule_Score LIMIT "+newY, null);
        while (cursor.moveToNext()){
            s += cursor.getString(0)+","+cursor.getString(1)+",";
        }
        if (s.length()>0){
            s = s.substring(0, s.length() - 1); // removing last ","
        }

        cv.put(COL_7_3, s);
        cv.put(COL_8_3, newCardsKey);

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
    }

    public Cursor getSequenceScore(){
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        Cursor res = db.rawQuery("SELECT SEQUENCE_SCORE FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        return res;
    }

    public Cursor getStudied_XY(){
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        Cursor res = db.rawQuery("SELECT STUDIED_X, STUDIED_Y FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        return res;
    }

    public void increaseStudied_Xby1(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        Cursor res = db.rawQuery("SELECT STUDIED_X FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        res.moveToFirst();
        int newStudied_X = res.getInt(0)+1;

        cv.put(COL_5_3, newStudied_X); // increasing score by 1

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
    }

    public void increaseStudied_Yby1(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        Cursor res = db.rawQuery("SELECT STUDIED_Y FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        res.moveToFirst();
        int newStudied_Y = res.getInt(0)+1;

        cv.put(COL_6_3, newStudied_Y); // increasing score by 1

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
    }

    public void decreaseStudied_Xby1(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        Cursor res = db.rawQuery("SELECT STUDIED_X FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        res.moveToFirst();
        int newStudied_X = res.getInt(0)-1;

        cv.put(COL_5_3, newStudied_X); // increasing score by 1

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
    }

    public void decreaseStudied_Yby1(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        Cursor res = db.rawQuery("SELECT STUDIED_Y FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        res.moveToFirst();
        int newStudied_Y = res.getInt(0)-1;

        cv.put(COL_6_3, newStudied_Y); // increasing score by 1

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
    }

}