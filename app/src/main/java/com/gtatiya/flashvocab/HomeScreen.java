package com.gtatiya.flashvocab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Gyan Tatiya on 3/Oct/17.
 */

public class HomeScreen extends AppCompatActivity {


    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        imageView= (ImageView) findViewById(R.id.img_splash);

        //Glide.with(this).load("https://vergecampus.com/wp-content/uploads/2015/04/flashcards.png").into(imageView);
        Glide.with(this).load("https://drive.google.com/uc?export=view&id=0B6uiOVPlyIu-UVVCWm5EVThtUW8").into(imageView);
        //Glide.with(this).load("https://drive.google.com/uc?export=view&id=0B6uiOVPlyIu-OTR1VjV2VEplZGc").into(imageView);
        //Glide.with(this).load("https://drive.google.com/uc?export=view&id=0B6uiOVPlyIu-MkdyMWE1cWd6Rlk").into(imageView);


    }
}