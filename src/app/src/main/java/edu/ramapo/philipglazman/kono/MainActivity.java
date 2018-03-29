package edu.ramapo.philipglazman.kono;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private final int OPEN_COLOR = Color.WHITE;
    private final int BLACK_COLOR = Color.BLACK;
    private final int WHITE_COLOR = Color.LTGRAY;
    private final int SELECTED_COLOR = Color.YELLOW;

    private Board board;
    private Round round;
    private Human human;
    private Computer computer;
    private Computer help;
    private Tournament tournament;
    private GameConfiguration config;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);


        // Prepare board.
        // If pre-existing board exists from load game.
        if(getIntent().hasExtra("BOARD"))
        {
            char[][] loadedBoard = (char[][]) getIntent().getSerializableExtra("BOARD");
            for(int i=0; i<loadedBoard.length;i++)
            {
                for(int j=0; j<loadedBoard.length;j++)
                {
                    Log.d("MAIN",Character.toString(loadedBoard[i][j]));
                }
            }
            board = new Board(loadedBoard);
        }
        else
        {
            String boardSize = getIntent().getStringExtra("BOARD_SIZE");
            board = new Board(Integer.parseInt(boardSize));
        }


        String humanPlayerColor = getIntent().getStringExtra("HUMAN_COLOR");
        String computerPlayerColor = getIntent().getStringExtra("COMPUTER_COLOR");
        String firstPlayer = getIntent().getStringExtra("FIRST_PLAYER");

        round = new Round(firstPlayer, humanPlayerColor,computerPlayerColor);

        human = new Human();
        computer = new Computer(round.getComputerPlayerColor());
        help = new Computer(round.getHumanPlayerColor());

        if(getIntent().hasExtra("ROUND_NUM"))
        {
            int roundNum = Integer.parseInt(getIntent().getStringExtra("ROUND_NUM"));
            int computerScore = Integer.parseInt(getIntent().getStringExtra("COMPUTER_SCORE"));
            int humanScore = Integer.parseInt(getIntent().getStringExtra("HUMAN_SCORE"));
            tournament = new Tournament(roundNum, computerScore,humanScore);
        }
        else
        {
            tournament = new Tournament();
        }

        setComputerModeButton();
        createTable();
        announceGameSettings();

    }

    private void setComputerModeButton()
    {
        if(round.getCurrentTurn().equals("computer"))
        {
            writeToMessageFeed("It is computer's turn.");
            Button button = (Button)findViewById(R.id.computerMode);
            button.setText("Play");
        }
        else if(round.getCurrentTurn().equals("human"))
        {
            writeToMessageFeed("It is human's turn.");
            Button button = (Button)findViewById(R.id.computerMode);
            button.setText("Help Mode");
        }

    }
    public void announceGameSettings()
    {
        // Announce board length.
        writeToMessageFeed("Board is "+board.getBoardLength()+"x"+board.getBoardLength()+".");

        // Announce player colors.
        writeToMessageFeed("Computer will play as "+round.getComputerPlayerColor()+".");
        writeToMessageFeed("Human will play as "+round.getHumanPlayerColor()+".");
    }

    public void writeToMessageFeed(String message)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.gameMessages);
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(25);
        linearLayout.addView(textView,0);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createTable()
    {
        // TableLayout.
        TableLayout table = (TableLayout) findViewById(R.id.boardTable);

        // Create rows.
        for (int rowNum = 0; rowNum < board.getBoardLength(); rowNum++)
        {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(4, 4, 4, 4);

            // Create pieces.
            for (int columnNum = 0; columnNum < board.getBoardLength(); columnNum++)
            {
                TextView columns = new TextView(this);

                columns.setWidth(100);
                columns.setHeight(100);
                columns.setTextSize(20);

                GradientDrawable gd = new GradientDrawable();

                char piece = board.getPieceAtCoordinates(rowNum,columnNum);

                if(piece == 'w' || piece == 'W')
                {
                    gd.setColor(WHITE_COLOR);
                    if(piece=='w')
                    {
                        columns.setText("S");
                        columns.setTextColor(BLACK_COLOR);
                    }
                }
                else if(piece == 'b' || piece == 'B')
                {
                    gd.setColor(BLACK_COLOR);
                    if(piece=='b')
                    {
                        columns.setText("S");
                        columns.setTextColor(WHITE_COLOR);
                    }
                }
                if(piece == '+')
                {
                    gd.setColor(OPEN_COLOR);
                    columns.setText("");
                }


                gd.setCornerRadius(5);
                gd.setStroke(2, BLACK_COLOR);

                columns.setBackground(gd);
                columns.setTag(rowNum+""+columnNum);

                columns.setOnClickListener(makeMove);

                row.addView(columns, params);
            }

            table.addView(row);
        }
    }

    // Inefficient but is easier from a programmer perspective. Respectful to MVC model and is less error-prone from passing paramaters.
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void refreshBoard()
    {
        TableLayout table = (TableLayout) findViewById(R.id.boardTable);
        //int numberOfRows = table.getChildCount();

        for (int rowNum = 0; rowNum < table.getChildCount(); rowNum++)
        {

            TableRow row = (TableRow) table.getChildAt(rowNum);

            for (int columnNum = 0; columnNum < row.getChildCount(); columnNum++)
            {
                TextView column = (TextView) row.getChildAt(columnNum);

                GradientDrawable gd  = new GradientDrawable();

                char piece = board.getPieceAtCoordinates(rowNum,columnNum);

                if(piece == 'w' || piece == 'W')
                {
                    gd.setColor(WHITE_COLOR);
                    if(piece=='w')
                    {
                        column.setText("S");
                        column.setTextColor(BLACK_COLOR);
                    }
                    else
                    {
                        column.setText("");
                    }
                }
                else if(piece == 'b' || piece == 'B')
                {
                    gd.setColor(BLACK_COLOR);
                    if(piece=='b')
                    {
                        column.setText("S");
                        column.setTextColor(WHITE_COLOR);
                    }
                    else
                    {
                        column.setText("");
                    }
                }
                if(piece == '+')
                {
                    gd.setColor(OPEN_COLOR);
                    column.setText("");
                }

                gd.setCornerRadius(5);
                gd.setStroke(2, BLACK_COLOR);

                column.setBackground(gd);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void computerMode(View v)
    {
        if(round.getCurrentTurn().equals("computer"))
        {
            computer.play(board.getBoard());
            Pair<Integer,Integer> initialCoordinates = computer.getInitialCoordinates();
            Pair<Integer,Integer> finalCoordinates = computer.getFinalCoordinates();
            String direction = computer.getDirection();
            String strategy = computer.getStrategy();

            displayComputerStrategy(initialCoordinates.first,initialCoordinates.second,finalCoordinates.first,finalCoordinates.second,direction,strategy);

            board.updateBoard(initialCoordinates.first, initialCoordinates.second, finalCoordinates.first, finalCoordinates.second);

            this.startNextRound();
        }
        else if(round.getCurrentTurn().equals("human"))
        {
            help.play(board.getBoard());
            Pair<Integer,Integer> initialCoordinates = help.getInitialCoordinates();
            Pair<Integer,Integer> finalCoordinates = help.getFinalCoordinates();
            String direction = help.getDirection();
            String strategy = help.getStrategy();

            displayHelpStrategy(initialCoordinates.first,initialCoordinates.second,finalCoordinates.first,finalCoordinates.second,direction,strategy);
        }

    }

    private void displayHelpStrategy(int initialRow, int initialColumn, int finalRow, int finalColumn, String direction, String strategy)
    {
        if(strategy.equals("forward"))
        {
            writeToMessageFeed("It is recommend to move the piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".");
            writeToMessageFeed("This will advance the piece to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("block"))
        {
            writeToMessageFeed("It is recommend to move the piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".");
            writeToMessageFeed("This will block the computer piece by moving to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("retreat"))
        {
            writeToMessageFeed("It is recommend to move the piece at("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".");
            writeToMessageFeed("This will retreat the piece by moving to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("capture"))
        {
            writeToMessageFeed("It is recommend to move the piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".");
            writeToMessageFeed("This capture the computer piece at ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
    }

    private void displayComputerStrategy(int initialRow, int initialColumn, int finalRow, int finalColumn, String direction, String strategy)
    {
        if(strategy.equals("forward"))
        {
            writeToMessageFeed("Computer is moving piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".");
            writeToMessageFeed("This will advance its piece to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("block"))
        {
            writeToMessageFeed("Computer is moving piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".");
            writeToMessageFeed("This will block the human piece by moving to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("retreat"))
        {
            writeToMessageFeed("Computer is moving piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".");
            writeToMessageFeed("This will retreat its piece by moving to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("capture"))
        {
            writeToMessageFeed("Computer is moving piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".");
            writeToMessageFeed("This capture the human piece at ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
    }


    private View.OnClickListener makeMove = new View.OnClickListener() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            if (round.getCurrentTurn().equals("human"))
            {
                int row = Character.getNumericValue(v.getTag().toString().charAt(0));
                int column = Character.getNumericValue(v.getTag().toString().charAt(1));

                // If an initial piece was selected.
                if(human.isInitialPieceSelected())
                {
                    if(human.getInitialRow()==row && human.getInitialColumn()==column)
                    {
                        human.clear();
                        refreshBoard();
                    }
                    // Check if the place to move to is valid.
                    else if(board.isValidLocationToMove(row,column,board.isSuperPiece(human.getInitialRow(),human.getInitialColumn())))
                    {
                        if(board.isValidMove(human.getInitialRow(),human.getInitialColumn(),row,column))
                        {
                            board.updateBoard(human.getInitialRow(),human.getInitialColumn(),row,column);

                            startNextRound();
                            human.clear();
                        }

                    }
                }
                // Select an initial piece.
                else
                {
                    // If piece selected is valid.
                    if(board.isValidPieceToMove(round.getHumanPlayerColorAsChar(),row,column))
                    {
                        highlightSelectedPiece(v);
                        human.setInitialPosition(row,column);
                    }
                }
            }


        }

    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void highlightSelectedPiece(View v)
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(SELECTED_COLOR);
        gd.setCornerRadius(5);
        gd.setStroke(2, BLACK_COLOR);
        v.setBackground(gd);
    }

    // Starts next round.
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void startNextRound()
    {
        this.refreshBoard();
        round.setNextTurn();
        this.setComputerModeButton();

        // Check if winner.
        if(round.isWon(board.getBlackSide(),board.getWhiteSide()))
        {
            round.calculateScore(board.getBoard(),board.getNumOfWhitePieces(),board.getNumOfBlackPieces());
            tournament.setHumanScore(round.getHumanScore());
            tournament.setComputerScore(round.getComputerScore());
        }
    }

    public void quitGame(View v)
    {
        tournament.subtractHumanScore(5);
        this.showResults();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveGame(View v)
    {
        config = new GameConfiguration(tournament.getRoundNum(),tournament.getComputerScore(),
                round.getComputerPlayerColor(),tournament.getHumanScore(),round.getHumanPlayerColor(),
                board.getBoard(),round.getCurrentTurn());

        String fileName = "case2.txt";
        config.saveGame(fileName);
    }

    public void showResults()
    {
        Intent intent = new Intent(this, EndActivity.class);

        // Put Extras for Round Scores
        intent.putExtra("ROUND_WINNER",round.getWinner());
        intent.putExtra("ROUND_COMPUTER_SCORE",Integer.toString(round.getComputerScore()));
        intent.putExtra("ROUND_HUMAN_SCORE",Integer.toString(round.getHumanScore()));

        // Put Extras for Tournament Scores
        intent.putExtra("TOURNAMENT_COMPUTER_SCORE",Integer.toString(tournament.getComputerScore()));
        intent.putExtra("TOURNAMENT_HUMAN_SCORE",Integer.toString(tournament.getHumanScore()));

        startActivity(intent);
    }
}
