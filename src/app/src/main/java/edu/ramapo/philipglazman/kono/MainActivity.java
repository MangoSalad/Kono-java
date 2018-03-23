package edu.ramapo.philipglazman.kono;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    private final int OPEN_COLOR = Color.WHITE;
    private final int BLACK_COLOR = Color.BLACK;
    private final int WHITE_COLOR = Color.LTGRAY;

    private Board board;
    private Round round;
    private ListAdapter boardView;

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
                //columns.setText("B");

                GradientDrawable gd = new GradientDrawable();

                char piece = board.getPieceAtCoordinates(rowNum,columnNum);

                if(piece == 'w' || piece == 'W')
                {
                    gd.setColor(WHITE_COLOR);
                }
                else if(piece == 'b' || piece == 'B')
                {
                    gd.setColor(BLACK_COLOR);
                }
                if(piece == '+')
                {
                    gd.setColor(OPEN_COLOR);
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


    private View.OnClickListener makeMove = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(round.getCurrentTurn().equals("computer"))
            {
                writeToMessageFeed("Computer must play.");
            }
            else if (round.getCurrentTurn().equals("human"))
            {
                writeToMessageFeed(round.getCurrentTurn()+" clicked "+v.getTag().toString());
                int row = v.getTag().toString().charAt(0);
                int column = v.getTag().toString().charAt(1);
                //makeHumanMove();
            }

            round.setNextTurn();
        }

    };
}
