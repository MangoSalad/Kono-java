package edu.ramapo.philipglazman.kono;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int DEFAULT_COLOR = Color.parseColor("#FCEBB6");
    private final int BLACK_COLOR = Color.BLACK;
    private final int WHITE_COLOR = Color.WHITE;

    private Board board = new Board(5);
    private ListAdapter boardView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);



        createTable();
        
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
        for (int rowIndex = 0; rowIndex < board.getBoardLength(); ++rowIndex) {

            TableRow row = new TableRow(this);

            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(4, 4, 4, 4);

            // Create pieces.
            for (int columnIndex = 0; columnIndex < board.getBoardLength(); ++columnIndex) {


                TextView columns = new TextView(this);

                columns.setWidth(100);
                columns.setHeight(100);
                columns.setTextSize(20);


                GradientDrawable gd = new GradientDrawable();
                gd.setColor(WHITE_COLOR); // Changes this drawbale to use a single color instead of a gradient
                gd.setCornerRadius(5);
                gd.setStroke(2, BLACK_COLOR);

                columns.setBackground(gd);
                columns.setTag(rowIndex+""+columnIndex);

                columns.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        writeToMessageFeed(v.getTag().toString());
                    }
                });

                row.addView(columns, params);
            }
            table.addView(row);
        }
    }


}
