package edu.ramapo.philipglazman.kono;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class start_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity);
    }

    /**
     * Called when user taps the start button.
     * @param view
     */
    public void startNewGame(View view)
    {
//        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.feed);
//        linearLayout.addView(textView);

        Intent intent = new Intent(this, new_game.class);
        intent.putExtra("startType", "new");
        startActivity( intent );
    }

    public void startLoadGame(View view)
    {
        Intent intent = new Intent(this, new_game.class);
        intent.putExtra("startType", "load");
        startActivity( intent );
    }
}
