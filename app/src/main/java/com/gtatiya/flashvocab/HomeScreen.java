package com.gtatiya.flashvocab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    Button bStudyVocab;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.home_screen);
        imageView= (ImageView) findViewById(R.id.img_splash);

        myDB = new DatabaseHelper(this);

        bStudyVocab = (Button) findViewById(R.id.bStudyVocab);

        Glide.with(this).load("https://vergecampus.com/wp-content/uploads/2015/04/flashcards.png").into(imageView);
        //Glide.with(this).load("https://drive.google.com/uc?export=view&id=0B6uiOVPlyIu-UVVCWm5EVThtUW8").into(imageView);
        //Glide.with(this).load("https://drive.google.com/uc?export=view&id=0B6uiOVPlyIu-OTR1VjV2VEplZGc").into(imageView);
        //Glide.with(this).load("https://drive.google.com/uc?export=view&id=0B6uiOVPlyIu-MkdyMWE1cWd6Rlk").into(imageView);

        //Google Could Platform
        //Glide.with(this).load("https://storage.googleapis.com/staging.my-first-cloud-app-gtatiya.appspot.com/FlashVocab/Age%20is%20just%20number.jpg").into(imageView);
//        Glide.with(this).load("https://storage.googleapis.com/staging.my-first-cloud-app-gtatiya.appspot.com/FlashVocab/Age is just number.jpg").into(imageView);
//        Glide.with(this).load("https://storage.googleapis.com/staging.my-first-cloud-app-gtatiya.appspot.com/FlashVocab/2017.10.11 HZ chat.jpg").into(imageView);
//        Glide.with(this).load("https://storage.googleapis.com/staging.my-first-cloud-app-gtatiya.appspot.com/FlashVocab/GYan.jpg").into(imageView);

        text2SQLite();

        bStudyVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(HomeScreen.this, Main2Activity.class);
                //i.putExtra("platform","Android");
                startActivity(i);
            }
        });

    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }


    public void text2SQLite() {
        String data = "";

        StringBuffer sbuffer = new StringBuffer();
        InputStream is = this.getResources().openRawResource(R.raw.word_db);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        //List<Map<String, String>> maps = new ArrayList<Map<String, String>>();

        if (is != null) {
            try {
                int n = 1;
                String antonym, card_key, card_type, example, meaning, pos, picture2_GCS_links, schedule_score, synonym, word, word_no;
                antonym = "";
                card_key = "";
                card_type = "";
                example = "";
                meaning = "";
                pos = "";
                picture2_GCS_links = "";
                schedule_score = "";
                synonym = "";
                word = "";
                word_no = "";

                while ((data = reader.readLine()) != null) {
                    int m = n % 11;
                    if (m == 1) {
                        antonym = data;
                    }
                    if (m == 2) {
                        card_key = data;
                    }
                    if (m == 3) {
                        card_type = data;
                    }
                    if (m == 4) {
                        example = data;
                    }
                    if (m == 5) {
                        meaning = data;
                    }
                    if (m == 6) {
                        pos = data;
                    }
                    if (m == 7) {
                        picture2_GCS_links = data;
                    }
                    if (m == 8) {
                        schedule_score = data;
                    }
                    if (m == 9) {
                        synonym = data;
                    }
                    if (m == 10) {
                        word = data;
                    }
                    if (m == 0) {
                        word_no = data;

                        boolean isInserted = myDB.insertData(antonym, card_key, card_type, example, meaning, pos, picture2_GCS_links, schedule_score, synonym, word, word_no);
                        if (isInserted == true){
                            Toast.makeText(HomeScreen.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(HomeScreen.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                        }

                        //Map<String, String> map = javamaps2(antonym, card_key, card_type, example, meaning, pos, picture2_GCS_links, schedule_score, synonym, word, word_no);
                        //maps.add(map);
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