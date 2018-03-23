package edu.ramapo.philipglazman.kono;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

public class new_game extends AppCompatActivity {

    private Random randomGenerator =  new Random();
    private char humanPlayerColor = 'x';
    private char computerPlayerColor = 'x';
    private String firstPlayer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        choosePlayers();
    }

    /**
     * Submits settings for game and starts main activity.
     */
    public void readySubmit(View view)
    {
        // Get selected radio button for human player color.
        if(firstPlayer.equals("human"))
        {
            RadioGroup choosePlayerColor = (RadioGroup) findViewById(R.id.choosePlayerColor_rg);
            RadioButton playerColorChosen = (RadioButton) findViewById(choosePlayerColor.getCheckedRadioButtonId());

            String humanPlayer = playerColorChosen.getText().toString();

            if(humanPlayer.equals("Black"))
            {
                humanPlayerColor='b';
                computerPlayerColor='w';
            }
            else if(humanPlayer.equals("White"))
            {
                humanPlayerColor='w';
                computerPlayerColor='b';
            }
        }

        // Get selected radio button for board size.
        RadioGroup chooseBoardSize = (RadioGroup) findViewById(R.id.chooseBoardSize_rg);
        RadioButton boardSizeButton = (RadioButton) findViewById(chooseBoardSize.getCheckedRadioButtonId());

        String boardSize = boardSizeButton.getText().toString();


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("boardSize",boardSize);
        intent.putExtra("humanPlayerColor",humanPlayerColor);

        startActivity( intent );

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

        firstPlayer = "computer";

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
        View linearLayout = findViewById(R.id.choosePlayerColor);
        linearLayout.setVisibility(View.VISIBLE);
        firstPlayer = "human";
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

        // Human picks player color first.
        else if(humanDiceRoll > computerDiceRoll)
        {
            askHumanForColor();
        }

        // Try rolling again.
        else
        {
            choosePlayers();
        }

    }



}
