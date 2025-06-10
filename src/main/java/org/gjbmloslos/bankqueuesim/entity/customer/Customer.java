package org.gjbmloslos.bankqueuesim.entity.customer;

import javafx.scene.control.Label;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;

public class Customer {

    int id;

    BankService service;
    Label labelRef;
    CustomerQueue customerQueue;

    int duration;

    public Customer(int id) {
        this.id = id;
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
        duration = this.service.getServiceDuration();
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Customer"+id+"\n"+service.getServiceName() + "-" + service.getServiceDuration()+"s";
    }
}
