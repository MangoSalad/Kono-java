package edu.ramapo.philipglazman.kono;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class main_activity extends AppCompatActivity {

    private Board board = new Board(5);
    private ListAdapter boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(this));
        boardView = gridview.getAdapter();

        
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(main_activity.this, "" + position,
                        Toast.LENGTH_SHORT).show();

                ImageView imageView = (ImageView) v;
                //imageView.setImageResource(ImageAdapter.mThumbIds[position]);
                //imageButton.setTag(R.drawable.your_resource);
                imageView.setImageResource(R.drawable.ic_launcher_foreground);

            }
        });




        for(int i  =0; i <100; i++)
        {
            writeToMessageFeed("It is your turn.");
        }

    }

    public void writeToMessageFeed(String message)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.gameMessages);
        TextView textView = new TextView(this);
        textView.setText(message);
        linearLayout.addView(textView);
    }


}
