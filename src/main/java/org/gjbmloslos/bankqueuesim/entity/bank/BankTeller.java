package org.gjbmloslos.bankqueuesim.entity.bank;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;

public class BankTeller {

    int id;
    VBox tellerBox;

    Customer currentCustomer;
    boolean busy;

    public BankTeller(int id, VBox tellerBox) {
        this.id = id;
        this.tellerBox = tellerBox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VBox getTellerBox() {
        return tellerBox;
    }

    public void setTellerBox(VBox tellerBox) {
        this.tellerBox = tellerBox;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
