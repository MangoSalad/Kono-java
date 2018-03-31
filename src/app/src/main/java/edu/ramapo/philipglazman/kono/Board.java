package edu.ramapo.philipglazman.kono;

import android.util.Log;

import java.util.Vector;

/**
 * Responsible for board data model.
 */

public class Board {

    // Board object.
    private char[][] board;

    /**
     * Constructor for new board.
     * @param boardSize
     */
    public Board(int boardSize)
    {
        // Creates new board.
        board = new char[boardSize][boardSize];

        // Generates pieces on board.
        placeHomePoints();
    }

    /**
     * Constructor for loading an existing board.
     * @param board
     */
    public Board(char[][] board)
    {
        this.board = new char[board.length][board.length];

        for(int i=0;i<board.length;i++)
        {
            for(int j=0;j<board.length;j++)
            {
                this.board[i][j]=board[i][j];
            }
        }
    }

    /**
     * Getter for board object.
     * @return board, char[][].
     */
    public char[][] getBoard()
    {
        return board;
    }

    /**
     * Getter for board length.
     * @return board length, integer.
     */
    public int getBoardLength()
    {
        return board.length;
    }

    /**
     * Getter for piece at specific coordinate.
     * @param row, integer.
     * @param column, integer.
     * @return piece located at provided coordinates, char.
     */
    public char getPieceAtCoordinates(int row, int column)
    {
        return board[row][column];
    }

    /**
     * Getter for the number of white pieces on the board.
     * @return number of white pieces, integer.
     */
    public int getNumOfWhitePieces()
    {
        int numWhite = 0;

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board.length; j++)
            {
                if(board[i][j] == 'w' || board[i][j] == 'W')
                {
                    numWhite++;
                }
            }
        }

        return numWhite;
    }

    /**
     * Getter for the number of black pieces on the board.
     * @return number of black pieces, integer.
     */
    public int getNumOfBlackPieces()
    {
        int numBlack = 0;

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board.length; j++)
            {
                if(board[i][j] == 'b' || board[i][j] == 'B')
                {
                    numBlack++;
                }
            }
        }

        return numBlack;
    }

    /**
     * Getter for the pieces located on the black side.
     * @return vector of pieces located on the black side, vector.
     */
    public Vector<Character> getBlackSide()
    {
        Vector<Character> blackSide = new Vector<>(board.length + 2, '+');

        int  i =0;
        for(; i < board.length; i++)
        {
            blackSide.add(i,board[board.length-1][i]);
        }

        blackSide.add(i,board[board.length-2][0]);
        i++;
        blackSide.add(i,board[board.length-2][board.length-1]);

        return blackSide;
    }

    /**
     * Getter for the pieces located on the white side.
     * @return vector of pieces located on the white side, vector.
     */
    public Vector<Character> getWhiteSide()
    {
        Vector<Character> whiteSide = new Vector<>(board.length + 2, '+');

        int i = 0;
        for(; i < board.length; i++)
        {
            whiteSide.add(i,board[0][i]);
        }

        whiteSide.add(i,board[1][0]);
        i++;
        whiteSide.add(i,board[1][board.length-1]);

        return whiteSide;
    }

    /**
     * Checks to see if the piece at the given coordinates is a valid piece to move.
     * @param color, color of the player, char.
     * @param row, integer.
     * @param column, integer.
     * @return boolean.
     */
    public boolean isValidPieceToMove(char color, int row, int column)
    {
        // Check if out of bounds.
        if(isOutOfBounds(row,column))
        {
            return false;
        }
        else
        {
            // Row/Column in board matches the color provided.
            if( board[row][column] == color || board[row][column] == Character.toLowerCase(color))
            {
                return true;
            }
            // Cannot move piece.
            else
            {
                return false;
            }
        }
    }

    /**
     * Checks to see if the given coordinate is a valid place to move to.
     * @param row, integer.
     * @param column, integer.
     * @param isSuperPiece, boolean for if piece at coordinate is a super piece.
     * @return boolean.
     */
    public boolean isValidLocationToMove(int row, int column, boolean isSuperPiece)
    {
        // Check out of bounds.
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
                // Position is open.
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

    /**
     * Checks if the overall move is valid.
     * @param initialRow, integer.
     * @param initialColumn, integer.
     * @param finalRow, integer.
     * @param finalColumn, integer.
     * @return boolean.
     */
    public boolean isValidMove(int initialRow, int initialColumn, int finalRow, int finalColumn)
    {
        // Check out of bounds.
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

    /**
     * Updates the state of the board given initial coordinates and final coordinates.
     * @param initialRow, integer.
     * @param initialColumn, integer.
     * @param finalRow, integer.
     * @param finalColumn, integer.
     */
    public void updateBoard(int initialRow, int initialColumn, int finalRow, int finalColumn)
    {
        // Checks if the piece is ready to be upgraded to super piece.
        if(isReadyToUpgrade(initialRow,initialColumn, finalRow))
        {
            board[finalRow][finalColumn]=Character.toLowerCase(board[initialRow][initialColumn]);
        }
        else
        {
            // Update board.
            board[finalRow][finalColumn]=board[initialRow][initialColumn];
        }

        // Set old position to open.
        board[initialRow][initialColumn]='+';
    }

    /**
     * Checks if a piece is ready to be upgraded to super piece.
     * @param initialRow
     * @param initialColumn
     * @param finalRow
     * @return boolean.
     */
    private boolean isReadyToUpgrade(int initialRow, int initialColumn, int finalRow)
    {
        // If the piece is white.
        if(board[initialRow][initialColumn]=='W')
        {
            // Check if the piece reached the black side.
            if(finalRow==board.length-1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        // If piece is black.
        else if(board[initialRow][initialColumn]=='B')
        {
            // Check if the piece reached the white side.
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

    /**
     * Checks if a piece at the given coordinates is a super piece.
     * @param row
     * @param column
     * @return boolean.
     */
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
     * Checks to see if the given coordinates are out of bounds.
     * @param row, integer.
     * @param column, integer.
     * @return boolean.
     */
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

    /**
     * Used for debugging by printing board pieces to log.
     */
    int main()
    {
        for (char[] a : board) {
            for (char i : a) {
                Log.d("board",i + "\t");
            }
            Log.d("board","\n");
        }

        return 0;
    }

}
