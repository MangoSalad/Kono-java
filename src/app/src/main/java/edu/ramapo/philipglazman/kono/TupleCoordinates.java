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

    public TupleCoordinates(Pair<Integer,Integer> initialCoordiantes, Pair<Integer, Integer> finalCoordinates, String direction, String strategy)
    {
        this.initialCoordinates=initialCoordiantes;
        this.finalCoordinates=finalCoordinates;
        this.direction=direction;
        this.strategy=strategy;
    }

    public Pair<Integer,Integer> getInitialCoordinates()
    {
        return initialCoordinates;
    }

    public Pair<Integer,Integer> getFinalCoordinates()
    {
        return finalCoordinates;
    }

    public String getDirection()
    {
        return direction;
    }

    public String getStrategy()
    {
        return strategy;
    }

}
