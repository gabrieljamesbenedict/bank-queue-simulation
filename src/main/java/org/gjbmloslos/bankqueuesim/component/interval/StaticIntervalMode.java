package org.gjbmloslos.bankqueuesim.component.interval;

public class StaticIntervalMode implements IntervalMode {

    int interval;

    public StaticIntervalMode(int interval) {
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
