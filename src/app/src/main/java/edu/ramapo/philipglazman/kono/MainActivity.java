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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);


        String boardSize = getIntent().getStringExtra("BOARD_SIZE");
        board = new Board(Integer.parseInt(boardSize));

        String humanPlayerColor = getIntent().getStringExtra("HUMAN_PLAYER_COLOR");
        String computerPlayerColor = getIntent().getStringExtra("COMPUTER_PLAYER_COLOR");
        String firstPlayer = getIntent().getStringExtra("FIRST_PLAYER");

        round = new Round(firstPlayer, humanPlayerColor,computerPlayerColor);

        human = new Human();
        computer = new Computer(round.getComputerPlayerColor());

        setComputerModeButton();
        createTable();
        announceGameSettings();

    }

    private void setComputerModeButton()
    {
        if(round.getCurrentTurn().equals("computer"))
        {
            Button button = (Button)findViewById(R.id.computerMode);
            button.setText("Play");
        }
        else if(round.getCurrentTurn().equals("human"))
        {
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

        // Announce current turn.
        writeToMessageFeed("It is "+round.getCurrentTurn()+"'s turn.");
    }

    public void writeToMessageFeed(String message)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.gameMessages);
        TextView textView = new TextView(this);
        textView.setText(message);
        linearLayout.addView(textView);
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
            writeToMessageFeed("Computer must play.");
            computer.play(board.getBoard());
            Pair<Integer,Integer> initialCoordinates = computer.getInitialCoordinates();
            Pair<Integer,Integer> finalCoordinates = computer.getFinalCoordinates();
            String direction = computer.getDirection();
            String strategy = computer.getStrategy();

            writeToMessageFeed("Computer moving ("+initialCoordinates.first+","+initialCoordinates.second+") to ("+ finalCoordinates.first+","+finalCoordinates.second+") "+direction+" "+strategy);
            board.updateBoard(initialCoordinates.first, initialCoordinates.second, finalCoordinates.first, finalCoordinates.second);

            refreshBoard();

            round.setNextTurn();
            setComputerModeButton();
        }
        else
        {
            writeToMessageFeed("Help mode");
            setComputerModeButton();
        }

    }


    private View.OnClickListener makeMove = new View.OnClickListener() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            if (round.getCurrentTurn().equals("human"))
            {
                writeToMessageFeed(round.getCurrentTurn()+" clicked "+v.getTag().toString());
                int row = Character.getNumericValue(v.getTag().toString().charAt(0));
                int column = Character.getNumericValue(v.getTag().toString().charAt(1));

                // Identify row and column for debugging.
                writeToMessageFeed(Integer.toString(row));
                writeToMessageFeed(Integer.toString(column));
                writeToMessageFeed(Boolean.toString(board.isValidPieceToMove(round.getHumanPlayerColorAsChar(),row,column)));

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
                            writeToMessageFeed("Moving from ("+human.getInitialRow()+","+human.getInitialColumn()+").");
                            board.updateBoard(human.getInitialRow(),human.getInitialColumn(),row,column);
                            refreshBoard();

                            writeToMessageFeed("Valid location to move to.");
                            round.setNextTurn();

                            human.clear();
                            setComputerModeButton();
                        }

                    }
                }
                // Select an initial piece.
                else
                {
                    // If piece selected is valid.
                    if(board.isValidPieceToMove(round.getHumanPlayerColorAsChar(),row,column))
                    {
                        writeToMessageFeed("Valid piece to move");
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
}
