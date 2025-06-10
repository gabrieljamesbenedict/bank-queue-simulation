package org.gjbmloslos.bankqueuesim;

import org.gjbmloslos.bankqueuesim.entity.bank.BankService;
import org.gjbmloslos.bankqueuesim.entity.interval.Interval;

import java.util.ArrayList;

public class SimulationConfiguration {

    int tellerAmount, queueAmount, simulationTime, simulationSpeed;
    Boolean strictExclusivity;
    Interval interval;
    ArrayList<BankService> bankServiceList;

    public SimulationConfiguration(int tellerAmount, int queueAmount, int simulationTime, int simulationSpeed, Boolean strictExclusivity, Interval interval, ArrayList<BankService> bankServiceList) {
        this.tellerAmount = tellerAmount;
        this.queueAmount = queueAmount;
        this.simulationTime = simulationTime;
        this.simulationSpeed = simulationSpeed;
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

    public int getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(int simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
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
