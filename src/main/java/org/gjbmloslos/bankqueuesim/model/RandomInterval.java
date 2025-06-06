package org.gjbmloslos.bankqueuesim.model;

import java.util.Random;

public class RandomInterval implements Interval {

    int intervalMin, intervalMax;
    Random rand;

    public RandomInterval(int intervalMin, int intervalMax) {
        this.intervalMin = intervalMin;
        this.intervalMax = intervalMax;
    }

    @Override
    public String getMode() {
        return "Random";
    }

    @Override
    public String getTimeInterval() {
        return Integer.toString(intervalMin)  + " - " + Integer.toString(intervalMax);
    }

    @Override
    public int getNextCustomerArrivalTime() {
        int nextCustomerArrivalTime = rand.nextInt(intervalMin, intervalMax);
        return nextCustomerArrivalTime;
    }
}
