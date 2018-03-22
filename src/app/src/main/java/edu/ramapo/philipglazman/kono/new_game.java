package edu.ramapo.philipglazman.kono;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class new_game extends AppCompatActivity {

    private Random randomGenerator =  new Random();
    private char humanPlayerColor = 'x';
    private char computerPlayerColor = 'x';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

//        Intent intent = getIntent();
//
////        String message = intent.getStringExtra("startType");
////        TextView textView = new TextView(this);
////        textView.setTextSize(40);
////        textView.setText(message);
////        setContentView(textView);
        //randomGenerator = new Random(System.currentTimeMillis());
        //randomGenerator.setSeed(System.currentTimeMillis());
        choosePlayers();
    }

    /**
     * Submits settings for game and starts main activity.
     */
    public void readySubmit()
    {

    }

    /**
     * Simulates random dice throw by randomly generating number between 1 and 12.
     * @return Integer between 1 and 12.
     */
    private int randomDiceNumber()
    {
        int randomNumber = randomGenerator.nextInt(12)+1;
        return randomNumber;
    }

    /**
     * Randomly choose a player color for computer and human players.
     */
    private void randomPlayerColor()
    {
        int randomNumber = randomGenerator.nextInt(2);
        if(randomNumber>0)
        {
            computerPlayerColor = 'b';
            humanPlayerColor = 'w';
        }
        else
        {
            computerPlayerColor = 'w';
            humanPlayerColor = 'b';
        }
        announcePlayerColor();
    }

    private void announcePlayerColor()
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.diceRolls);
        TextView textView = new TextView(this);

        textView.setText("Human will play "+humanPlayerColor + ". Computer will play "+computerPlayerColor);
        linearLayout.addView(textView);
    }

    private void askHumanForColor()
    {

    }

    private void announceDiceRoll(int humanDiceRoll, int computerDiceRoll)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.diceRolls);
        TextView textView = new TextView(this);

        textView.setText("Human rolled "+humanDiceRoll + ". Computer rolled "+computerDiceRoll);
        linearLayout.addView(textView);
    }

    /**
     * Set colors for players.
     */
    private void choosePlayers()
    {
        int humanDiceRoll = randomDiceNumber();
        int computerDiceRoll = randomDiceNumber();


        announceDiceRoll(humanDiceRoll,computerDiceRoll);

        // Computer picks player color first.
        if(computerDiceRoll > humanDiceRoll)
        {
            randomPlayerColor();
        }
//
//        // Human picks player color first.
//        else if(humanDiceRoll > computerDiceRoll)
//        {
//            askHumanForColor();
//        }
//
//        // Try rolling again.
//        else
//        {
//            choosePlayers();
//        }

    }



}
