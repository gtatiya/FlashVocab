package com.gtatiya.flashvocab;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MCQCard extends AppCompatActivity {
    DatabaseHelper myDB;
    ViewFlipper mViewFlipper;
    TextView tvMeaning, tvMeaning2;
    RadioButton option1, option1_2, option2, option2_2, option3, option3_2, option4, option4_2;
    Button bShowAns, bAgain, bGood, bEasy, bPlay;
    RadioGroup rgOptions, rgOptions2;

    public static String word;
    HomeScreen homescreen;
    String option1_value, option2_value, option3_value, option4_value;

    int[] SequenceScoreIntArray;

    int indexOfSequenceScoreIntArray = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcq_card);

        myDB = new DatabaseHelper(this);
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);

        tvMeaning = (TextView) findViewById(R.id.tvMeaning);
        tvMeaning2 = (TextView) findViewById(R.id.tvMeaning2);

        rgOptions = (RadioGroup) findViewById(R.id.rgOptions);
        rgOptions2 = (RadioGroup) findViewById(R.id.rgOptions2);
        option1 = (RadioButton) findViewById(R.id.option1);
        option1_2 = (RadioButton) findViewById(R.id.option1_2);
        option2 = (RadioButton) findViewById(R.id.option2);
        option2_2 = (RadioButton) findViewById(R.id.option2_2);
        option3 = (RadioButton) findViewById(R.id.option3);
        option3_2 = (RadioButton) findViewById(R.id.option3_2);
        option4 = (RadioButton) findViewById(R.id.option4);
        option4_2 = (RadioButton) findViewById(R.id.option4_2);

        bShowAns = (Button) findViewById(R.id.bShowAns);
        bAgain = (Button) findViewById(R.id.bAgain);
        bGood = (Button) findViewById(R.id.bGood);
        bEasy = (Button) findViewById(R.id.bEasy);
        bPlay = (Button) findViewById(R.id.bPlay);

        SequenceScoreIntArray = homescreen.new_review_CardsIntArray_MCQCard;

        //SequenceScoreIntArray = new int[]{1162, 0, 978, 0, 1144, 0, 715, 0, 1045, 0, 1043, 0, 1042, 0, 938, 0, 850, 0};

        System.out.println("Array of Integer: SequenceScoreIntArray");
        System.out.println(Arrays.toString(SequenceScoreIntArray));
        viewCard(SequenceScoreIntArray[0]); // displaying the first card
        fillOptions();

        bShowAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!option1.isChecked() && !option2.isChecked() && !option3.isChecked() && !option4.isChecked()) {
                    Toast.makeText(MCQCard.this, "Please select your answer", Toast.LENGTH_SHORT).show();
                }else {

                    String selectedOption = ((RadioButton)findViewById(rgOptions.getCheckedRadioButtonId())).getText().toString();

                    option1_value = option1.getText().toString();
                    option1_2.setText(option1_value);
                    option2_value = option2.getText().toString();
                    option2_2.setText(option2_value);
                    option3_value = option3.getText().toString();
                    option3_2.setText(option3_value);
                    option4_value = option4.getText().toString();
                    option4_2.setText(option4_value);

                    if (option1.isChecked()){
                        option1_2.setChecked(true);
                    }
                    if (option2.isChecked()){
                        option2_2.setChecked(true);
                    }
                    if (option3.isChecked()){
                        option3_2.setChecked(true);
                    }
                    if (option4.isChecked()){
                        option4_2.setChecked(true);
                    }

                    //Highlighting the correct answer
                    if (option1_value.equals(word)){
                        option1_2.setTypeface(null, Typeface.BOLD);
                        option1_2.setTextColor(Color.parseColor("#009900"));
                    }else {
                        option1_2.setTypeface(null, Typeface.NORMAL);
                        option1_2.setTextColor(Color.parseColor("#000000"));
                    }
                    if (option2_value.equals(word)){
                        option2_2.setTypeface(null, Typeface.BOLD);
                        option2_2.setTextColor(Color.parseColor("#009900"));
                    }else {
                        option2_2.setTypeface(null, Typeface.NORMAL);
                        option2_2.setTextColor(Color.parseColor("#000000"));
                    }
                    if (option3_value.equals(word)){
                        option3_2.setTypeface(null, Typeface.BOLD);
                        option3_2.setTextColor(Color.parseColor("#009900"));
                    }else {
                        option3_2.setTypeface(null, Typeface.NORMAL);
                        option3_2.setTextColor(Color.parseColor("#000000"));
                    }
                    if (option4_value.equals(word)){
                        option4_2.setTypeface(null, Typeface.BOLD);
                        option4_2.setTextColor(Color.parseColor("#009900"));
                    }else {
                        option4_2.setTypeface(null, Typeface.NORMAL);
                        option4_2.setTextColor(Color.parseColor("#000000"));
                    }

                    System.out.println(selectedOption);

                    AnimationFactory.flipTransition(mViewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
                    mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_2)));
                }
            }
        });

        bAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In case of the last card go back to main screen when Next Card is pressed
                indexOfSequenceScoreIntArray +=2;
                if (indexOfSequenceScoreIntArray >= SequenceScoreIntArray.length){
                    Toast.makeText(MCQCard.this, "Congratulations!! You are all set for the day", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(MCQCard.this, HomeScreen.class);
                    startActivity(i);
                }else {
                    viewCard(SequenceScoreIntArray[indexOfSequenceScoreIntArray]);
                    fillOptions();
                    mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_1)));
                }

                myDB.updateScore_MCQCardAgain(SequenceScoreIntArray[indexOfSequenceScoreIntArray -2]); // updating score of previous card
                // incrementing studied new cards and review cards
                if (SequenceScoreIntArray[indexOfSequenceScoreIntArray -1] == 0){
                    myDB.increaseStudied_New_MCQCard_by1();
                } else{
                    myDB.increaseStudied_Review_MCQCard_by1();
                }
//                option1.setChecked(false);
//                option2.setChecked(false);
//                option3.setChecked(false);
//                option4.setChecked(false);
//                option1_2.setChecked(false);
//                option2_2.setChecked(false);
//                option3_2.setChecked(false);
//                option4_2.setChecked(false);

                rgOptions.clearCheck();
                rgOptions2.clearCheck();
            }
        });

        bGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In case of the last card go back to main screen when Next Card is pressed
                indexOfSequenceScoreIntArray +=2;
                if (indexOfSequenceScoreIntArray >= SequenceScoreIntArray.length){
                    Toast.makeText(MCQCard.this, "Congratulations!! You are all set for the day", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(MCQCard.this, HomeScreen.class);
                    startActivity(i);
                }else {
                    viewCard(SequenceScoreIntArray[indexOfSequenceScoreIntArray]);
                    fillOptions();
                    mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_1)));
                }
                myDB.updateScore_MCQCardGood(SequenceScoreIntArray[indexOfSequenceScoreIntArray -2]); // updating score of previous card
                // incrementing studied new cards and review cards
                if (SequenceScoreIntArray[indexOfSequenceScoreIntArray -1] == 0){
                    myDB.increaseStudied_New_MCQCard_by1();
                } else{
                    myDB.increaseStudied_Review_MCQCard_by1();
                }
//                option1.setChecked(false);
//                option2.setChecked(false);
//                option3.setChecked(false);
//                option4.setChecked(false);
//                option1_2.setChecked(false);
//                option2_2.setChecked(false);
//                option3_2.setChecked(false);
//                option4_2.setChecked(false);

                rgOptions.clearCheck();
                rgOptions2.clearCheck();
            }
        });

        bEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In case of the last card go back to main screen when Next Card is pressed
                indexOfSequenceScoreIntArray +=2;
                if (indexOfSequenceScoreIntArray >= SequenceScoreIntArray.length){
                    Toast.makeText(MCQCard.this, "Congratulations!! You are all set for the day", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(MCQCard.this, HomeScreen.class);
                    startActivity(i);
                }else {
                    viewCard(SequenceScoreIntArray[indexOfSequenceScoreIntArray]);
                    fillOptions();
                    mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_1)));
                }
                myDB.updateScore_MCQCardEasy(SequenceScoreIntArray[indexOfSequenceScoreIntArray -2]); // updating score of previous card
                // incrementing studied new cards and review cards
                if (SequenceScoreIntArray[indexOfSequenceScoreIntArray -1] == 0){
                    myDB.increaseStudied_New_MCQCard_by1();
                } else{
                    myDB.increaseStudied_Review_MCQCard_by1();
                }
//                option1.setChecked(false);
//                option2.setChecked(false);
//                option3.setChecked(false);
//                option4.setChecked(false);
//                option1_2.setChecked(false);
//                option2_2.setChecked(false);
//                option3_2.setChecked(false);
//                option4_2.setChecked(false);

                rgOptions.clearCheck();
                rgOptions2.clearCheck();
            }
        });

    }

    // create an action bar undo button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.undo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle action bar undo button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_undo) {
            // do something here
            if(indexOfSequenceScoreIntArray == 0){
                Toast.makeText(MCQCard.this, "Can't go back to previous card; this is the first card", Toast.LENGTH_LONG).show();
            } else{
                indexOfSequenceScoreIntArray -= 2;
                viewCard(SequenceScoreIntArray[indexOfSequenceScoreIntArray]);
                fillOptions();
                rgOptions.clearCheck();
                mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_1)));
                myDB.undoScore_MCQCard(SequenceScoreIntArray[indexOfSequenceScoreIntArray], SequenceScoreIntArray[indexOfSequenceScoreIntArray +1]); // undo score
                // undoing studied cards
                if (SequenceScoreIntArray[indexOfSequenceScoreIntArray +1] == 0){
                    myDB.decreaseStudied_New_MCQCard_by1();
                } else{
                    myDB.decreaseStudied_Review_MCQCard_by1();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewCard(int cardkey){
        Cursor res = myDB.getByCardKey(cardkey);
        if (res.getCount() == 0){
            return;
        }
        LinearLayout layout = (LinearLayout) findViewById(R.id.child_1_2);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.child_2_2);

        while (res.moveToNext()){
//            tvWord.setText(res.getString(4));
//            tvWord2.setText(res.getString(4));
            word = res.getString(4);

            System.out.println(word);
//            speakText2(word);
//            tvPOS.setText(res.getString(5));
//            tvPOS2.setText(res.getString(5));
            //option1.setText(res.getString(4));
            tvMeaning.setText(res.getString(6));
            tvMeaning2.setText(res.getString(6));
//            tvExample.setText(res.getString(7));
            layout.removeAllViews();
            if (!res.getString(8).equals("[]")){
                String[] values1 = picList(res.getString(8));

                for(int i=0; i<values1.length; i++) {
                    ImageView image = new ImageView(this);
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(400,400));
                    // Adds the view to the layout
                    Glide.with(MCQCard.this).load(values1[i]).fitCenter().into(image);

                    layout.addView(image);
                }
            }
            layout2.removeAllViews();
            if (!res.getString(8).equals("[]")){
                String[] values1 = picList(res.getString(8));

                for(int i=0; i<values1.length; i++) {
                    ImageView image = new ImageView(this);
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(400,400));
                    // Adds the view to the layout
                    Glide.with(MCQCard.this).load(values1[i]).fitCenter().into(image);

                    layout2.addView(image);
                }
            }


//            tvSynonym.setText(res.getString(9));
//            tvAntonym.setText(res.getString(10));
        }

        res.close();
    }

    public void fillOptions(){
        Cursor res = myDB.get3options_MCQCard(word);

        List<String> stringList = new ArrayList<String>();
        stringList.add(word);

        if(res.moveToNext()){
            stringList.add(res.getString(0));
        }
        if(res.moveToNext()){
            stringList.add(res.getString(0));
        }
        if(res.moveToNext()){
            stringList.add(res.getString(0));
        }

        Collections.shuffle(stringList);
        option1.setText(stringList.get(0));
        option2.setText(stringList.get(1));
        option3.setText(stringList.get(2));
        option4.setText(stringList.get(3));

        res.close();
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
