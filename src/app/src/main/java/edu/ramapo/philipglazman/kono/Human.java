package edu.ramapo.philipglazman.kono;

import android.util.Pair;

import java.util.Stack;

/**
 * Created by mango on 3/23/18.
 */

public class Human {

    //public Stack<Pair<Integer,Integer>> playerMoves;

    private int initialRow;
    private int initialColumn;

    private int finalRow;
    private int finalColumn;

    public Human()
    {
        this.clear();
    }

    public void setInitialPosition(int row, int column)
    {
        initialRow=row;
        initialColumn=column;
    }

    public void setFinalPosition(int row, int column)
    {
        finalRow=row;
        finalColumn=column;
    }

    public int getInitialRow(){return initialRow;}

    public int getInitialColumn(){return initialColumn;}

    public int getFinalRow(){return finalRow;}

    public int getFinalColumn(){return finalColumn;}

    public boolean isInitialPieceSelected()
    {
        if(initialRow >= 0 && initialColumn >= 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void clear()
    {
        initialRow = -1;
        initialColumn = -1;
        finalRow = -1;
        finalColumn = -1;

    }

}
