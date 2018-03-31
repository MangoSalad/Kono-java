package edu.ramapo.philipglazman.kono;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
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

    // Constants
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

        // If pre-existing board exists from load game.
        if(getIntent().hasExtra("BOARD"))
        {
            // Get the loaded board.
            char[][] loadedBoard = (char[][]) getIntent().getSerializableExtra("BOARD");
            board = new Board(loadedBoard);
        }
        // Create new board given board size.
        else
        {
            String boardSize = getIntent().getStringExtra("BOARD_SIZE");
            board = new Board(Integer.parseInt(boardSize));
        }

        // Get player colors and current turn.
        String humanPlayerColor = getIntent().getStringExtra("HUMAN_COLOR");
        String computerPlayerColor = getIntent().getStringExtra("COMPUTER_COLOR");
        String firstPlayer = getIntent().getStringExtra("FIRST_PLAYER");

        round = new Round(firstPlayer, humanPlayerColor,computerPlayerColor);
        human = new Human();
        computer = new Computer(round.getComputerPlayerColor());
        help = new Computer(round.getHumanPlayerColor());

        // If a round number exists, then load an existing tournament config.
        if(getIntent().hasExtra("ROUND_NUM"))
        {
            int roundNum = Integer.parseInt(getIntent().getStringExtra("ROUND_NUM"));
            int computerScore = Integer.parseInt(getIntent().getStringExtra("COMPUTER_SCORE"));
            int humanScore = Integer.parseInt(getIntent().getStringExtra("HUMAN_SCORE"));
            tournament = new Tournament(roundNum, computerScore,humanScore);
        }
        // Start new tournament.
        else
        {
            tournament = new Tournament();
        }
        // If a tournament score exists, set the scores accordingly.
        if(getIntent().hasExtra("TOURNAMENT_HUMAN_SCORE"))
        {
            int tournamentHumanScore = Integer.parseInt(getIntent().getStringExtra("TOURNAMENT_HUMAN_SCORE"));
            int tournamentComputerScore = Integer.parseInt(getIntent().getStringExtra("TOURNAMENT_COMPUTER_SCORE"));

            tournament.setComputerScore(tournamentComputerScore);
            tournament.setHumanScore(tournamentHumanScore);
        }

        // Create table and announce it.
        createTable();
        announceGameSettings();
        setComputerModeButton();
    }

    /**
     * Changes the computer button depending on turn. Turns to help mode for human turn.
     */
    private void setComputerModeButton()
    {
        // Play button when computer's turn.
        if(round.getCurrentTurn().equals("computer"))
        {
            writeToMessageFeed("It is computer's turn.");
            Button button = (Button)findViewById(R.id.computerMode);
            button.setText("Play");
        }
        // Help button when human's turn.
        else if(round.getCurrentTurn().equals("human"))
        {
            writeToMessageFeed("It is human's turn.");
            Button button = (Button)findViewById(R.id.computerMode);
            button.setText("Help Mode");
        }
    }

    /**
     * Announce to player the game settings.
     */
    public void announceGameSettings()
    {
        // Announce board length.
        writeToMessageFeed("Board is "+board.getBoardLength()+"x"+board.getBoardLength()+".");

        // Announce player colors.
        writeToMessageFeed("Computer will play as "+round.getComputerPlayerColor()+".");
        writeToMessageFeed("Human will play as "+round.getHumanPlayerColor()+".");
    }

    /**
     * Write to user a line of text.
     * @param message, message to write.
     */
    public void writeToMessageFeed(String message)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.gameMessages);
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(25);
        linearLayout.addView(textView,0);
    }

    /**
     * Generates the table in GUI.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createTable() {

        // TableLayout.
        TableLayout table = (TableLayout) findViewById(R.id.boardTable);

        // Create rows.
        for (int rowNum = 0; rowNum <= board.getBoardLength(); rowNum++) {

            // New row.
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(4, 4, 4, 4);


            // Create row labels (numbered rows).
            if(rowNum < board.getBoardLength())
            {
                TextView rowLabel = new TextView(this);
                rowLabel.setWidth(100);
                rowLabel.setHeight(100);
                rowLabel.setTextSize(20);
                rowLabel.setText(Integer.toString(rowNum+1));
                rowLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                row.addView(rowLabel);
            }

            // Create column labels (numbered columns).
            if(rowNum == board.getBoardLength())
            {
                for (int columnNum = 0; columnNum <= board.getBoardLength(); columnNum++) {
                    TextView columnLabel = new TextView(this);
                    columnLabel.setWidth(100);
                    columnLabel.setHeight(100);
                    columnLabel.setTextSize(20);
                    columnLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    if(columnNum == 0)
                    {
                        columnLabel.setText("");
                    }
                    else
                    {
                        columnLabel.setText(Integer.toString(columnNum));
                    }
                    row.addView(columnLabel);
                }
            }

            // Generate the board pieces.
            else {

                // Create pieces/columns.
                for (int columnNum = 0; columnNum < board.getBoardLength(); columnNum++) {

                    // New coordinate.
                    TextView columns = new TextView(this);

                    columns.setWidth(100);
                    columns.setHeight(100);
                    columns.setTextSize(20);
                    columns.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    GradientDrawable gd = new GradientDrawable();

                    // Use board model as reference.
                    char piece = board.getPieceAtCoordinates(rowNum, columnNum);

                    if (piece == 'w' || piece == 'W') {
                        gd.setColor(WHITE_COLOR);
                        if (piece == 'w') {
                            columns.setText("S");
                            columns.setTextColor(BLACK_COLOR);
                        }
                    } else if (piece == 'b' || piece == 'B') {
                        gd.setColor(BLACK_COLOR);
                        if (piece == 'b') {
                            columns.setText("S");
                            columns.setTextColor(WHITE_COLOR);
                        }
                    }
                    if (piece == '+') {
                        gd.setColor(OPEN_COLOR);
                        columns.setText("");
                    }


                    gd.setCornerRadius(5);
                    gd.setStroke(2, BLACK_COLOR);

                    columns.setBackground(gd);
                    columns.setTag(rowNum + "" + columnNum);

                    columns.setOnClickListener(makeMove);

                    row.addView(columns, params);
                }
            }
            table.addView(row);
        }

    }

    /**
     * Refreshes the board using the data model.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void refreshBoard()
    {
        // Get GUI board.
        TableLayout table = (TableLayout) findViewById(R.id.boardTable);

        // For every row..
        for (int rowNum = 0; rowNum < table.getChildCount()-1; rowNum++)
        {

            TableRow row = (TableRow) table.getChildAt(rowNum);

            // For every piece on that row...
            for (int columnNum = 1; columnNum < row.getChildCount(); columnNum++)
            {
                // Select piece.
                TextView column = (TextView) row.getChildAt(columnNum);

                GradientDrawable gd  = new GradientDrawable();

                // Get piece from data model.
                char piece = board.getPieceAtCoordinates(rowNum,columnNum-1);

                // Set the pieces on the GUI accordingly.
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

    /**
     * Check if computer can meet condition to quit. If only 2 pieces remain for computer, it will quit.
     */
    private void checkComputerQuit()
    {
        if(round.getComputerPlayerColor().equals("white"))
        {
            if(board.getNumOfWhitePieces()<=2)
            {
                writeToMessageFeed("Computer quit game.");
                this.computerQuit();
            }
        }
        else if(round.getComputerPlayerColor().equals("black"))
        {
            if(board.getNumOfBlackPieces()<=2)
            {
                writeToMessageFeed("Computer quit game.");
                this.computerQuit();
            }

        }
    }

    /**
     * Computer quits game.
     */
    private void computerQuit()
    {
        tournament.subtractComputerScore(5);
        this.showResults();
    }

    /**
     * On click handler for computer mode (help/play).
     * @param v
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void computerMode(View v)
    {
        // If it is computer's turn.
        if(round.getCurrentTurn().equals("computer"))
        {
            // Check if computer should quit.
            this.checkComputerQuit();

            // Make computer play.
            computer.play(board.getBoard());
            Pair<Integer,Integer> initialCoordinates = computer.getInitialCoordinates();
            Pair<Integer,Integer> finalCoordinates = computer.getFinalCoordinates();
            String direction = computer.getDirection();
            String strategy = computer.getStrategy();

            // Announce decision and update board.
            displayComputerStrategy(initialCoordinates.first+1,initialCoordinates.second+1,finalCoordinates.first+1,finalCoordinates.second+1,direction,strategy);

            board.updateBoard(initialCoordinates.first, initialCoordinates.second, finalCoordinates.first, finalCoordinates.second);

            // next turn.
            this.startNextRound();
        }
        // If human's turn.
        else if(round.getCurrentTurn().equals("human"))
        {
            // Have computer make decision on which piece to play.
            help.play(board.getBoard());
            Pair<Integer,Integer> initialCoordinates = help.getInitialCoordinates();
            Pair<Integer,Integer> finalCoordinates = help.getFinalCoordinates();
            String direction = help.getDirection();
            String strategy = help.getStrategy();

            // Announce help strategy.
            displayHelpStrategy(initialCoordinates.first+1,initialCoordinates.second+1,finalCoordinates.first+1,finalCoordinates.second+1,direction,strategy);
        }

    }

    /**
     * Announce help strategy to user.
     * @param initialRow
     * @param initialColumn
     * @param finalRow
     * @param finalColumn
     * @param direction
     * @param strategy
     */
    private void displayHelpStrategy(int initialRow, int initialColumn, int finalRow, int finalColumn, String direction, String strategy)
    {
        if(strategy.equals("forward"))
        {
            writeToMessageFeed("It is recommend to move the piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".\nThis will advance the piece to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("block"))
        {
            writeToMessageFeed("It is recommend to move the piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".\nThis will block the computer piece by moving to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("retreat"))
        {
            writeToMessageFeed("It is recommend to move the piece at("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".\nThis will retreat the piece by moving to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("capture"))
        {
            writeToMessageFeed("It is recommend to move the piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".\nThis capture the computer piece at ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
    }

    /**
     * Announces computer strategy to user.
     * @param initialRow
     * @param initialColumn
     * @param finalRow
     * @param finalColumn
     * @param direction
     * @param strategy
     */
    private void displayComputerStrategy(int initialRow, int initialColumn, int finalRow, int finalColumn, String direction, String strategy)
    {
        if(strategy.equals("forward"))
        {
            writeToMessageFeed("Computer is moving piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".\nThis will advance its piece to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("block"))
        {
            writeToMessageFeed("Computer is moving piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".\nThis will block the human piece by moving to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("retreat"))
        {
            writeToMessageFeed("Computer is moving piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".\nThis will retreat its piece by moving to ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
        else if(strategy.equals("capture"))
        {
            writeToMessageFeed("Computer is moving piece at ("+Integer.toString(initialRow)+","+Integer.toString(initialColumn)+") "+direction+".\nThis capture the human piece at ("+Integer.toString(finalRow)+","+Integer.toString(finalColumn)+").");
        }
    }

    /**
     * On click handler for when clicking a piece on the board.
     */
    private View.OnClickListener makeMove = new View.OnClickListener() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            // If the player's turn is human.
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

    /**
     * Highlights a piece  - used for when user is selecting a piece.
     * @param v
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void highlightSelectedPiece(View v)
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(SELECTED_COLOR);
        gd.setCornerRadius(5);
        gd.setStroke(2, BLACK_COLOR);
        v.setBackground(gd);
    }


    /**
     * Starts next turn - and checks if a winner exists.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void startNextRound()
    {
        this.refreshBoard();
        round.setNextTurn();
        this.setComputerModeButton();

        // Check if winner.
        if(round.isWon(board.getBlackSide(),board.getWhiteSide(),board.getNumOfBlackPieces(),board.getNumOfWhitePieces()))
        {
            // IF winner, calculate score.
            round.calculateScore(board.getBoard(),board.getNumOfWhitePieces(),board.getNumOfBlackPieces());
            int awardedPoints = Math.abs(round.getHumanScore()-round.getComputerScore());

            tournament.setAwardedPoints(awardedPoints);

            if(round.getWinner().equals("human"))
            {
                tournament.addHumanScore(awardedPoints);
            }
            else if(round.getWinner().equals("computer"))
            {
                tournament.addComputerScore(awardedPoints);
            }
            // Move onto next activity.
            this.showResults();
        }
    }

    /**
     * Quit game for human.
     * @param v
     */
    public void quitGame(View v)
    {
        tournament.subtractHumanScore(5);
        this.showResults();
    }

    /**
     * Saves game.
     * @param v
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveGame(View v)
    {
        config = new GameConfiguration(tournament.getRoundNum(),tournament.getComputerScore(),
                round.getComputerPlayerColor(),tournament.getHumanScore(),round.getHumanPlayerColor(),
                board.getBoard(),round.getCurrentTurn());

        String fileName = "saveGame.txt";
        config.saveGame(fileName);
        finishAffinity();
    }

    /**
     * Moves onto next activity showing results of the round.
     */
    public void showResults()
    {
        Intent intent = new Intent(this, EndActivity.class);

        // Put Extras for Round Scores
        intent.putExtra("ROUND_WINNER",round.getWinner());
        intent.putExtra("ROUND_COMPUTER_SCORE",Integer.toString(round.getComputerScore()));
        intent.putExtra("ROUND_HUMAN_SCORE",Integer.toString(round.getHumanScore()));

        // Put Extras for Tournament Scores
        intent.putExtra("AWARDED_POINTS", Integer.toString(tournament.getAwardedPoints()));
        intent.putExtra("TOURNAMENT_COMPUTER_SCORE",Integer.toString(tournament.getComputerScore()));
        intent.putExtra("TOURNAMENT_HUMAN_SCORE",Integer.toString(tournament.getHumanScore()));

        startActivity(intent);
    }
}
