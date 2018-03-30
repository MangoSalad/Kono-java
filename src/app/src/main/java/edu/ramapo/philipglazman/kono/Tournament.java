package edu.ramapo.philipglazman.kono;

/**
 * Created by mango on 3/27/18.
 */

public class Tournament {

    private int roundNum;
    private int computerScore;
    private int humanScore;
    private int awardedPoints;

    public Tournament()
    {
        roundNum = 0;
        computerScore = 0;
        humanScore = 0;
    }

    public Tournament(int roundNum, int computerScore, int humanScore)
    {
        this.roundNum = roundNum;
        this.computerScore = computerScore;
        this.humanScore = humanScore;
    }

    public void setHumanScore(int humanScore)
    {
        this.humanScore=humanScore;
    }

    public void setComputerScore(int computerScore)
    {
        this.computerScore=computerScore;
    }

    public void subtractHumanScore(int numToSubtract)
    {
        this.humanScore-=numToSubtract;
    }

    public void subtractComputerScore(int numToSubtract)
    {
        this.computerScore-=numToSubtract;
    }

    public int getRoundNum()
    {
        return roundNum;
    }

    public int getComputerScore()
    {
        return computerScore;
    }

    public int getHumanScore()
    {
        return humanScore;
    }

    public void addHumanScore(int addPoints)
    {
        this.humanScore+=addPoints;
    }

    public void addComputerScore(int addPoints)
    {
        this.computerScore+=addPoints;
    }

    public void setAwardedPoints(int points)
    {
        this.awardedPoints = points;
    }

    public int getAwardedPoints()
    {
        return this.awardedPoints;
    }

}
