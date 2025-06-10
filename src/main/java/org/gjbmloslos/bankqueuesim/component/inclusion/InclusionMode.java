package org.gjbmloslos.bankqueuesim.component.inclusion;

import org.gjbmloslos.bankqueuesim.entity.bank.BankTeller;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import static org.gjbmloslos.bankqueuesim.SimulationController.time;

public abstract class InclusionMode {

    ArrayList<BankTeller> bankTellerList;
    ArrayList<CustomerQueue> customerQueueList;
    String toolTip;

    Semaphore bankTellerGuard = new Semaphore(1);

    String timestamp() {
        return " @" + time + "s";
    }

    String log(Customer c, BankTeller bt) {
        return "Started Customer" + c.getId() + " with Service:" + c.getService().getServiceName() + " at BankTeller" + bt.getId() + " from Queue" + c.getCustomerQueue().getId() + " " + timestamp();
    }

    public InclusionMode () {}

    public ArrayList<BankTeller> getBankTellerList() {
        return bankTellerList;
    }

    public void setBankTellerList(ArrayList<BankTeller> costumerBufferList) {
        this.bankTellerList = costumerBufferList;
    }

    public ArrayList<CustomerQueue> getCustomerQueueList() {
        return customerQueueList;
    }

    public void setCustomerQueueList(ArrayList<CustomerQueue> customerQueueList) {
        this.customerQueueList = customerQueueList;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public abstract String getMode();
    public abstract void assignCustomerToTeller (ArrayList<BankTeller> availableBankTellerList);
}
