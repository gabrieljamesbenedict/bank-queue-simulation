package org.gjbmloslos.bankqueuesim;

import org.gjbmloslos.bankqueuesim.component.inclusion.InclusionMode;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;
import org.gjbmloslos.bankqueuesim.component.interval.IntervalMode;

import java.util.ArrayList;

public class SimulationConfiguration {

    int tellerAmount, queueAmount, simulationTime, simulationSpeed;
    InclusionMode inclusionMode;
    IntervalMode intervalMode;
    ArrayList<BankService> bankServiceList;

    public SimulationConfiguration(int tellerAmount, int queueAmount, int simulationTime, int simulationSpeed, InclusionMode inclusionMode, IntervalMode intervalMode, ArrayList<BankService> bankServiceList) {
        this.tellerAmount = tellerAmount;
        this.queueAmount = queueAmount;
        this.simulationTime = simulationTime;
        this.simulationSpeed = simulationSpeed;
        this.inclusionMode = inclusionMode;
        this.intervalMode = intervalMode;
        this.bankServiceList = bankServiceList;
    }

    public ArrayList<BankService> getBankServiceList() {
        return bankServiceList;
    }

    public void setBankServiceList(ArrayList<BankService> bankServiceList) {
        this.bankServiceList = bankServiceList;
    }

    public IntervalMode getIntervalMode() {
        return intervalMode;
    }

    public void setIntervalMode(IntervalMode intervalMode) {
        this.intervalMode = intervalMode;
    }

    public InclusionMode getInclusionMode() {
        return inclusionMode;
    }

    public void setInclusionMode(InclusionMode inclusionMode) {
        this.inclusionMode = inclusionMode;
    }

    public int getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(int simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }

    public int getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }

    public int getQueueAmount() {
        return queueAmount;
    }

    public void setQueueAmount(int queueAmount) {
        this.queueAmount = queueAmount;
    }

    public int getTellerAmount() {
        return tellerAmount;
    }

    public void setTellerAmount(int tellerAmount) {
        this.tellerAmount = tellerAmount;
    }
}
