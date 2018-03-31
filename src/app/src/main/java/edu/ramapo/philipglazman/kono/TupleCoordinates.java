package edu.ramapo.philipglazman.kono;

import android.util.Pair;

/**
 * Created by mango on 3/25/18.
 */

public class TupleCoordinates {

    private Pair<Integer, Integer> initialCoordinates;
    private Pair<Integer, Integer> finalCoordinates;
    private String direction;
    private String strategy;

    /**
     * Constructor for tuple.
     * @param initialCoordiantes
     * @param finalCoordinates
     * @param direction
     * @param strategy
     */
    public TupleCoordinates(Pair<Integer,Integer> initialCoordiantes, Pair<Integer, Integer> finalCoordinates, String direction, String strategy)
    {
        this.initialCoordinates=initialCoordiantes;
        this.finalCoordinates=finalCoordinates;
        this.direction=direction;
        this.strategy=strategy;
    }

    /**
     * Getter for returning pair of initial coordinates.
     * @return pair of row/column.
     */
    public Pair<Integer,Integer> getInitialCoordinates()
    {
        return initialCoordinates;
    }

    /**
     * Getter for returning pair of final coordinates.
     * @return pair of row/column.
     */
    public Pair<Integer,Integer> getFinalCoordinates()
    {
        return finalCoordinates;
    }

    /**
     * Getter for direction.
     * @return string holding direction.
     */
    public String getDirection()
    {
        return direction;
    }

    /**
     * Getter for strategy.
     * @return string holding strategy.
     */
    public String getStrategy()
    {
        return strategy;
    }

}
