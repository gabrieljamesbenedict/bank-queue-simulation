package org.gjbmloslos.bankqueuesim.model;

import java.util.ArrayList;

public class SimulationConfiguration {

    int tellerAmount, queueAmount, simulationTime;
    Boolean strictExclusivity;
    Interval interval;
    ArrayList<BankService> bankServiceList;

    public SimulationConfiguration(int tellerAmount, int queueAmount, int simulationTime, Boolean strictExclusivity, Interval interval, ArrayList<BankService> bankServiceList) {
        this.tellerAmount = tellerAmount;
        this.queueAmount = queueAmount;
        this.simulationTime = simulationTime;
        this.strictExclusivity = strictExclusivity;
        this.interval = interval;
        this.bankServiceList = bankServiceList;
    }

    public int getTellerAmount() {
        return tellerAmount;
    }

    public void setTellerAmount(int tellerAmount) {
        this.tellerAmount = tellerAmount;
    }

    public int getQueueAmount() {
        return queueAmount;
    }

    public void setQueueAmount(int queueAmount) {
        this.queueAmount = queueAmount;
    }

    public int getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }

    public Boolean getStrictExclusivity() {
        return strictExclusivity;
    }

    public void setStrictExclusivity(Boolean strictExclusivity) {
        this.strictExclusivity = strictExclusivity;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public ArrayList<BankService> getBankServiceList() {
        return bankServiceList;
    }

    public void setBankServiceList(ArrayList<BankService> bankServiceList) {
        this.bankServiceList = bankServiceList;
    }
}
