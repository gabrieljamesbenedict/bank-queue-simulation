package org.gjbmloslos.bankqueuesim.entity.customer;

import org.gjbmloslos.bankqueuesim.entity.bank.BankService;

public class Customer {

    int id;
    BankService service;

    public Customer(int id, BankService service) {
        this.id = id;
        this.service = service;
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
}
