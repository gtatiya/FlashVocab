package com.gtatiya.flashvocab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.crashlytics.android.Crashlytics;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import io.fabric.sdk.android.Fabric;

/**
 * Created by Gyan Tatiya on 3/Oct/17.
 */

public class HomeScreen extends AppCompatActivity {
    DatabaseHelper myDB;
    Button bStudyVocab, bSettings;
    ImageView imageView;

    public static int[] new_review_CardsIntArray;

    int[] Studied_XY;
    int[] Settings_XY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.home_screen);
        imageView= (ImageView) findViewById(R.id.img_splash);

        myDB = new DatabaseHelper(this);

        bStudyVocab = (Button) findViewById(R.id.bStudyVocab);
        bSettings = (Button) findViewById(R.id.bSettings);

        imageView.setImageResource(R.drawable.flashvocab_pic);

        boolean isInserted = myDB.checkTable("VocabTable");
        if (!isInserted){
            text2SQLite();
        }

        isInserted = myDB.checkTable("Settings");
        if (!isInserted){
            myDB.createSetting();
        }


        myDB.createScheduledCardsFirstTime();

        Studied_XY = readStudied_XY();
        Settings_XY = readSettings();


        bStudyVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings_XY[0]<=Studied_XY[0] && Settings_XY[1]<=Studied_XY[1]){
                    showMessage("Congratulations!! You are all set for the day", "Today's review limit has been reached, but you can study more cards by increasing the daily limit in the settings.");
                }else {
                    myDB.createScheduledCards_WordCard();
                    new_review_CardsIntArray = readSequenceScoreIntArray();
                    // If there's no cards scheduled the new_review_CardsIntArray length is 1
                    if (new_review_CardsIntArray.length == 1){
                        showMessage("Congratulations!! You are all set for the day", "Today's review limit has been reached, but you can study more cards by increasing the daily limit in the settings.");
                    }else{
                        Intent i = new Intent(HomeScreen.this, WordCard.class);
                        startActivity(i);
                    }
                }
            }
        });

        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(HomeScreen.this, Settings.class);
                startActivity(i);
            }
        });

    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    // Reading today's scheduled cards and scores from Database
    // Storing new and review cards in String of format "1,0,7,0,13,68,4,420"
    // Here odd index are Card_Key and even index are its Schedule_Score
    // Then, it converts array of string to array of int[]
    public int[] readSequenceScoreIntArray(){
        Cursor res = myDB.getSequenceScore_WordCard();

        String sequenceScore;
        sequenceScore = "";
        while (res.moveToNext()){
            sequenceScore = res.getString(0);
        }
        String[] sequenceScoreArray = sequenceScore.split(",");

        // converting array of string to array of int
        int[] i2 = new int[sequenceScoreArray.length];
        if (sequenceScore.equals("")){
            return i2;
        }else{
            for (int i = 0; i < sequenceScoreArray.length; i++) {
                i2[i] = Integer.parseInt(sequenceScoreArray[i]);
            }
            return i2;
        }
    }

    // This function will read today's studies new and review cards
    // Store it in array of integer xy
    // use xy[0] to get new cards and xy[1] to get review cards
    public int[] readStudied_XY(){
        int[] xy = new int[2];
        Cursor res = myDB.getStudied_WordCard();

        while (res.moveToNext()){
            xy[0] = res.getInt(0);
            xy[1] = res.getInt(1);
        }
        return xy;
    }

    // This function will read settings: no. of new and review cards
    // Store it in array of integer xy
    // use xy[0] to get new cards and xy[1] to get review cards
    public int[] readSettings(){
        int[] xy = new int[2];
        Cursor res = myDB.getSettings();

        while (res.moveToNext()){
            xy[0] = res.getInt(1);
            xy[1] = res.getInt(2);
        }
        return xy;
    }


    public void text2SQLite() {
        String data = "";

        StringBuffer sbuffer = new StringBuffer();
        InputStream is = this.getResources().openRawResource(R.raw.word_db);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        if (is != null) {
            try {
                int n = 1;
                String antonym, example, meaning, pos, picture2_GCS_links, score_MCQCard, score_TypeInCard, score_WordCard, synonym, word, word_no;
                antonym = "";
                example = "";
                meaning = "";
                pos = "";
                picture2_GCS_links = "";
                score_MCQCard = "";
                score_TypeInCard = "";
                score_WordCard = "";
                synonym = "";
                word = "";
                word_no = "";

                while ((data = reader.readLine()) != null) {
                    int m = n % 11;
                    if (m == 1) {
                        antonym = data;
                    }
                    if (m == 2) {
                        example = data;
                    }
                    if (m == 3) {
                        meaning = data;
                    }
                    if (m == 4) {
                        pos = data;
                    }
                    if (m == 5) {
                        picture2_GCS_links = data;
                    }
                    if (m == 6) {
                        score_MCQCard = data;
                    }
                    if (m == 7) {
                        score_TypeInCard = data;
                    }
                    if (m == 8) {
                        score_WordCard = data;
                    }
                    if (m == 9) {
                        synonym = data;
                    }
                    if (m == 10) {
                        word = data;
                    }
                    if (m == 0) {
                        word_no = data;

                        myDB.insertData(antonym, example, meaning, pos, picture2_GCS_links, score_MCQCard, score_TypeInCard, score_WordCard, synonym, word, word_no);
                    }
                    n += 1;
                }
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}