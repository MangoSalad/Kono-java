package edu.ramapo.philipglazman.kono;

import android.util.Pair;

import java.util.Stack;

/**
 * Responsible for human player turn.
 */

public class Human extends Player{


    public Human()
    {
        this.clear();
    }

    /**
     * Getter for final row coordinate.
     * @return row, integer.
     */
    public int getFinalRow(){return finalRow;}

    /**
     * Getter for final column coordinate.
     * @return column integer.
     */
    public int getFinalColumn(){return finalColumn;}

    /**
     * Sets the initial coordinates for human player move.
     * @param row, row position.
     * @param column, column position.
     */
    public void setInitialPosition(int row, int column)
    {
        initialRow=row;
        initialColumn=column;
    }

    /**
     * Checks to see if an initial coordinate has been set.
     * @return boolean.
     */
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

    /**
     * Clears values for the human player class.
     */
    public void clear()
    {
        initialRow = -1;
        initialColumn = -1;
        finalRow = -1;
        finalColumn = -1;

    }

    /**
     * Sets the final coordinates for human player move.
     * @param row, row position.
     * @param column, column position.
     */
    public void setFinalPosition(int row, int column)
    {
        finalRow=row;
        finalColumn=column;
    }
}
