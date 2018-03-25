package edu.ramapo.philipglazman.kono;

import android.util.Log;
import android.util.Pair;

import java.util.Random;
import java.util.Vector;

/**
 * Created by mango on 3/23/18.
 */

public class Computer {

    // Available Strategies
    private final String FORWARD = "forward";
    private final String RETREAT = "retreat";
    private final String BLOCK = "block";
    private final String CAPTURE = "capture";

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
        this.playerColor = new String(playerColor);

        Log.d("computer color",playerColor);

        if (playerColor.equals("white")) {
            this.pieceColor = new Character('W');
            this.opponentPieceColor = new Character('B');
        } else if (playerColor.equals("black")) {
            this.pieceColor = new Character('B');
            this.opponentPieceColor = new Character('W');
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

    public String getStrategy()
    {
        return coordinates.getStrategy();
    }

    public void play(char[][] board) {
        this.board=null;
        this.availablePieces=null;
        this.closestOpponent=null;
        this.availableSuperPieces=null;

        // Set available pieces.
        this.board = new char[board.length][board.length];

        for(int i=0; i<board.length; i++)
        {
            for(int j=0; j<board[i].length; j++)
            {
                this.board[i][j]=board[i][j];
            }
        }

        setAvailablePieces();

        // Set closest opponent.
        setClosestOpponent();

        Log.d("Opponent row",Integer.toString(closestOpponent.first));
        Log.d("Opponent column",Integer.toString(closestOpponent.second));

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
            Log.d("Piece to move row", Integer.toString(row));
            Log.d("Piece to move column", Integer.toString(column));

            if (playerColor.equals("black")) {
                if (isMoveNorthEast(row, column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"northeast",FORWARD);
                    break;
                }
                else if(isMoveNorthWest(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"northwest",FORWARD);
                    break;
                }

            } else if (playerColor.equals("white")) {
                if(isMoveSouthEast(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"southeast",FORWARD);
                    break;

                }
                if(isMoveSouthWest(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"southwest",FORWARD);
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

        for(int i = 0; i < availablePieces.size(); i++) {
            int row = availablePieces.elementAt(i).first;
            int column = availablePieces.elementAt(i).second;

            if (playerColor.equals("black")) {
                if (isMoveSouthEast(row, column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"southwest",RETREAT);
                    return true;
                }
                else if(isMoveSouthWest(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"southeast",RETREAT);
                    return true;
                }

            } else if (playerColor.equals("white")) {
                if(isMoveNorthEast(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"northeast",RETREAT);
                    return true;
                }
                if(isMoveNorthWest(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"northwest",RETREAT);
                    return true;
                }
            }
        }

        return true;
    }

    private boolean isMoveNorthEast(int row, int column)
    {
        if(isOutOfBounds(row-1,column+1))
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
        if(isOutOfBounds(row-1,column-1))
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
        if(isOutOfBounds(row+1,column+1))
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
        if(isOutOfBounds(row+1,column-1))
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
            if(isFriendlyEast(row,column) && isOpenLocation(row+1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column+2),Pair.create(row+1,column+1),"southeast",BLOCK);
                return true;
            }
            else if (isFriendlySouthEast(row,column) && isOpenLocation(row+1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row+2,column+2),Pair.create(row+1,column+1),"northwest",BLOCK);
                return true;
            }
            else if(isFriendlySouth(row,column) && isOpenLocation(row+1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row+2,column),Pair.create(row+1,column+1),"northeast",BLOCK);
                return true;
            }
        }
        else if(playerColor.equals("white"))
        {
            if(isFriendlyEast(row,column) && isOpenLocation(row-1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column+2),Pair.create(row-1,column+1),"northwest",BLOCK);
                return true;
            }
            else if (isFriendlyNorthEast(row,column) && isOpenLocation(row-1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row-2,column+2),Pair.create(row-1,column+1),"southwest",BLOCK);
                return true;
            }
            else if(isFriendlyNorth(row,column) && isOpenLocation(row-1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row-2,column),Pair.create(row-1,column+1),"southeast",BLOCK);
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
            if(isFriendlyWest(row,column) && isOpenLocation(row+1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column-2),Pair.create(row+1,column-1),"southeast",BLOCK);
                return true;
            }
            else if (isFriendlySouthWest(row,column) && isOpenLocation(row+1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row+2,column-2),Pair.create(row+1,column-1),"northeast",BLOCK);
                return true;
            }
            else if(isFriendlySouth(row,column) && isOpenLocation(row+1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row+2,column),Pair.create(row+1,column-1),"northwest",BLOCK);
                return true;
            }
        }
        else if(playerColor.equals("white"))
        {
            if(isFriendlyWest(row,column) && isOpenLocation(row-1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column-2),Pair.create(row-1,column-1),"northeast",BLOCK);
                return true;
            }
            else if (isFriendlyNorthWest(row,column) && isOpenLocation(row-1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row-2,column-2),Pair.create(row-1,column-1),"southeast",BLOCK);
                return true;
            }
            else if(isFriendlyNorth(row,column) && isOpenLocation(row-1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row-2,column),Pair.create(row-1,column-1),"southwest",BLOCK);
                return true;
            }
        }
        return false;
    }

    private boolean isOpenLocation(int row, int column)
    {
        if(isOutOfBounds(row,column))
        {
            return false;
        }

        if (board[row][column] == '+')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isFriendlySouth(int row, int column)
    {
        if(isOutOfBounds(row+2,column))
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
        if(isOutOfBounds(row-2,column))
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
        if(isOutOfBounds(row+2,column+2))
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
        if(isOutOfBounds(row+2,column=2))
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
        if(isOutOfBounds(row-2,column-2))
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
        if(isOutOfBounds(row-2,column+2))
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
        if(isOutOfBounds(row,column-2))
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
        if(isOutOfBounds(row,column+2))
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
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"northeast",CAPTURE);
                return true;
            }
            else if (isOpponentNorthWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"northwest",CAPTURE);
                return true;
            }
            else if(isOpponentSouthEast(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"southeast",CAPTURE);
                return true;
            }
            else if(isOpponentSouthWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"southwest",CAPTURE);
                return true;
            }

        }

        return false;
    }


    private boolean isOpponentSouthWest(int row, int column)
    {
        if(isOutOfBounds(row+1,column-1))
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
        if(isOutOfBounds(row+1,column+1))
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
        if(isOutOfBounds(row-1,column-1))
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
        if(isOutOfBounds(row-1,column+1))
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


    private void setAvailablePieces()
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

    private int setClosestOpponent()
    {
        if(playerColor.equals("white"))
        {
            for(int i = 0; i < board.length; i++)
            {
                for(int j =0; j < board.length; j++)
                {
                    if(board[i][j]==opponentPieceColor || board[i][j] == Character.toLowerCase(opponentPieceColor))
                    {
                        Log.d("opponent",Integer.toString(i)+Integer.toString(j));
                        closestOpponent = new Pair<Integer,Integer> (i,j);
                        return 0;
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
                        Log.d("opponent",Integer.toString(i)+Integer.toString(j));
                        closestOpponent = new Pair<Integer,Integer> (i,j);
                        return 0;
                    }
                }
            }
        }
        return 1;
    }
}