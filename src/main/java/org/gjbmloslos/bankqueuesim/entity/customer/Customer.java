package org.gjbmloslos.bankqueuesim.entity.customer;

import javafx.scene.control.Label;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;

public class Customer {

    int id;
    BankService service;
    Label labelRef;

    public Customer(int id, BankService service, Label labelRef) {
        this.id = id;
        this.service = service;
        this.labelRef = labelRef;
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
    }

    public Label getLabelRef() {
        return labelRef;
    }

    public void setLabelRef(Label labelRef) {
        this.labelRef = labelRef;
    }

    @Override
    public String toString() {
        return "Customer"+id+"\n"+service.getServiceName() + "-" + service.getServiceDuration()+"s";
    }
}
