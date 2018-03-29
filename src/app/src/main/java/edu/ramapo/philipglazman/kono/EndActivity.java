package edu.ramapo.philipglazman.kono;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    private String winner;
    private int roundComputerScore;
    private int roundHumanScore;
    private int tournamentComputerScore;
    private int tournamentHumanScore;
    private int awardedPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        this.winner = getIntent().getStringExtra("ROUND_WINNER");
        this.roundComputerScore = Integer.parseInt(getIntent().getStringExtra("ROUND_COMPUTER_SCORE"));
        this.roundHumanScore = Integer.parseInt(getIntent().getStringExtra("ROUND_HUMAN_SCORE"));

        this.tournamentComputerScore = Integer.parseInt(getIntent().getStringExtra("TOURNAMENT_COMPUTER_SCORE"));
        this.tournamentHumanScore = Integer.parseInt(getIntent().getStringExtra("TOURNAMENT_HUMAN_SCORE"));

        this.awardedPoints = Integer.parseInt(getIntent().getStringExtra("AWARDED_POINTS"));

        this.displayRoundResult();
        this.displayTournamentResult();
    }

    @SuppressLint("SetTextI18n")
    private void displayRoundResult()
    {
        TextView roundResult = (TextView)findViewById(R.id.roundScores);

        if(winner.equals("draw"))
        {
            roundResult.setText("It is a draw!.\nComputer scored "+roundComputerScore+".\nHuman scored "+roundHumanScore+".");
        }
        roundResult.setText("The winner is "+winner+"!\nComputer scored "+roundComputerScore+".\nHuman scored "+roundHumanScore+".");
    }

    private void displayTournamentResult()
    {
        TextView roundResult = (TextView)findViewById(R.id.tournamentScores);

        if(!winner.equals("draw"))
        {
            roundResult.setText("Tournament Scores!\n"+awardedPoints+" points awarded to "+winner+".\nComputer scored "+tournamentComputerScore+".\nHuman scored "+tournamentHumanScore+".");
        }
        else
        {
            roundResult.setText("Tournament Scores!\nComputer scored "+tournamentComputerScore+".\nHuman scored "+tournamentHumanScore+".");
        }
    }

    public void playGame(View view)
    {
        Intent intent = new Intent(this, NewGameActivity.class);
        intent.putExtra("START_TYPE","new");
        intent.putExtra("TOURNAMENT_HUMAN_SCORE",Integer.toString(this.tournamentHumanScore));
        intent.putExtra("TOURNAMENT_COMPUTER_SCORE",Integer.toString(this.tournamentComputerScore));
        intent.putExtra("START_TYPE","new");
        if(!winner.equals("draw"))
        {
            intent.putExtra("FIRST_PLAYER",this.winner);
        }
        startActivity( intent );
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void quitGame(View view)
    {
        finishAffinity();
    }

}
