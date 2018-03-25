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

    public char[][] getBoard()
    {
        return board;
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

    public int getBoardLength()
    {
        return board.length;
    }

    public char getPieceAtCoordinates(int row, int column)
    {
        return board[row][column];
    }

    private boolean isOutOfBounds(int row, int column)
    {
        if(row < 0 || column < 0 || row >= board.length || column >= board.length)
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
            Log.d("isvalidPieceToMove","out of bounds");
            return false;
        }
        else
        {
            // Row/Column in board matches the color provided.
            if( board[row][column] == color || board[row][column] == Character.toLowerCase(color))
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
                if(board[row][column] == '+')
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

    public boolean isValidMove(int initialRow, int initialColumn, int finalRow, int finalColumn)
    {
        if(isOutOfBounds(initialRow,initialColumn) || isOutOfBounds(finalRow,finalColumn))
        {
            return false;
        }

        // Movement is is northwest.
        if( (initialRow-1) == finalRow && (initialColumn-1) == finalColumn)
        {
            return true;
        }
        // Movement is northeast.
        else if( (initialRow-1) == finalRow && (initialColumn+1) == finalColumn)
        {
            return true;
        }
        // Movement is southwest.
        else if( (initialRow+1) == finalRow && (initialColumn-1) == finalColumn)
        {
            return true;
        }
        // Movement is southeast.
        else if( (initialRow+1) == finalRow && (initialColumn+1) == finalColumn)
        {
            return true;
        }
        // Movement is invalid.
        else
        {
            return false;
        }
    }

    public void updateBoard(int initialRow, int initialColumn, int finalRow, int finalColumn)
    {
        if(isReadyToUpgrade(initialRow,initialColumn, finalRow))
        {
            board[finalRow][finalColumn]=Character.toLowerCase(board[initialRow][initialColumn]);
        }
        else
        {
            board[finalRow][finalColumn]=board[initialRow][initialColumn];
        }
        board[initialRow][initialColumn]='+';
        printBoard();
    }

    private boolean isReadyToUpgrade(int initialRow, int initialColumn, int finalRow)
    {
        if(board[initialRow][initialColumn]=='W')
        {
            if(finalRow==board.length-1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if(board[initialRow][initialColumn]=='B')
        {
            if(finalRow==0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
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
            if(board[row][column] == '+')
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public boolean isSuperPiece(int row, int column)
    {
        if(board[row][column] == 'w' || board[row][column] == 'b')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
