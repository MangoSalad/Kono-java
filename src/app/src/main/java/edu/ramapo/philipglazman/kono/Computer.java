package edu.ramapo.philipglazman.kono;

import android.util.Pair;

import java.util.Random;
import java.util.Vector;

/**
 * Created by mango on 3/23/18.
 */

public class Computer {

    String playerColor;
    char pieceColor;
    char opponentPieceColor;

    private Random randomGenerator =  new Random();

    private char[][] board;
    Vector<Pair<Integer, Integer>> availablePieces;
    Vector<Pair<Integer, Integer>> availableSuperPieces;
    Pair<Integer, Integer> closestOpponent;

    private TupleCoordinates coordinates;


    public Computer(String playerColor) {
        this.playerColor = playerColor;

        if (playerColor.equals("white")) {
            this.pieceColor = 'W';
            this.opponentPieceColor = 'B';
        } else if (playerColor.equals("black")) {
            this.pieceColor = 'B';
            this.opponentPieceColor = 'W';
        }
    }

    public Pair<Integer,Integer> getInitialCoordinates()
    {
        return coordinates.getInitialCoordinates();
    }

    public Pair<Integer,Integer> getFinalCoordinates()
    {
        return coordinates.getFinalCoordinates();
    }

    public String getDirection()
    {
        return coordinates.getDirection();
    }

    public void play(char[][] board) {
        // Set available pieces.
        this.board = board;

        setAvailablePieces(this.board);

        // Set closest opponent.
        setClosestOpponent(this.board);

        // Strategy is following:
        // Check if computer can capture, then check if computer can block east, then check if computer can block west, then check if it must retreat, then play forward.
        if (!playCapture()) {
            if (!playBlockEast()) {
                if (!playBlockWest()) {
                    if (!playRetreat()) {
                        playForward();
                    }
                }
            }
        } else {
            // do something with coordinates.
        }
    }

    private Pair<Integer,Integer> getRandomPiece()
    {
        int randomNumber = randomGenerator.nextInt(availablePieces.size());
        return availablePieces.elementAt(randomNumber);
    }

    private void playForward()
    {
        while(true)
        {
            Pair<Integer, Integer> randomPiece = getRandomPiece();

            int row = randomPiece.first;
            int column = randomPiece.second;

            if (playerColor.equals("black")) {
                if (isMoveNorthEast(row, column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"northeast");
                    break;
                }
                else if(isMoveNorthWest(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"northwest");
                    break;
                }

            } else if (playerColor.equals("white")) {
                if(isMoveSouthEast(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"southeast");
                    break;

                }
                if(isMoveSouthWest(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"southwest");
                    break;
                }
            }
        }
    }

    private boolean playRetreat()
    {
        for(int i = 0; i < availablePieces.size(); i++) {
            int row = availablePieces.elementAt(i).first;
            int column = availablePieces.elementAt(i).second;

            if (playerColor.equals("black")) {
                if (isMoveNorthEast(row, column))
                {
                    return false;
                }
                else if(isMoveNorthWest(row,column))
                {
                    return false;
                }

            } else if (playerColor.equals("white")) {
                if(isMoveSouthEast(row,column))
                {
                    return false;
                }
                if(isMoveSouthWest(row,column))
                {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isMoveNorthEast(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row-1][column+1] == '+')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isMoveNorthWest(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row-1][column-1] == '+')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isMoveSouthEast(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row+1][column+1] == '+')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isMoveSouthWest(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row+1][column-1] == '+')
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private boolean playBlockEast()
    {
        int row = closestOpponent.first;
        int column = closestOpponent.second;

        if(playerColor.equals("black"))
        {
            if(isFriendlyEast(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"southeast");
                return true;
            }
            else if (isFriendlySouthEast(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"northwest");
                return true;
            }
            else if(isFriendlySouth(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"northeast");
                return true;
            }
        }
        else if(playerColor.equals("white"))
        {
            if(isFriendlyEast(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"northwest");
                return true;
            }
            else if (isFriendlyNorthEast(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"southwest");
                return true;
            }
            else if(isFriendlyNorth(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"southeast");
                return true;
            }
        }
        return false;
    }

    private boolean playBlockWest()
    {
        int row = closestOpponent.first;
        int column = closestOpponent.second;

        if(playerColor.equals("black"))
        {
            if(isFriendlyWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"southeast");
                return true;
            }
            else if (isFriendlySouthWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"northeast");
                return true;
            }
            else if(isFriendlySouth(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"northwest");
                return true;
            }
        }
        else if(playerColor.equals("white"))
        {
            if(isFriendlyWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"northeast");
                return true;
            }
            else if (isFriendlyNorthWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"southeast");
                return true;
            }
            else if(isFriendlyNorth(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"southwest");
                return true;
            }
        }
        return false;
    }

    private boolean isFriendlySouth(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row+2][column] == pieceColor || board[row+2][column] == Character.toLowerCase(pieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isFriendlyNorth(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row-2][column] == pieceColor || board[row-2][column] == Character.toLowerCase(pieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    private boolean isFriendlySouthEast(int row ,int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row+2][column+2] == pieceColor || board[row+2][column+2] == Character.toLowerCase(pieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isFriendlySouthWest(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row+2][column-2] == pieceColor || board[row+2][column-2] == Character.toLowerCase(pieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isFriendlyNorthWest(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row-2][column-2] == pieceColor || board[row-2][column-2] == Character.toLowerCase(pieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isFriendlyNorthEast(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row-2][column+2] == pieceColor || board[row-2][column+2] == Character.toLowerCase(pieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private boolean isFriendlyWest(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row][column-2] == pieceColor || board[row][column-2] == Character.toLowerCase(pieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private boolean isFriendlyEast(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row][column+2] == pieceColor || board[row][column+2] == Character.toLowerCase(pieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private boolean playCapture()
    {
        for(int i = 0; i < availableSuperPieces.size(); i++)
        {
            int row = availableSuperPieces.elementAt(i).first;
            int column = availableSuperPieces.elementAt(i).second;

            if(isOpponentNorthEast(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"northeast");
                return true;
            }
            else if (isOpponentNorthWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"northwest");
                return true;
            }
            else if(isOpponentSouthEast(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"southeast");
                return true;
            }
            else if(isOpponentSouthWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"southwest");
                return true;
            }

        }

        return false;
    }

    private void setInitialCoordinates()
    {

    }

    private void setFinalCoordinates()
    {

    }

    private boolean isOpponentSouthWest(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row+1][column-1] == opponentPieceColor || board[row+1][column-1] == Character.toLowerCase(opponentPieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private boolean isOpponentSouthEast(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row+1][column+1] == opponentPieceColor || board[row+1][column+1] == Character.toLowerCase(opponentPieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isOpponentNorthWest(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row-1][column-1] == opponentPieceColor || board[row-1][column-1] == Character.toLowerCase(opponentPieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isOpponentNorthEast(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if( board[row-1][column+1] == opponentPieceColor || board[row-1][column+1] == Character.toLowerCase(opponentPieceColor) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isOutOfBounds(int row,int column)
    {
        if(row >= board.length || row <0 || column >= board.length || column < 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void setAvailablePieces(char[][] board)
    {
        availablePieces = new Vector<Pair<Integer,Integer>> ();
        availableSuperPieces = new Vector<Pair<Integer,Integer>> ();


        for(int i = 0; i < board.length; i++)
        {
            for(int j =0; j < board.length; j++)
            {
                if(board[i][j]==pieceColor || board[i][j] == Character.toLowerCase(pieceColor))
                {
                    availablePieces.add(Pair.create(i,j));
                    // Super Pieces
                    if(board[i][j] == Character.toLowerCase(pieceColor))
                    {
                        availableSuperPieces.add(Pair.create(i,j));
                    }
                }
            }
        }
    }

    private void setClosestOpponent(char[][] board)
    {
        if(playerColor.equals("white"))
        {
            for(int i = 0; i < board.length; i++)
            {
                for(int j =0; j < board.length; j++)
                {
                    if(board[i][j]==opponentPieceColor || board[i][j] == Character.toLowerCase(opponentPieceColor))
                    {
                        closestOpponent = Pair.create(i,j);
                        break;
                    }
                }
            }
        }
        else if(playerColor.equals("black"))
        {
            for(int i = board.length-1; i > 0; i--)
            {
                for(int j = board.length-1; j > 0; j--)
                {
                    if(board[i][j]==opponentPieceColor || board[i][j] == Character.toLowerCase(opponentPieceColor))
                    {
                        closestOpponent = Pair.create(i,j);
                        break;
                    }
                }
            }
        }
    }
}
