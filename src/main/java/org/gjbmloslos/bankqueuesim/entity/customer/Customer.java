package org.gjbmloslos.bankqueuesim.entity.customer;

import javafx.scene.control.Label;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;

public class Customer {

    int id;

    BankService service;
    Label labelRef;
    CustomerQueue customerQueue;

    int remainingDuration;
    boolean processing;
    boolean completed;

    int waitingTime;
    int turnAroundTime;

    public Customer(int id) {
        this.id = id;

        processing = false;
        completed = false;
        waitingTime = 0;
        turnAroundTime = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BankService getService() {
        return service;
    }

    public void setService(BankService service) {
        this.service = service;
        remainingDuration = this.service.getServiceDuration();
    }

    public Label getLabelRef() {
        return labelRef;
    }

    public void setLabelRef(Label labelRef) {
        this.labelRef = labelRef;
    }

    public CustomerQueue getCustomerQueue() {
        return customerQueue;
    }

    public void setCustomerQueue(CustomerQueue customerQueue) {
        this.customerQueue = customerQueue;
    }

    public int getRemainingDuration() {
        return remainingDuration;
    }

    public void setRemainingDuration(int remainingDuration) {
        this.remainingDuration = remainingDuration;
    }

    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    @Override
    public String toString() {
        return "Customer"+id+"\n"+service.getServiceName() + "-" + remainingDuration +"s";
    }
}
