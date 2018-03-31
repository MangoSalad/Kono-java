package edu.ramapo.philipglazman.kono;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity);
    }

    /**
     * Starts activity for new game.
     * @param view
     */
    public void startNewGame(View view)
    {
        Intent intent = new Intent(this, NewGameActivity.class);

        intent.putExtra("START_TYPE","new");
        startActivity( intent );
    }

    /**
     * Starts activity for loading game.
     * @param view
     */
    public void startLoadGame(View view)
    {
        Intent intent = new Intent(this, NewGameActivity.class);

        intent.putExtra("START_TYPE","load");
        startActivity( intent );
    }
}
