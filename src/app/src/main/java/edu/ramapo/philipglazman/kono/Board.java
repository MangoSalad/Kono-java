package edu.ramapo.philipglazman.kono;

import android.util.Log;

import java.util.Vector;

/**
 * Created by mango on 3/22/18.
 */

public class Board {
    private char[][] board;

    public Board(int boardSize)
    {
        // Create new board.
        board = new char[boardSize][boardSize];

        placeHomePoints();

        printBoard();
    }

    /**
     * Generates home pieces on a new board.
     */
    private void placeHomePoints()
    {
        // Generate board pieces.
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                board[i][j] = '+';
            }
        }

        // Place white pieces on white side.
        for(int i = 0; i < board.length; i++)
        {
            board[0][i] = 'W';
        }

        board[1][0] = 'W';
        board[1][board.length-1] = 'W';

        // Place black pieces on black side.
        for(int i = 0; i < board.length; i++)
        {
            board[board.length-1][i] = 'B';
        }

        board[board.length-2][0] = 'B';
        board[board.length-2][board.length-1] = 'B';
    }

    /**
     * Debugging function.
     */
    public void printBoard()
    {
        for (char[] a : board) {
            for (char i : a) {
                Log.d("board",i + "\t");
            }
            Log.d("board","\n");
        }
    }

    private boolean isOutOfBounds(int row, int column)
    {
        if(row - 1 < 0 || column - 1 < 0 || row - 1 >= board.length || column - 1 >= board.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isValidPieceToMove(char color, int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }
        else
        {
            // Row/Column in board matches the color provided.
            if( board[row-1][column-1] == color || board[row-1][column-1] == Character.toLowerCase(color))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public boolean isValidLocationToMove(int row, int column, boolean isSuperPiece)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }
        else
        {
            if(isSuperPiece)
            {
                return true;
            }
            else
            {
                if(board[row-1][column-1] == '+')
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }

    public boolean isValidOpenLocation(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }
        else
        {
            if(board[row-1][column-1] == '+')
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

}
