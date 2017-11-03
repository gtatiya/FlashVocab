package com.gtatiya.flashvocab;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class Main2Activity extends AppCompatActivity {
    DatabaseHelper myDB;
    TextView tvWord, tvPOS, tvMeaning, tvExample, tvSynonym, tvAntonym;
    ImageView ivPic;
    Button bNextCard;

    int j = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main2);

        myDB = new DatabaseHelper(this);

        tvWord = (TextView) findViewById(R.id.tvWord);
        tvPOS = (TextView) findViewById(R.id.tvPOS);
        tvMeaning = (TextView) findViewById(R.id.tvMeaning);
        tvExample = (TextView) findViewById(R.id.tvExample);
        tvSynonym = (TextView) findViewById(R.id.tvSynonym);
        tvAntonym = (TextView) findViewById(R.id.tvAntonym);

        ivPic = (ImageView) findViewById(R.id.ivPic);

        bNextCard = (Button) findViewById(R.id.bNextCard);

        viewALL();


        bNextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent i=new Intent(Main2Activity.class, Main2Activity.class);
                //i.putExtra("platform","Android");
                //startActivity(i);


                Cursor res = myDB.getByCardKey(j);
                if (res.getCount() == 0){
                    //showMessage("Error", "Nothing Found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("Word_No: "+res.getString(0)+"\n" );
                    buffer.append("Card_Key: "+res.getString(1)+"\n" );
                    buffer.append("Card_Type: "+res.getString(2)+"\n" );
                    buffer.append("Schedule_Score: "+res.getString(3)+"\n" );
                    buffer.append("Word: "+res.getString(4)+"\n" );
                    tvWord.setText(res.getString(4).toString());
                    buffer.append("POS: "+res.getString(5)+"\n" );
                    tvPOS.setText(res.getString(5).toString());
                    buffer.append("Meaning: "+res.getString(6)+"\n" );
                    tvMeaning.setText(res.getString(6).toString());
                    buffer.append("Example: "+res.getString(7)+"\n" );
                    tvExample.setText(res.getString(7).toString());
                    buffer.append("Picture: "+res.getString(8)+"\n" );
                    if (res.getString(8) != "[]"){
                        String[] values1 = picList(res.getString(8));
                        Glide.with(Main2Activity.this).load(values1[0]).into(ivPic);
                    }
                    buffer.append("Synonym: "+res.getString(9)+"\n" );
                    tvSynonym.setText(res.getString(9).toString());
                    buffer.append("Antonym: "+res.getString(10)+"\n\n" );
                    tvAntonym.setText(res.getString(10).toString());
                }
                j+=3;
                if (j > 15){
                    //Intent i=new Intent(Main2Activity.this, MainActivity.class);
                    //i.putExtra("platform","Android");
                    //startActivity(i);
                    bNextCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i=new Intent(Main2Activity.this, HomeScreen.class);
                            //i.putExtra("platform","Android");
                            startActivity(i);
                        }
                    });
                }
            }
        });
    }

    public void viewALL(){
        Cursor res = myDB.getFirstData();
        if (res.getCount() == 0){
            //showMessage("Error", "Nothing Found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append("Word_No: "+res.getString(0)+"\n" );
            buffer.append("Card_Key: "+res.getString(1)+"\n" );
            buffer.append("Card_Type: "+res.getString(2)+"\n" );
            buffer.append("Schedule_Score: "+res.getString(3)+"\n" );
            buffer.append("Word: "+res.getString(4)+"\n" );
            tvWord.setText(res.getString(4).toString());
            buffer.append("POS: "+res.getString(5)+"\n" );
            tvPOS.setText(res.getString(5).toString());
            buffer.append("Meaning: "+res.getString(6)+"\n" );
            tvMeaning.setText(res.getString(6).toString());
            buffer.append("Example: "+res.getString(7)+"\n" );
            tvExample.setText(res.getString(7).toString());
            buffer.append("Picture: "+res.getString(8)+"\n" );
            if (res.getString(8) != "[]"){
                String[] values1 = picList(res.getString(8));
                Glide.with(Main2Activity.this).load(values1[0]).into(ivPic);
            }
            buffer.append("Synonym: "+res.getString(9)+"\n" );
            tvSynonym.setText(res.getString(9).toString());
            buffer.append("Antonym: "+res.getString(10)+"\n\n" );
            tvAntonym.setText(res.getString(10).toString());

        }
        //showMessage("Data", buffer.toString());
    }

    public String[] picList(String s1){
        s1 = s1.replace("[", "");
        s1 = s1.replace("]", "");
        String[] values1 = s1.split(",");
        for (int i = 0; i < values1.length; i++) {
            values1[i] = values1[i].replace("'", "");
            values1[i] = values1[i].replaceAll("^\\s+",""); // strip the first whitespace
        }
        return values1;
    }
}
