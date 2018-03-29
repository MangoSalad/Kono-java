package edu.ramapo.philipglazman.kono;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

public class NewGameActivity extends AppCompatActivity {

    private final String ROUND_NUM = "ROUND_NUM";
    private final String BOARD = "BOARD";
    private final String COMPUTER_SCORE = "COMPUTER_SCORE";
    private final String COMPUTER_COLOR = "COMPUTER_COLOR";
    private final String HUMAN_SCORE = "HUMAN_SCORE";
    private final String HUMAN_COLOR = "HUMAN_COLOR";
    private final String FIRST_PLAYER = "FIRST_PLAYER";
    private final String BOARD_SIZE = "BOARD_SIZE";


    private int tournamentHumanScore;
    private int tournamentComputerScore;

    private final String BLACK = "black";
    private final String WHITE = "white";
    private final String COMPUTER_PLAYER = "computer";
    private final String HUMAN_PLAYER = "human";


    private String humanPlayerColor = "";
    private String computerPlayerColor = "";
    private String firstPlayer = "";

    private Random randomGenerator =  new Random();

    private String startType;
    private GameConfiguration config;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);


        startType = new String(getIntent().getStringExtra("START_TYPE"));
        config = new GameConfiguration(startType);

        if(this.startType.equals("load"))
        {
            this.askFileName();
        }
        else
        {
            if(getIntent().hasExtra("TOURNAMENT_HUMAN_SCORE"))
            {
                tournamentHumanScore = Integer.parseInt(getIntent().getStringExtra("TOURNAMENT_HUMAN_SCORE"));
                tournamentComputerScore = Integer.parseInt(getIntent().getStringExtra("TOURNAMENT_COMPUTER_SCORE"));

                if(getIntent().hasExtra("FIRST_PLAYER"))
                {
                    this.firstPlayer = getIntent().getStringExtra("FIRST_PLAYER");
                    String firstPlayer = getIntent().getStringExtra("FIRST_PLAYER");
                    if(firstPlayer.equals("human"))
                    {
                        askHumanForColor();
                        View linearLayout = findViewById(R.id.chooseBoardSize);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    choosePlayers();
                    View linearLayout = findViewById(R.id.chooseBoardSize);
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }
            else
            {
                choosePlayers();
                View linearLayout = findViewById(R.id.chooseBoardSize);
                linearLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public void askFileName()
    {
        TextView fileLabel = findViewById(R.id.fileLabel);
        fileLabel.setVisibility(View.VISIBLE);

        View fileText = findViewById(R.id.fileName);
        fileText.setVisibility(View.VISIBLE);
    }


    private void passLoadGameToMain()
    {
        Intent intent = new Intent(this, MainActivity.class);

        // Pass Round Number.
        intent.putExtra(ROUND_NUM,Integer.toString(config.getRoundNum()));

        // Pass Computer Score.
        intent.putExtra(COMPUTER_SCORE, Integer.toString(config.getComputerScore()));

        // Pass Computer Color.
        intent.putExtra(COMPUTER_COLOR, config.getComputerColor());

        // Pass Human Score.
        intent.putExtra(HUMAN_SCORE,Integer.toString(config.getHumanScore()));

        // Pass Human Color.
        intent.putExtra(HUMAN_COLOR,config.getHumanColor());

        // Pass Board.
        intent.putExtra(BOARD,config.getBoard());

        // Pass Next Player.
        intent.putExtra(FIRST_PLAYER,config.getNextPlayer());

        startActivity(intent);
    }

    private void passNewToMain()
    {
        // Get selected radio button for human player color.
        if (firstPlayer.equals(HUMAN_PLAYER)) {
            RadioGroup choosePlayerColor = (RadioGroup) findViewById(R.id.choosePlayerColor_rg);
            RadioButton playerColorChosen = (RadioButton) findViewById(choosePlayerColor.getCheckedRadioButtonId());

            String humanPlayer = playerColorChosen.getText().toString();

            if (humanPlayer.equals(BLACK)) {
                humanPlayerColor = BLACK;
                computerPlayerColor = WHITE;
            } else if (humanPlayer.equals(WHITE)) {
                humanPlayerColor = WHITE;
                computerPlayerColor = BLACK;
            }
        }
        // Get selected radio button for board size.
        RadioGroup chooseBoardSize = (RadioGroup) findViewById(R.id.chooseBoardSize_rg);
        RadioButton boardSizeButton = (RadioButton) findViewById(chooseBoardSize.getCheckedRadioButtonId());

        String boardSize = boardSizeButton.getText().toString();


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(BOARD_SIZE, boardSize.toString());
        intent.putExtra(HUMAN_COLOR, humanPlayerColor);
        intent.putExtra(COMPUTER_COLOR, computerPlayerColor);
        intent.putExtra(FIRST_PLAYER, firstPlayer);

        if(getIntent().hasExtra("TOURNAMENT_HUMAN_SCORE"))
        {
            intent.putExtra("TOURNAMENT_HUMAN_SCORE",Integer.toString(tournamentHumanScore));
            intent.putExtra("TOURNAMENT_COMPUTER_SCORE",Integer.toString(tournamentComputerScore));
        }

        startActivity(intent);
    }

    /**
     * Submits settings for game and starts main activity.
     */
    public void readySubmit(View view)
    {
        if(startType.equals("load"))
        {
            // check if file can open.
            EditText fileText = findViewById(R.id.fileName);
            String fileName = fileText.getText().toString();

            // If file is valid, load it.
            if(config.isValidFile(fileName))
            {
                config.loadGame(fileName);
                this.passLoadGameToMain();
            }
            // Else notify user that they need to input a new file name.
            else
            {
                TextInputLayout fileNameLayout = (TextInputLayout) findViewById(R.id.fileNameLayout);
                fileNameLayout.setError("Invalid file.");
            }
        }
        else if(startType.equals("new"))
        {
            this.passNewToMain();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void announcePlayerColor()
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.diceRolls);
        TextView textView = new TextView(this);
        textView.setTextAlignment(textView.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(25);
        textView.setText("Human will play "+humanPlayerColor + ". Computer will play "+computerPlayerColor+".");
        linearLayout.addView(textView);
    }

    private void askHumanForColor()
    {
        View linearLayout = findViewById(R.id.choosePlayerColor);
        linearLayout.setVisibility(View.VISIBLE);
        firstPlayer = HUMAN_PLAYER;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void announceDiceRoll(int humanDiceRoll, int computerDiceRoll)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.diceRolls);
        TextView textView = new TextView(this);
        textView.setTextAlignment(textView.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(25);
        textView.setText("Human rolled "+humanDiceRoll + ". Computer rolled "+computerDiceRoll+".");
        linearLayout.addView(textView);
    }

    /**
     * Set colors for players.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void choosePlayers()
    {
        int humanDiceRoll = config.randomDiceNumber();
        int computerDiceRoll = config.randomDiceNumber();


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

    /**
     * Randomly choose a player color for computer and human players.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void randomPlayerColor()
    {
        int randomNumber = randomGenerator.nextInt(2);

        firstPlayer = COMPUTER_PLAYER;

        if(randomNumber>0)
        {
            computerPlayerColor = BLACK;
            humanPlayerColor = WHITE;
        }
        else
        {
            computerPlayerColor = WHITE;
            humanPlayerColor = BLACK;
        }
        announcePlayerColor();
    }




}
