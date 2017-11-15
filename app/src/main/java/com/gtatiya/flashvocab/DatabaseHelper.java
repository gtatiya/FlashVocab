package com.gtatiya.flashvocab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gyan Tatiya on 26/Oct/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "VocabData.db";
    public static final String TABLE_NAME = "VocabTable";
    public static final String COL_1 = "Word_No";
    public static final String COL_2 = "score_MCQCard";
    public static final String COL_3 = "score_TypeInCard";
    public static final String COL_4 = "score_WordCard";
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
    public static final String COL_2_3 = "SequenceScore_WordCard";
    public static final String COL_3_3 = "Studied_New_WordCard";
    public static final String COL_4_3 = "Selected_New_WordCard";
    public static final String COL_5_3 = "Studied_Review_WordCard";
    public static final String COL_6_3 = "Selected_Review_WordCard";
    public static final String COL_7_3 = "SequenceScore_MCQCard";
    public static final String COL_8_3 = "Studied_New_MCQCard";
    public static final String COL_9_3 = "Selected_New_MCQCard";
    public static final String COL_10_3 = "Studied_Review_MCQCard";
    public static final String COL_11_3 = "Selected_Review_MCQCard";
    public static final String COL_12_3 = "SequenceScore_TypeInCard";
    public static final String COL_13_3 = "Studied_New_TypeInCard";
    public static final String COL_14_3 = "Selected_New_TypeInCard";
    public static final String COL_15_3 = "Studied_Review_TypeInCard";
    public static final String COL_16_3 = "Selected_Review_TypeInCard";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); // create the database
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertData(String antonym, String example, String meaning, String pos, String picture2_GCS_links, String score_MCQCard, String score_TypeInCard, String score_WordCard, String synonym, String word, String word_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (Word_No INTEGER PRIMARY KEY, score_MCQCard INTEGER, score_TypeInCard INTEGER, score_WordCard INTEGER, Word TEXT, POS TEXT, Meaning TEXT, Example TEXT, Picture TEXT, Synonym TEXT, Antonym TEXT);");

        cv.put(COL_1, word_no);
        cv.put(COL_2, score_MCQCard);
        cv.put(COL_3, score_TypeInCard);
        cv.put(COL_4, score_WordCard);
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

    public Cursor getByCardKey(int Word_No){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE Word_No ="+Word_No, null);
        return res;
    }

    public void updateScore_WordCardAgain(int Word_No){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_4, 0); // setting score to 0

        db.update(TABLE_NAME, cv, "Word_No = "+Word_No, null);
    }

    public void updateScore_WordCardGood(int Word_No){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor res = db.rawQuery("SELECT score_WordCard FROM "+TABLE_NAME+" WHERE Word_No = "+Word_No, null);
        res.moveToFirst();
        int newScore = res.getInt(0)+1;

        cv.put(COL_4, newScore); // increasing score by 1

        db.update(TABLE_NAME, cv, "Word_No = "+Word_No, null);
        res.close();
    }

    public void updateScore_WordCardEasy(int Word_No){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Cursor res = db.rawQuery("SELECT score_WordCard FROM "+TABLE_NAME+" WHERE Word_No = "+Word_No, null);
        res.moveToFirst();
        int newScore = res.getInt(0)+2;

        cv.put(COL_4, newScore); // increasing score by 2

        db.update(TABLE_NAME, cv, "Word_No = "+Word_No, null);
        res.close();
    }

    public void undoScore_WordCard(int Word_No, int lastScore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_4, lastScore);

        db.update(TABLE_NAME, cv, "Word_No = "+Word_No, null);
    }

    // Settings Table
    public void updateSettings(int noNewCards, int noReviewCards){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2_2, noNewCards); //These Fields should be your String values of actual column names
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
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME3 +" (Date_Key TEXT PRIMARY KEY, SequenceScore_WordCard TEXT, Studied_New_WordCard INTEGER DEFAULT 0, Selected_New_WordCard TEXT, Studied_Review_WordCard INTEGER DEFAULT 0, Selected_Review_WordCard TEXT, SequenceScore_MCQCard TEXT, Studied_New_MCQCard INTEGER DEFAULT 0, Selected_New_MCQCard TEXT, Studied_Review_MCQCard INTEGER DEFAULT 0, Selected_Review_MCQCard TEXT, SequenceScore_TypeInCard TEXT, Studied_New_TypeInCard INTEGER DEFAULT 0, Selected_New_TypeInCard TEXT, Studied_Review_TypeInCard INTEGER DEFAULT 0, Selected_Review_TypeInCard TEXT);");

        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);
        ContentValues cv = new ContentValues();

        Cursor cursor  = db.rawQuery("SELECT * FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        if(count == 0) {
            //Date_Key not present
            cv.put(COL_1_3, newDate); // key will be today's date
            // Setting selected new and review cards to empty string
            cv.put(COL_4_3, "");
            cv.put(COL_6_3, "");
            cv.put(COL_9_3, "");
            cv.put(COL_11_3, "");
            cv.put(COL_14_3, "");
            cv.put(COL_16_3, "");

            db.insert(TABLE_NAME3, null, cv);
        }
        cursor.close();
    }

    public void createScheduledCards_WordCard(){
        SQLiteDatabase db = this.getWritableDatabase();

        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);

        ContentValues cv = new ContentValues();
        Cursor cursor = getSettings();
        cursor.moveToFirst();
        int x = cursor.getInt(1);
        int y = cursor.getInt(2);

        cursor = db.rawQuery("SELECT Studied_New_WordCard, Studied_Review_WordCard, Selected_New_WordCard, Selected_Review_WordCard FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        int Studied_New_WordCard, Studied_Review_WordCard;
        String Selected_New_WordCard, Selected_Review_WordCard;
        cursor.moveToNext();
        Studied_New_WordCard = cursor.getInt(0);
        Studied_Review_WordCard = cursor.getInt(1);
        Selected_New_WordCard = cursor.getString(2);
        Selected_Review_WordCard = cursor.getString(3);

        int newX = Math.max(0, x - Studied_New_WordCard); // changes negative number to 0
        int newY = Math.max(0, y - Studied_Review_WordCard); // changes negative number to 0

        // Selecting New Cards
        cursor  = db.rawQuery("SELECT Word_No, score_WordCard FROM "+TABLE_NAME+" WHERE score_WordCard = 0 ORDER BY Word_No LIMIT "+newX, null);
        String SequenceScore_WordCard, excludeKeys;
        SequenceScore_WordCard = "";
        excludeKeys="";
        while (cursor.moveToNext()){
            SequenceScore_WordCard += cursor.getString(0)+","+cursor.getString(1)+",";
            Selected_New_WordCard += "'"+cursor.getString(0)+"',";
        }

        if (Selected_New_WordCard.length() == 0){
            if (Selected_Review_WordCard.length()>0){
                excludeKeys = Selected_Review_WordCard.substring(0, Selected_Review_WordCard.length() - 1); // removing last ","
            }
        }else if(Selected_Review_WordCard.length() == 0){
            if (Selected_New_WordCard.length()>0){
                excludeKeys = Selected_New_WordCard.substring(0, Selected_New_WordCard.length() - 1); // removing last ","
            }
        }else{
            excludeKeys = Selected_New_WordCard + Selected_Review_WordCard.substring(0, Selected_Review_WordCard.length() - 1);
        }

        // Selecting Review Cards except New cards selected today
        // It will not select review cards that has been selected today again - might happen if review card limit has not been reached
        cursor  = db.rawQuery("SELECT Word_No, score_WordCard FROM "+TABLE_NAME+" WHERE score_WordCard > 0 EXCEPT SELECT Word_No, score_WordCard FROM "+TABLE_NAME+" WHERE Word_No IN ("+excludeKeys+") ORDER BY score_WordCard LIMIT "+newY, null);
        while (cursor.moveToNext()){
            SequenceScore_WordCard += cursor.getString(0)+","+cursor.getString(1)+",";
            Selected_Review_WordCard += "'"+cursor.getString(0)+"',";
        }
        if (SequenceScore_WordCard.length()>0){
            SequenceScore_WordCard = SequenceScore_WordCard.substring(0, SequenceScore_WordCard.length() - 1); // removing last ","
        }

        cv.put(COL_2_3, SequenceScore_WordCard);
        cv.put(COL_4_3, Selected_New_WordCard);
        cv.put(COL_6_3, Selected_Review_WordCard);

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
        cursor.close();
    }

    public Cursor getSequenceScore_WordCard(){
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);
        Cursor res = db.rawQuery("SELECT SequenceScore_WordCard FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        return res;
    }

    public Cursor getStudied_WordCard(){
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);
        Cursor res = db.rawQuery("SELECT Studied_New_WordCard, Studied_Review_WordCard FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        return res;
    }

    public void increaseStudied_New_WordCard_by1(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);

        Cursor res = db.rawQuery("SELECT Studied_New_WordCard FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        res.moveToFirst();
        int newStudied_X = res.getInt(0)+1;

        cv.put(COL_3_3, newStudied_X); // increasing score by 1

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
        res.close();
    }

    public void increaseStudied_Review_WordCard_by1(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);

        Cursor res = db.rawQuery("SELECT Studied_Review_WordCard FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        res.moveToFirst();
        int newStudied_Y = res.getInt(0)+1;

        cv.put(COL_5_3, newStudied_Y); // increasing score by 1

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
        res.close();
    }

    public void decreaseStudied_New_WordCard_by1(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);

        Cursor res = db.rawQuery("SELECT Studied_New_WordCard FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        res.moveToFirst();
        int newStudied_X = res.getInt(0)-1;

        cv.put(COL_3_3, newStudied_X); // increasing score by 1

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
        res.close();
    }

    public void decreaseStudied_Review_WordCard_by1(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        String newDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);

        Cursor res = db.rawQuery("SELECT Studied_Review_WordCard FROM "+TABLE_NAME3+" WHERE Date_Key = '"+newDate+"'", null);
        res.moveToFirst();
        int newStudied_Y = res.getInt(0)-1;

        cv.put(COL_5_3, newStudied_Y); // increasing score by 1

        db.update(TABLE_NAME3, cv, "Date_Key = '"+newDate+"'", null);
        res.close();
    }
}