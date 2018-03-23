package edu.ramapo.philipglazman.kono;

/**
 * Created by mango on 3/23/18.
 */

public class Round {

    private String currentPlayer;
    private String humanPlayerColor;
    private String computerPlayerColor;

    public Round(String afirstPlayer, String ahumanPlayerColor, String acomputerPlayerColor)
    {
        currentPlayer = afirstPlayer;
        humanPlayerColor = ahumanPlayerColor;
        computerPlayerColor = acomputerPlayerColor;
    }

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

    public String getCurrentTurn()
    {
        return currentPlayer;
    }

    public String getComputerPlayerColor()
    {
        return computerPlayerColor;
    }

    public String getHumanPlayerColor()
    {
        return humanPlayerColor;
    }

    public char getHumanPlayerColorAsChar()
    {
        return colorStringToColorChar(humanPlayerColor);
    }

    public char getComputerPlayerColorAsChar()
    {
        return colorStringToColorChar(computerPlayerColor);
    }

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

}
