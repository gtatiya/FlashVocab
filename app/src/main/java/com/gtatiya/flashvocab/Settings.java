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
    EditText etNoNewCards, etNoReviewCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        bSubmit = (Button) findViewById(R.id.bSubmit);
        myDB = new DatabaseHelper(this);
        etNoNewCards = (EditText) findViewById(R.id.etNoNewCards);
        etNoReviewCards = (EditText) findViewById(R.id.etNoReviewCards);

        readSettings();

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n1, n2;
                n1 = 0;
                n2 = 0;

                try {
                    n1 =  Integer.parseInt(etNoNewCards.getText().toString());
                } catch (NumberFormatException e) {
                    // log and do something else like notify the user or set i to a default value
                    etNoNewCards.setText("0");
                }

                try {
                    n2 = Integer.parseInt(etNoReviewCards.getText().toString());
                } catch (NumberFormatException e) {
                    // log and do something else like notify the user or set i to a default value
                    etNoReviewCards.setText("0");
                }

                myDB.updateSettings(n1, n2);
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
            etNoNewCards.setText(res.getString(1));
            etNoReviewCards.setText(res.getString(2));
        }
    }
}
