package edu.ramapo.philipglazman.kono;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridView;

public class main_activity extends AppCompatActivity {

    private Board board = new Board(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(this));
        //displayBoard();
    }

    // Programatically display board.
//    public void displayBoard()
//    {
//        //        GridView grid = new GridView(this);
//////        grid.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//////        grid.setBackgroundColor(Color.WHITE);
////        grid.setNumColumns(3);
//////        grid.setColumnWidth(GridView.AUTO_FIT);
//////        grid.setVerticalSpacing(5);
//////        grid.setHorizontalSpacing(5);
//////        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
////        setContentView(grid);
//
//    }
}
