package edu.ramapo.philipglazman.kono;

import android.util.Log;
import android.util.Pair;

import java.util.Random;
import java.util.Vector;

/**
 * Created by mango on 3/23/18.
 */

public class Computer extends Player{

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


    /**
     * Constructor
     * @param playerColor
     */
    public Computer(String playerColor) {

        this.playerColor = new String(playerColor);

        // Set piece color and opponent piece color.
        if (playerColor.equals("white")) {
            this.pieceColor = new Character('W');
            this.opponentPieceColor = new Character('B');
        } else if (playerColor.equals("black")) {
            this.pieceColor = new Character('B');
            this.opponentPieceColor = new Character('W');
        }
    }

    /**
     * Getter for initial coordinates
     * @return pair holding row/column.
     */
    public Pair<Integer,Integer> getInitialCoordinates()
    {
        return coordinates.getInitialCoordinates();
    }

    /**
     * Getter for final coordinates.
     * @return pair holding row/column.
     */
    public Pair<Integer,Integer> getFinalCoordinates()
    {
        return coordinates.getFinalCoordinates();
    }

    /**
     * Getter for direction.
     * @return string holding direction.
     */
    public String getDirection()
    {
        return coordinates.getDirection();
    }

    /**
     * Getter for strategy.
     * @return string holding strategy.
     */
    public String getStrategy()
    {
        return coordinates.getStrategy();
    }

    /**
     * Responsible for decision-making for computer's play turn.
     * @param board current board state.
     */
    public void play(char[][] board) {

        // Clear any existing values.
        this.board=null;
        this.availablePieces=null;
        this.closestOpponent=null;
        this.availableSuperPieces=null;

        // Set available pieces.
        this.board = new char[board.length][board.length];

        // Load board state.
        for(int i=0; i<board.length; i++)
        {
            for(int j=0; j<board.length; j++)
            {
                this.board[i][j]=board[i][j];
            }
        }

        // Load available pieces.
        setAvailablePieces();

        // Set closest opponent.
        setClosestOpponent();

        // Strategy is following:
        // Check if computer can capture,
        // then check if computer can block east,
        // then check if computer can block west,
        // then check if it must retreat,
        // then play forward.

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

    /**
     * Getter for selecting random piece from available list of pieces.
     * @return pair holding row/column of piece.
     */
    private Pair<Integer,Integer> getRandomPiece()
    {
        int randomNumber = randomGenerator.nextInt(availablePieces.size());
        return availablePieces.elementAt(randomNumber);
    }

    /**
     * Strategy for advancing a piece forward.
     */
    private void playForward()
    {
        // While valid piece is found to move forward.
        while(true)
        {
            // Select random piece.
            Pair<Integer, Integer> randomPiece = getRandomPiece();

            int row = randomPiece.first;
            int column = randomPiece.second;

            if (playerColor.equals("black")) {
                // Move northeast.
                if (isMoveNorthEast(row, column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"northeast",FORWARD);
                    break;
                }
                // Move northwest.
                else if(isMoveNorthWest(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"northwest",FORWARD);
                    break;
                }

            } else if (playerColor.equals("white")) {
                // Move southeast.
                if(isMoveSouthEast(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"southeast",FORWARD);
                    break;

                }
                // Move southwest.
                if(isMoveSouthWest(row,column))
                {
                    coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"southwest",FORWARD);
                    break;
                }
            }
        }
    }

    /**
     * Strategy for retreating a piece.
     * @return boolean if piece can be retreated.
     */
    private boolean playRetreat()
    {
        // For every available piece, check if it can move forward.
        for(int i = 0; i < availablePieces.size(); i++) {
            int row = availablePieces.elementAt(i).first;
            int column = availablePieces.elementAt(i).second;

            // If returns false, then a piece can move forward.
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

        // Since no piece can move forward, find a piece to retreat.
        // Return true.
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

    /**
     * Checks if a piece can move northeast.
     * @param row
     * @param column
     * @return boolean
     */
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

    /**
     * Checks if a piece can move northwest.
     * @param row
     * @param column
     * @return boolean
     */
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

    /**
     * Checks if a piece can move southeast.
     * @param row
     * @param column
     * @return boolean
     */
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

    /**
     * Checks if a piece can move southwest.
     * @param row
     * @param column
     * @return boolean
     */
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

    /**
     * Strategy for playing defensively on east side.
     * @return boolean if move was made.
     */
    private boolean playBlockEast()
    {
        int row = closestOpponent.first;
        int column = closestOpponent.second;

        if(playerColor.equals("black"))
        {
            // Block by moving southeast.
            if(isFriendlyEast(row,column) && isOpenLocation(row+1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column+2),Pair.create(row+1,column+1),"southeast",BLOCK);
                return true;
            }

            // Block by moving northwest.
            else if (isFriendlySouthEast(row,column) && isOpenLocation(row+1,column+1))
            {
                Log.d("BLOCK","PLAYBLOCKEAST");
                coordinates = new TupleCoordinates(Pair.create(row+2,column+2),Pair.create(row+1,column+1),"northwest",BLOCK);
                return true;
            }

            // Block by moving northeast.
            else if(isFriendlySouth(row,column) && isOpenLocation(row+1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row+2,column),Pair.create(row+1,column+1),"northeast",BLOCK);
                return true;
            }
        }
        else if(playerColor.equals("white"))
        {
            // Block by moving northwest.
            if(isFriendlyEast(row,column) && isOpenLocation(row-1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column+2),Pair.create(row-1,column+1),"northwest",BLOCK);
                return true;
            }

            // Block by moving southwest.
            else if (isFriendlyNorthEast(row,column) && isOpenLocation(row-1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row-2,column+2),Pair.create(row-1,column+1),"southwest",BLOCK);
                return true;
            }

            // Block by moving southeast.
            else if(isFriendlyNorth(row,column) && isOpenLocation(row-1,column+1))
            {
                coordinates = new TupleCoordinates(Pair.create(row-2,column),Pair.create(row-1,column+1),"southeast",BLOCK);
                return true;
            }
        }
        return false;
    }

    /**
     * Strategy for playing defensively from west side.s
     * @return boolean if move was made.
     */
    private boolean playBlockWest()
    {
        int row = closestOpponent.first;
        int column = closestOpponent.second;

        if(playerColor.equals("black"))
        {
            // Block by moving southeast.
            if(isFriendlyWest(row,column) && isOpenLocation(row+1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column-2),Pair.create(row+1,column-1),"southeast",BLOCK);
                return true;
            }

            // Block by moving northeast.
            else if (isFriendlySouthWest(row,column) && isOpenLocation(row+1,column-1))
            {
                Log.d("BLOCK","PLAYBLOCKWEST");
                coordinates = new TupleCoordinates(Pair.create(row+2,column-2),Pair.create(row+1,column-1),"northeast",BLOCK);
                return true;
            }

            // Block by moving northwest.
            else if(isFriendlySouth(row,column) && isOpenLocation(row+1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row+2,column),Pair.create(row+1,column-1),"northwest",BLOCK);
                return true;
            }
        }
        else if(playerColor.equals("white"))
        {
            // Block by moving northeast.
            if(isFriendlyWest(row,column) && isOpenLocation(row-1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column-2),Pair.create(row-1,column-1),"northeast",BLOCK);
                return true;
            }

            // Block by moving southeast.
            else if (isFriendlyNorthWest(row,column) && isOpenLocation(row-1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row-2,column-2),Pair.create(row-1,column-1),"southeast",BLOCK);
                return true;
            }

            // Block by moving southwest.
            else if(isFriendlyNorth(row,column) && isOpenLocation(row-1,column-1))
            {
                coordinates = new TupleCoordinates(Pair.create(row-2,column),Pair.create(row-1,column-1),"southwest",BLOCK);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a row/column position is open.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Checks if a friendly piece is located south of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Checks if a friendly piece is located north of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Checks if a friendly piece is located southeast of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Checks if a friendly piece is located southwest of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
    private boolean isFriendlySouthWest(int row, int column)
    {
        if(isOutOfBounds(row+2,column-2))
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

    /**
     * Checks if a friendly piece is located northwest of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Checks if a friendly piece is located northeast of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Checks if a friendly piece is located west of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Checks if a friendly piece is located east of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Strategy for capturing an opponent piece.
     * @return boolean if capture can be made.
     */
    private boolean playCapture()
    {
        // Check every available super piece if it can capture.
        for(int i = 0; i < availableSuperPieces.size(); i++)
        {
            int row = availableSuperPieces.elementAt(i).first;
            int column = availableSuperPieces.elementAt(i).second;

            // Capture by moving northeast.
            if(isOpponentNorthEast(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column+1),"northeast",CAPTURE);
                return true;
            }

            // Capture by moving northwest.
            else if (isOpponentNorthWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row-1,column-1),"northwest",CAPTURE);
                return true;
            }

            // Capture by moving southeast.
            else if(isOpponentSouthEast(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column+1),"southeast",CAPTURE);
                return true;
            }

            // Capture by moving southwest.
            else if(isOpponentSouthWest(row,column))
            {
                coordinates = new TupleCoordinates(Pair.create(row,column),Pair.create(row+1,column-1),"southwest",CAPTURE);
                return true;
            }

        }

        return false;
    }


    /**
     * Check if opponent is located southwest of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Check if opponent is located southeast of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Check if opponent is located northwest of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Check if opponent is located northeast of coordinates row/column.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Checks if coordinates are out of bounds.
     * @param row
     * @param column
     * @return boolean.
     */
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

    /**
     * Loads the vector for holding the location of computer pieces.
     */
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

    /**
     * Finds the closest opponent to block.
     * @return boolean.
     */
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