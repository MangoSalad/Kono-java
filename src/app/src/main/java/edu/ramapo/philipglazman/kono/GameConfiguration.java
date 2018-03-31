package edu.ramapo.philipglazman.kono;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mango on 3/25/18.
 */

public class GameConfiguration {

    private String startType;
    private Random randomGenerator = new Random();

    private int roundNum;
    private int computerScore;
    private String computerColor;
    private int humanScore;
    private String humanColor;
    private String nextPlayer;
    private char[][] board;


    /**
     * Constructor for preparing a game configuration. Used for loading a game file.
     * @param startType
     */
    public GameConfiguration(String startType) {
        this.startType = new String(startType);
    }

    /**
     * Constructor for loading a game configuration. Used for saving the game to file.
     * @param roundNum
     * @param computerScore
     * @param computerColor
     * @param humanScore
     * @param humanColor
     * @param board
     * @param nextPlayer
     */
    public GameConfiguration(int roundNum, int computerScore, String computerColor,
                             int humanScore, String humanColor, char[][] board, String nextPlayer)
    {
        this.roundNum = roundNum;
        this.computerScore = computerScore;
        this.computerColor = computerColor;
        this.humanScore = humanScore;
        this.humanColor = humanColor;

        this.board = new char[board.length][board.length];

        // Load board.
        for(int i =0; i < board.length; i++)
        {
            for(int j =0; j <board.length; j++)
            {
                this.board[i][j]=board[i][j];
            }
        }

        this.nextPlayer = nextPlayer;
    }

    /**
     * Getter for round number.
     * @return round number.
     */
    public int getRoundNum() {
        return roundNum;
    }

    /**
     * Getter for computer score.
     * @return computer score.
     */
    public int getComputerScore() {
        return computerScore;
    }

    /**
     * Getter for computer color.
     * @return computer color, string.
     */
    public String getComputerColor() {
        return computerColor.toLowerCase();
    }

    /**
     * Getter for human score.
     * @return human score.
     */
    public int getHumanScore() {
        return humanScore;
    }

    /**
     * Getter for human color.
     * @return human color.
     */
    public String getHumanColor() {
        return humanColor.toLowerCase();
    }

    /**
     * Getter for board.
     * @return board.
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Getter for next player.
     * @return next player.
     */
    public String getNextPlayer() {
        return nextPlayer.toLowerCase();
    }

    /**
     * Saves game configuration to a file.
     * @param fileName, file to save to.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveGame(String fileName)
    {
        // Load directory.
        File filePath = Environment.getExternalStorageDirectory();
        File file = new File(filePath,fileName);

        // If file exists, delete it.
        if(file.exists())
        {
            file.delete();
        }

        // Create a new file.
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Use filewriter and bufferedwriter to write to file.
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);

            try {
                // Write to the file.

                bw.write("Round: " + Integer.toString(roundNum));
                bw.newLine();

                bw.write("Computer: ");
                bw.newLine();
                bw.write("   Score: " + Integer.toString(computerScore));
                bw.newLine();
                bw.write("   Color: " + computerColor.substring(0, 1).toUpperCase() + computerColor.substring(1));
                bw.newLine();

                bw.write("Human: ");
                bw.newLine();
                bw.write("   Score: " + Integer.toString(humanScore));
                bw.newLine();
                bw.write("   Color: " + humanColor.substring(0, 1).toUpperCase() + humanColor.substring(1));
                bw.newLine();

                bw.write("Board: ");
                bw.newLine();

                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board.length; j++) {

                        Log.d("FILE", Character.toString(board[i][j]));
                        bw.write("   ");

                        if (board[i][j] == '+') {
                            bw.write("O");
                        } else if (board[i][j] == 'w') {
                            bw.write("WW");
                        } else if (board[i][j] == 'b') {
                            bw.write("BB");
                        } else if (board[i][j] == 'B') {
                            bw.write("B");
                        } else if (board[i][j] == 'W') {
                            bw.write("W");
                        }
                    }

                    bw.newLine();
                }

                bw.newLine();
                bw.write("Next Player: " + nextPlayer.substring(0, 1).toUpperCase() + nextPlayer.substring(1));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close the buffer and file.
                bw.close();
                fw.close();
            }
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    /**
     * Load an existing game file.
     * @param fileName, file to load.
     */
    public void loadGame(String fileName)
    {
        // Working directory.
        File filePath = Environment.getExternalStorageDirectory();
        File file = new File(filePath,fileName);

        try {
            // Use bufferedreader to parse file.
            BufferedReader br = new BufferedReader(new FileReader(file));

            // Hold contents.
            String line;

            while ((line = br.readLine()) != null)
            {
                // Get Round Number.
                if(line.contains("Round:"))
                {
                    String split[]= line.split(" ");
                    roundNum = Integer.parseInt(split[1]);
                    Log.d("RoundNum",Integer.toString(roundNum));
                }

                // Get computer information.
                if(line.contains("Computer:"))
                {
                    String score = br.readLine();
                    String scoreSplit[]= score.split(" ");
                    computerScore = Integer.parseInt(scoreSplit[scoreSplit.length-1]);
                    Log.d("ComputerScore",Integer.toString(computerScore));

                    String color = br.readLine();
                    String colorSplit[]= color.split(" ");
                    computerColor = colorSplit[colorSplit.length-1];
                    Log.d("ComputerColor",computerColor);
                }

                // Get human information.
                if(line.contains("Human:"))
                {
                    String score = br.readLine();
                    String scoreSplit[]= score.split(" ");
                    humanScore = Integer.parseInt(scoreSplit[scoreSplit.length-1]);
                    Log.d("HumanScore",Integer.toString(humanScore));

                    String color = br.readLine();
                    String colorSplit[]= color.split(" ");
                    humanColor = colorSplit[colorSplit.length-1];
                    Log.d("HumanColor",humanColor);
                }

                // Load board.
                if(line.contains("Board:"))
                {
                    char[][] initBoard = new char[9][9];
                    int rowIndex = 0;
                    int columnIndex = 0;

                    while(!line.isEmpty())
                    {
                        line = br.readLine();
                        String split[]= line.split(" ");
                        for(int i = 0; i < split.length ; i++)
                        {
                            Log.d("board",split[i]);
                            if(split[i].equals("O"))
                            {
                                initBoard[rowIndex][columnIndex]='+';
                                columnIndex++;
                            }
                            else if(split[i].equals("W"))
                            {
                                initBoard[rowIndex][columnIndex]='W';
                                columnIndex++;
                            }
                            else if(split[i].equals("B"))
                            {
                                initBoard[rowIndex][columnIndex]='B';
                                columnIndex++;
                            }
                            else if(split[i].equals("WW"))
                            {
                                initBoard[rowIndex][columnIndex]='w';
                                columnIndex++;
                            }
                            else if(split[i].equals("BB"))
                            {
                                initBoard[rowIndex][columnIndex]='b';
                                columnIndex++;
                            }
                        }
                        rowIndex++;
                        columnIndex=0;
                    }

                    board = new char[rowIndex-1][rowIndex-1];

                    // Can be done better to reallocate.
                    for(int i=0; i<rowIndex-1; i++)
                    {
                        for(int j=0; j<rowIndex-1; j++)
                        {
                            board[i][j]=initBoard[i][j];
                        }
                    }
                }

                // Load next player.
                if(line.contains("Next Player:"))
                {
                    String split[]= line.split(" ");
                    nextPlayer = split[2];
                    Log.d("NextPlayer",nextPlayer);
                }
            }
            // Close bufferedreader.
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
    }

    /**
     * Checks if the file is valid.
     * @param fileName file to check.
     * @return boolean.
     */
    public boolean isValidFile(String fileName)
    {
        File filePath = Environment.getExternalStorageDirectory();
        File file = new File(filePath,fileName);
        if(file.exists() && !file.isDirectory())
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * Simulates random dice throw by randomly generating number between 1 and 12.
     * @return Integer between 1 and 12.
     */
    public int randomDiceNumber()
    {
        int randomNumber = randomGenerator.nextInt(12)+2;
        return randomNumber;
    }
}
