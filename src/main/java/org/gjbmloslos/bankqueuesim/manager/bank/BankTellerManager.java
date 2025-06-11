package org.gjbmloslos.bankqueuesim.manager.bank;

import javafx.application.Platform;
import org.gjbmloslos.bankqueuesim.component.inclusion.InclusionMode;
import org.gjbmloslos.bankqueuesim.entity.bank.BankTeller;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.gjbmloslos.bankqueuesim.SimulationController.speed;
import static org.gjbmloslos.bankqueuesim.SimulationController.time;

public class BankTellerManager {

    int completedCustomers;

    ArrayList<BankTeller> bankTellerList;
    ArrayList<CustomerQueue> customerQueueList;
    InclusionMode bankTellerInclusionMode;

    ArrayList<BankTeller> availableBankTellerList;
    ArrayList<Customer> tempCompletedCustomerList;
    Semaphore bankTellerGuard = new Semaphore(1);

    ScheduledThreadPoolExecutor bankTellerManagerService;
    Runnable manageBankTellerTask;

    public BankTellerManager(ArrayList<BankTeller> bankTellerList, InclusionMode bankTellerInclusionMode, ArrayList<CustomerQueue> customerQueueList) {
        this.bankTellerList = bankTellerList;
        this.customerQueueList = customerQueueList;
        this.bankTellerInclusionMode = bankTellerInclusionMode;

        bankTellerInclusionMode.setBankTellerList(bankTellerList);
        bankTellerInclusionMode.setCustomerQueueList(customerQueueList);
        completedCustomers = 0;
        availableBankTellerList = new ArrayList<>(bankTellerList);
        tempCompletedCustomerList = new ArrayList<>();
        bankTellerManagerService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(4);
    }

    private String timestamp () {
        return " @" + time + "s";
    }

    public void startBankTellerManageService () {
        System.out.println("Started Bank Teller Manager Service" + " " + timestamp());

        Runnable addCustomerToTeller = () -> {
            bankTellerInclusionMode.assignCustomerToTeller(availableBankTellerList);
        };

        Runnable processCostumerAtTeller = () -> {
            List<BankTeller> busyBankTellerList = bankTellerList.stream().filter(BankTeller::isBusy).toList();
            if (busyBankTellerList.isEmpty()) return;

            Iterator<BankTeller> bti = busyBankTellerList.iterator();
            while (bti.hasNext()) {
                BankTeller bt = bti.next();
                Customer c = bt.getCurrentCustomer();
                int reducedDuration = c.getRemainingDuration() - 1;
                c.setRemainingDuration(reducedDuration);
                Customer finalC = c;
                Platform.runLater(() -> finalC.getLabelRef().setText(finalC.toString()));
                if (reducedDuration <= 0) {
                    try {
                        bankTellerGuard.acquire();
                        System.out.println("Customer"+c.getId() + " has completed Service:" + c.getService().getServiceName() + " " + timestamp());
                        setCompletedCustomers(getCompletedCustomers()+1);
                        //System.out.println("Completed: " + getCompletedCustomers());
                        c.setCompleted(true);
                        tempCompletedCustomerList.add(c);
                        Platform.runLater(bt::defaultCustomerLabel);
                        bt.setBusy(false);
                        availableBankTellerList.add(bt);
                        bankTellerGuard.release();
                    } catch (InterruptedException e) {e.printStackTrace();}
                }
            }

        };

        bankTellerManagerService.scheduleWithFixedDelay(addCustomerToTeller, 0, 1, TimeUnit.MILLISECONDS);
        bankTellerManagerService.scheduleWithFixedDelay(processCostumerAtTeller, 0, (1000/speed), TimeUnit.MILLISECONDS);
    }

    public void endBankTellerManageService () {
        System.out.println("Completed Customers: " + completedCustomers);
        bankTellerManagerService.shutdownNow();
    }

    public int getCompletedCustomers() {
        return completedCustomers;
    }

    public void setCompletedCustomers(int completedCustomers) {
        this.completedCustomers = completedCustomers;
    }

    public ArrayList<Customer> getTempCompletedCustomerList() {
        return tempCompletedCustomerList;
    }

    public void setTempCompletedCustomerList(ArrayList<Customer> tempCompletedCustomerList) {
        this.tempCompletedCustomerList = tempCompletedCustomerList;
    }
}
