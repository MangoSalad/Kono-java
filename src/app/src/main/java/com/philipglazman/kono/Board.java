package com.philipglazman.kono;

/**
 * Created by mango on 3/8/18.
 */

public class Board {


    final static int BOARDSIZE = 6;
    public static char[][] gameBoard;

    /**
     * Create Board.
     */
    public static void createBoard()
    {
        gameBoard = new char[BOARDSIZE][BOARDSIZE];

        // Place white pieces onto the white side.
        for(int i = 1; i < BOARDSIZE; i++)
        {
            gameBoard[1][i] = 'W';
        }

        gameBoard[2][1] = 'W';
        gameBoard[2][BOARDSIZE - 1] = 'W';


        // Place black pieces onto the black side.
        for(int i = 0; i < BOARDSIZE; i++)
        {
            gameBoard[BOARDSIZE-1][i] = 'B';
        }

        gameBoard[BOARDSIZE-2][1] = 'B';
        gameBoard[BOARDSIZE-2][BOARDSIZE-1] = 'B';
    }

    /**
     * Checks if the coordinate provided can be moved.
     * @param a_color
     * @param a_row
     * @param a_column
     * @return
     */
    public final static boolean isValidPieceToMove(char a_color, int a_row, int a_column)
    {
        if(a_row <= 0 || a_column <= 0 || a_row >= BOARDSIZE || a_column >= BOARDSIZE)
        {
            return false;
        }
        else
        {
            if(gameBoard[a_row][a_column] == a_color)
            {
                return true;
            }
            else
            {
                return true;
            }
        }
    }

    /**
     * Check if a given coordinate is open to move to.
     * @param a_row
     * @param a_column
     * @return
     */
    public final static boolean isValidOpenLocation(int a_row, int a_column)
    {
        if(a_row <= 0 || a_column <= 0 || a_row >= BOARDSIZE || a_column >= BOARDSIZE)
        {
            return false;
        }
        else
        {
            if(gameBoard[a_row][a_column]=='+')
            {
                return true;
            }
            else
            {
                return false;
            }

        }
    }




    public static void main(String[] args)
    {
        gameBoard = new char[BOARDSIZE][BOARDSIZE];
    }

}
