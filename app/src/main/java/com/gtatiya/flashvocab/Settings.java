package com.gtatiya.flashvocab;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity {
    Button bSubmit;
    DatabaseHelper myDB;
    EditText etNoNewCards_WordCard, etNoReviewCards_WordCard, etNoNewCards_MCQCard, etNoReviewCards_MCQCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        bSubmit = (Button) findViewById(R.id.bSubmit);
        myDB = new DatabaseHelper(this);
        etNoNewCards_WordCard = (EditText) findViewById(R.id.etNoNewCards_WordCard);
        etNoReviewCards_WordCard = (EditText) findViewById(R.id.etNoReviewCards_WordCard);
        etNoNewCards_MCQCard = (EditText) findViewById(R.id.etNoNewCards_MCQCard);
        etNoReviewCards_MCQCard = (EditText) findViewById(R.id.etNoReviewCards_MCQCard);

        readSettings();

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newWordCards, reviewWordCards, newMCQCards, reviewMCQCards;
                newWordCards = 0;
                reviewWordCards = 0;
                newMCQCards = 0;
                reviewMCQCards = 0;

                // Word Cards
                try {
                    newWordCards =  Integer.parseInt(etNoNewCards_WordCard.getText().toString());
                } catch (NumberFormatException e) {
                    // log and do something else like notify the user or set i to a default value
                    etNoNewCards_WordCard.setText("0");
                }

                try {
                    reviewWordCards = Integer.parseInt(etNoReviewCards_WordCard.getText().toString());
                } catch (NumberFormatException e) {
                    // log and do something else like notify the user or set i to a default value
                    etNoReviewCards_WordCard.setText("0");
                }

                // MCQ Cards
                try {
                    newMCQCards =  Integer.parseInt(etNoNewCards_MCQCard.getText().toString());
                } catch (NumberFormatException e) {
                    // log and do something else like notify the user or set i to a default value
                    etNoNewCards_MCQCard.setText("0");
                }

                try {
                    reviewMCQCards = Integer.parseInt(etNoReviewCards_MCQCard.getText().toString());
                } catch (NumberFormatException e) {
                    // log and do something else like notify the user or set i to a default value
                    etNoReviewCards_MCQCard.setText("0");
                }

                myDB.updateSettings_WordCard(newWordCards, reviewWordCards);
                myDB.updateSettings_MCQCard(newMCQCards, reviewMCQCards);

                Intent i=new Intent(Settings.this, HomeScreen.class);
                startActivity(i);
            }
        });
    }

    public void readSettings(){
        Cursor res = myDB.getSettings();
        if (res.getCount() == 0){
            return;
        }

        while (res.moveToNext()){
            etNoNewCards_WordCard.setText(res.getString(1));
            etNoReviewCards_WordCard.setText(res.getString(2));
            etNoNewCards_MCQCard.setText(res.getString(3));
            etNoReviewCards_MCQCard.setText(res.getString(4));
        }
    }
}
