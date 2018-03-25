package edu.ramapo.philipglazman.kono;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by mango on 3/25/18.
 */

public class GameConfiguration {

    private String startType;
    private Random randomGenerator =  new Random();

    public GameConfiguration(String startType)
    {
        this.startType = new String(startType);

        if(startType.equals("load"))
        {
            loadGame();
        }
    }

    // Load file
    private void loadGame()
    {
        String path = Environment.getExternalStorageDirectory().toString()+"/";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }
    }

    /**
     * Simulates random dice throw by randomly generating number between 1 and 12.
     * @return Integer between 1 and 12.
     */
    public int randomDiceNumber()
    {
        int randomNumber = randomGenerator.nextInt(12)+1;
        return randomNumber;
    }
}
