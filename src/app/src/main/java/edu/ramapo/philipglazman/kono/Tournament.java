package edu.ramapo.philipglazman.kono;

/**
 * Created by mango on 3/27/18.
 */

public class Tournament {

    private int roundNum;
    private int computerScore;
    private int humanScore;
    private int awardedPoints;

    // New tournament.
    public Tournament()
    {
        roundNum = 0;
        computerScore = 0;
        humanScore = 0;
    }

    // Loads existing tournament.
    public Tournament(int roundNum, int computerScore, int humanScore)
    {
        this.roundNum = roundNum;
        this.computerScore = computerScore;
        this.humanScore = humanScore;
    }

    /**
     * Setter for human score
     * @param humanScore
     */
    public void setHumanScore(int humanScore)
    {
        this.humanScore=humanScore;
    }

    /**
     * Setter for computer score.
     * @param computerScore
     */
    public void setComputerScore(int computerScore)
    {
        this.computerScore=computerScore;
    }

    /**
     * Subtracts number from human score.
     * @param numToSubtract
     */
    public void subtractHumanScore(int numToSubtract)
    {
        this.humanScore-=numToSubtract;
    }

    /**
     * Subtracts number from computer score.
     * @param numToSubtract
     */
    public void subtractComputerScore(int numToSubtract)
    {
        this.computerScore-=numToSubtract;
    }

    /**
     * Getter for round number.
     * @return roundNum, integer.
     */
    public int getRoundNum()
    {
        return roundNum;
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
     * Getter for human score.
     * @return humanScore, integer.
     */
    public int getHumanScore()
    {
        return humanScore;
    }

    /**
     * Adds to the human score.
     * @param addPoints  number of points to add to human score.
     */
    public void addHumanScore(int addPoints)
    {
        this.humanScore+=addPoints;
    }

    /**
     * Adds to computer score.
     * @param addPoints number of points to add to computer score.
     */
    public void addComputerScore(int addPoints)
    {
        this.computerScore+=addPoints;
    }

    /**
     * Sets the difference in points from the round scores to be awarded to the winner.
     * @param points
     */
    public void setAwardedPoints(int points)
    {
        this.awardedPoints = points;
    }

    /**
     * Getter for award points.
     * @return award points, integer.
     */
    public int getAwardedPoints()
    {
        return this.awardedPoints;
    }
}
