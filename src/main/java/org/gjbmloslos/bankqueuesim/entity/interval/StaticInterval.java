package org.gjbmloslos.bankqueuesim.entity.interval;

public class StaticInterval implements Interval {

    int interval;

    public StaticInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public String getMode() {
        return "Static";
    }

    @Override
    public String getTimeInterval() {
        return Integer.toString(interval);
    }

    @Override
    public int getNextCustomerArrivalTime() {
        return interval;
    }
}
