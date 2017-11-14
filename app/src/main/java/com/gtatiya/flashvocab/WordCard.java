package com.gtatiya.flashvocab;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class WordCard extends AppCompatActivity implements TextToSpeech.OnInitListener{

    DatabaseHelper myDB;
    ViewFlipper mViewFlipper;
    TextView tvWord, tvWord2, tvPOS, tvPOS2, tvMeaning, tvExample, tvSynonym, tvAntonym;
    ImageView ivPic;
    Button bShowAns, bAgain, bGood, bEasy, bPlay;

    public static String word;

    HomeScreen homeScreen;

    int[] SequenceScoreIntArray;

    int indexOfSequenceScoreIntArray = 0;

    private TextToSpeech engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_card);

        myDB = new DatabaseHelper(this);
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);

        tvWord = (TextView) findViewById(R.id.tvWord);
        tvWord2 = (TextView) findViewById(R.id.tvWord2);
        tvPOS = (TextView) findViewById(R.id.tvPOS);
        tvPOS2 = (TextView) findViewById(R.id.tvPOS2);
        tvMeaning = (TextView) findViewById(R.id.tvMeaning);
        tvExample = (TextView) findViewById(R.id.tvExample);
        tvSynonym = (TextView) findViewById(R.id.tvSynonym);
        tvAntonym = (TextView) findViewById(R.id.tvAntonym);

        bShowAns = (Button) findViewById(R.id.bShowAns);
        bAgain = (Button) findViewById(R.id.bAgain);
        bGood = (Button) findViewById(R.id.bGood);
        bEasy = (Button) findViewById(R.id.bEasy);
        bPlay = (Button) findViewById(R.id.bPlay);

        engine = new TextToSpeech(this, this);

        SequenceScoreIntArray = homeScreen.new_review_CardsIntArray;
        viewCard(SequenceScoreIntArray[0]); // displaying the first card

        bShowAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationFactory.flipTransition(mViewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
                mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_2)));

            }
        });

        bAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In case of the last card go back to main screen when Next Card is pressed
                indexOfSequenceScoreIntArray +=2;
                if (indexOfSequenceScoreIntArray >= SequenceScoreIntArray.length){
                    Toast.makeText(WordCard.this, "Congratulations!! You are all set for the day", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(WordCard.this, HomeScreen.class);
                    startActivity(i);
                }else {
                    viewCard(SequenceScoreIntArray[indexOfSequenceScoreIntArray]);
                    AnimationFactory.flipTransition(mViewFlipper, AnimationFactory.FlipDirection.TOP_BOTTOM);
                    mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_1)));
                }

                myDB.updateScoreAgain(SequenceScoreIntArray[indexOfSequenceScoreIntArray -2]); // updating score of previous card
                // incrementing studied new cards and review cards
                if (SequenceScoreIntArray[indexOfSequenceScoreIntArray -1] == 0){
                    myDB.increaseStudied_Xby1();
                } else{
                    myDB.increaseStudied_Yby1();
                }

            }
        });

        bGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In case of the last card go back to main screen when Next Card is pressed
                indexOfSequenceScoreIntArray +=2;
                if (indexOfSequenceScoreIntArray >= SequenceScoreIntArray.length){
                    Toast.makeText(WordCard.this, "Congratulations!! You are all set for the day", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(WordCard.this, HomeScreen.class);
                    startActivity(i);
                }else {
                    viewCard(SequenceScoreIntArray[indexOfSequenceScoreIntArray]);
                    AnimationFactory.flipTransition(mViewFlipper, AnimationFactory.FlipDirection.TOP_BOTTOM);
                    mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_1)));
                }
                myDB.updateScoreGood(SequenceScoreIntArray[indexOfSequenceScoreIntArray -2]); // updating score of previous card
                // incrementing studied new cards and review cards
                if (SequenceScoreIntArray[indexOfSequenceScoreIntArray -1] == 0){
                    myDB.increaseStudied_Xby1();
                } else{
                    myDB.increaseStudied_Yby1();
                }
            }
        });

        bEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In case of the last card go back to main screen when Next Card is pressed
                indexOfSequenceScoreIntArray +=2;
                if (indexOfSequenceScoreIntArray >= SequenceScoreIntArray.length){
                    Toast.makeText(WordCard.this, "Congratulations!! You are all set for the day", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(WordCard.this, HomeScreen.class);
                    startActivity(i);
                }else {
                    viewCard(SequenceScoreIntArray[indexOfSequenceScoreIntArray]);
                    AnimationFactory.flipTransition(mViewFlipper, AnimationFactory.FlipDirection.TOP_BOTTOM);
                    mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_1)));
                }
                myDB.updateScoreEasy(SequenceScoreIntArray[indexOfSequenceScoreIntArray -2]); // updating score of previous card
                // incrementing studied new cards and review cards
                if (SequenceScoreIntArray[indexOfSequenceScoreIntArray -1] == 0){
                    myDB.increaseStudied_Xby1();
                } else{
                    myDB.increaseStudied_Yby1();
                }
            }
        });

        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText(word);
            }
        });

    }

    // https://stackoverflow.com/questions/38158953/how-to-create-button-in-action-bar-in-android
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
                Toast.makeText(WordCard.this, "Can't go back", Toast.LENGTH_LONG).show();
            } else{
                indexOfSequenceScoreIntArray -= 2;
                viewCard(SequenceScoreIntArray[indexOfSequenceScoreIntArray]);
                mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child_1)));
                myDB.undoScore(SequenceScoreIntArray[indexOfSequenceScoreIntArray], SequenceScoreIntArray[indexOfSequenceScoreIntArray +1]); // undo score
                // undoing studied cards
                if (SequenceScoreIntArray[indexOfSequenceScoreIntArray +1] == 0){
                    myDB.decreaseStudied_Xby1();
                } else{
                    myDB.decreaseStudied_Yby1();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            //Setting speech Language
            engine.setLanguage(Locale.ENGLISH);
            engine.setPitch(1);
            speakText(word); // added only for the first word to fix: speak failed: not bound to TTS engine
        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (engine != null) {
            engine.stop();
            engine.shutdown();
        }
        super.onDestroy();
    }


    public void speakText(String textContents) {
        engine.speak(textContents, TextToSpeech.QUEUE_ADD, null, null);
    }

    public void viewCard(int cardkey){
        Cursor res = myDB.getByCardKey(cardkey);
        if (res.getCount() == 0){
            return;
        }
        LinearLayout layout = (LinearLayout) findViewById(R.id.child_2_5);

        while (res.moveToNext()){
            tvWord.setText(res.getString(4));
            tvWord2.setText(res.getString(4));
            word = res.getString(4);

            System.out.println(word);

            speakText(word);

            tvPOS.setText(res.getString(5));
            tvPOS2.setText(res.getString(5));
            tvMeaning.setText(res.getString(6));
            tvExample.setText(res.getString(7));

            if (res.getString(8) != "[]"){
                String[] values1 = picList(res.getString(8));

                layout.removeAllViews();
                for(int i=0; i<values1.length; i++) {
                    ImageView image = new ImageView(this);
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(400,400));
                    // Adds the view to the layout
                    Glide.with(WordCard.this).load(values1[i]).into(image);

                    layout.addView(image);
                }
//                Glide.with(Main2Activity.this).load(values1[0]).into(ivPic);
            }

            tvSynonym.setText(res.getString(9));
            tvAntonym.setText(res.getString(10));
        }
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
