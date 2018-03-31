package edu.ramapo.philipglazman.kono;

import android.util.Log;

import java.util.Vector;

/**
 * Created by mango on 3/23/18.
 */

public class Round {

    // Constants
    private final String WHITE = "white";
    private final String BLACK = "black";
    private final char charWhite = 'W';
    private final char charBlack = 'B';

    // Players
    private String currentPlayer;
    private String humanPlayerColor;
    private String computerPlayerColor;

    // Player scores.
    private int humanScore;
    private int computerScore;

    public Round(String currentPlayer, String humanPlayerColor, String computerPlayerColor)
    {
        this.currentPlayer = currentPlayer;
        this.humanPlayerColor = humanPlayerColor;
        this.computerPlayerColor = computerPlayerColor;

        humanScore = 0;
        computerScore = 0;
    }

    /**
     * Setter for changing the turn.
     */
    public void setNextTurn()
    {
        if(currentPlayer.equals("human"))
        {
            currentPlayer="computer";
        }
        else if(currentPlayer.equals("computer"))
        {
            currentPlayer="human";
        }
    }

    /**
     * Getter for the winner.
     * @return
     */
    public String getWinner()
    {
        if(computerScore > humanScore)
        {
            return "computer";
        }
        else if(humanScore > computerScore)
        {
            return "human";
        }
        else
        {
            return "draw";
        }
    }

    /**
     * Getter for human score.
     * @return humanScore, integer.
     */
    public int getHumanScore()
    {
        return humanScore;
    }

    /**
     * Getter for computer score.
     * @return computerScore, integer.
     */
    public int getComputerScore()
    {
        return computerScore;
    }

    /**
     * Getter for player of current turn.
     * @return currentPlayer, String/
     */
    public String getCurrentTurn()
    {
        return currentPlayer;
    }

    /**
     * Getter for computer player color.
     * @return computerPlayerColor.
     */
    public String getComputerPlayerColor()
    {
        return computerPlayerColor;
    }

    /**
     * Getter for human player color.
     * @return humanPlayerColor.
     */
    public String getHumanPlayerColor()
    {
        return humanPlayerColor;
    }

    /**
     * Getter for human player color as character.
     * @return char.
     */
    public char getHumanPlayerColorAsChar()
    {
        return colorStringToColorChar(humanPlayerColor);
    }

    /**
     * Getter for computer player color as character.
     * @return char.
     */
    public char getComputerPlayerColorAsChar()
    {
        return colorStringToColorChar(computerPlayerColor);
    }

    /**
     * Converts color as string to color as char.
     * @param color, string.
     * @return char.
     */
    private char colorStringToColorChar(String color)
    {
        if(color.equals("white"))
        {
            return 'W';
        }
        else
        {
            return 'B';
        }
    }

    /**
     * Checks if the winning condition has been met.
     * @param blackSide
     * @param whiteSide
     * @param numberOfBlack
     * @param numberOfWhite
     * @return boolean.
     */
    public boolean isWon(Vector<Character> blackSide,Vector<Character> whiteSide, int numberOfBlack, int numberOfWhite)
    {
        // Get count of pieces for each player.
        int numBlack = numberOfBlack;
        int numWhite = numberOfWhite;

        // Subtract from the number of white pieces that have reached the opposite end.
        for(int i =0; i < blackSide.size(); i++)
        {
            if(blackSide.elementAt(i) == 'w' || blackSide.elementAt(i) == 'W')
            {
                numWhite--;
            }
        }

        // Subtract from the number of black pieces that have reached the opposite end.
        for(int i=0; i < whiteSide.size(); i++)
        {
            if(whiteSide.elementAt(i) == 'b' || whiteSide.elementAt(i) == 'B')
            {
                numBlack--;
            }
        }

        // If a player has moved all their pieces to the other side, then game is won.
        if(numWhite == 0 || numBlack == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Calculates the scores for each player.
     * @param board, state of the board.
     * @param numWhite, number of white pieces.
     * @param numBlack, number of black pieces.
     */
    public void calculateScore(char[][] board, int numWhite, int numBlack) {

        humanScore = 0;
        computerScore = 0;

        // # of pieces each player starts with.
        int numberOfPieces = board.length + 2;

        // Get the total number of black pieces.
        int numberOfBlack = numBlack;

        // Get the total number of white pieces.
        int numberOfWhite = numWhite;


        // Make everything uppercase.
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 'w') {
                    board[i][j] = 'W';
                } else if (board[i][j] == 'b') {
                    board[i][j] = 'B';
                }
            }
        }

        // Calculate Score if player is White
        if(humanPlayerColor.equals(WHITE))
        {
            // Deduct capture pieces.
            humanScore += (numberOfPieces - numberOfBlack) * 5;

            //calculate human score
            if(board[board.length-1][0] == charWhite)
            {
                humanScore+=3;
            }
            if(board[board.length-1][1] == charWhite)
            {
                humanScore+=1;
            }
            if(board[board.length-1][2] == charWhite)
            {
                humanScore+=5;
            }
            //board is 7x7
            if(board.length == 7)
            {
                if(board[board.length-1][3] == charWhite)
                {
                    humanScore+=7;
                }
                if(board[board.length-1][4] == charWhite)
                {
                    humanScore+=5;
                }

            }
            // board is 9x9
            if(board.length == 9)
            {
                if(board[board.length-1][3] == charWhite)
                {
                    humanScore+=7;
                }
                if(board[board.length-1][4] == charWhite)
                {
                    humanScore+=9;
                }
                if(board[board.length-1][5] == charWhite)
                {
                    humanScore+=7;
                }
                if(board[board.length-1][6] == charWhite)
                {
                    humanScore+=5;
                }
            }

            if(board[board.length-1][board.length-2] == charWhite)
            {
                humanScore+=1;
            }
            if(board[board.length-1][board.length-1] == charWhite)
            {
                humanScore+=3;
            }

            if(board[board.length-2][0] == charWhite)
            {
                humanScore+=1;
            }
            if(board[board.length-2][board.length-1] == charWhite)
            {
                humanScore+=1;
            }
        }


        if(computerPlayerColor.equals(WHITE))
        {
            computerScore +=  (numberOfPieces - numberOfBlack) * 5;

            //calculate human score
            if(board[board.length-1][0] == charWhite)
            {
                computerScore+=3;
            }
            if(board[board.length-1][1] == charWhite)
            {
                computerScore+=1;
            }
            if(board[board.length-1][2] == charWhite)
            {
                computerScore+=5;
            }
            //board is 7x7
            if(board.length == 7)
            {
                if(board[board.length-1][3] == charWhite)
                {
                    computerScore+=7;
                }
                if(board[board.length-1][4] == charWhite)
                {
                    computerScore+=5;
                }

            }
            // board is 9x9
            if(board.length == 9)
            {
                if(board[board.length-1][3] == charWhite)
                {
                    computerScore+=7;
                }
                if(board[board.length-1][4] == charWhite)
                {
                    computerScore+=9;
                }
                if(board[board.length-1][5] == charWhite)
                {
                    computerScore+=7;
                }
                if(board[board.length-1][6] == charWhite)
                {
                    computerScore+=5;
                }
            }

            if(board[board.length-1][board.length-2] == charWhite)
            {
                computerScore+=1;
            }
            if(board[board.length-1][board.length-1] == charWhite)
            {
                computerScore+=3;
            }

            if(board[board.length-2][0] == charWhite)
            {
                computerScore+=1;
            }
            if(board[board.length-2][board.length-1] == charWhite)
            {
                computerScore+=1;
            }
        }


        if(humanPlayerColor.equals(BLACK))
        {
            humanScore +=  (numberOfPieces - numberOfWhite) * 5;

            //calculate human score
            if(board[0][0] == charBlack)
            {
                humanScore+=3;
            }
            if(board[0][1] == charBlack)
            {
                humanScore+=1;
            }
            if(board[0][2] == charBlack)
            {
                humanScore+=5;
            }
            //board is 7x7
            if(board.length == 7)
            {
                if(board[0][3] == charBlack)
                {
                    humanScore+=7;
                }
                if(board[0][4] == charBlack)
                {
                    humanScore+=5;
                }

            }
            // board is 9x9
            if(board.length == 9)
            {
                if(board[0][3] == charBlack)
                {
                    humanScore+=7;
                }
                if(board[0][4] == charBlack)
                {
                    humanScore+=9;
                }
                if(board[0][5] == charBlack)
                {
                    humanScore+=7;
                }
                if(board[0][6] == charBlack)
                {
                    humanScore+=5;
                }
            }

            if(board[0][board.length-2] == charBlack)
            {
                humanScore+=1;
            }
            if(board[0][board.length-1] == charBlack)
            {
                humanScore+=3;
            }

            if(board[1][0] == charBlack)
            {
                humanScore+=1;
            }
            if(board[1][board.length-1] == charBlack)
            {
                humanScore+=1;
            }
        }

        if(computerPlayerColor.equals(BLACK))
        {
            computerScore +=  (numberOfPieces - numberOfWhite) * 5;

            //calculate human score
            if(board[0][0] == charBlack)
            {
                computerScore+=3;
            }
            if(board[0][1] == charBlack)
            {
                computerScore+=1;
            }
            if(board[0][2] == charBlack)
            {
                computerScore+=5;
            }
            //board is 7x7
            if(board.length == 7)
            {
                if(board[0][3] == charBlack)
                {
                    computerScore+=7;
                }
                if(board[0][4] == charBlack)
                {
                    computerScore+=5;
                }

            }
            // board is 9x9
            if(board.length == 9)
            {
                if(board[0][3] == charBlack)
                {
                    computerScore+=7;
                }
                if(board[0][4] == charBlack)
                {
                    computerScore+=9;
                }
                if(board[0][5] == charBlack)
                {
                    computerScore+=7;
                }
                if(board[0][6] == charBlack)
                {
                    computerScore+=5;
                }
            }

            if(board[0][board.length-2] == charBlack)
            {
                computerScore+=1;
            }
            if(board[0][board.length-1] == charBlack)
            {
                computerScore+=3;
            }

            if(board[1][0] == charBlack)
            {
                computerScore+=1;
            }
            if(board[1][board.length-1] == charBlack)
            {
                computerScore+=1;
            }
        }

    }
}
