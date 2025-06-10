package org.gjbmloslos.bankqueuesim.entity.bank;

import javafx.application.Platform;
import javafx.scene.control.Label;
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

    ArrayList<BankTeller> availableBankTellerList;

    Semaphore addCustomerTaskGuard = new Semaphore(1);
    Semaphore removeCustomerTaskGuard = new Semaphore(1);

    ScheduledThreadPoolExecutor bankTellerManagerService;
    Runnable manageBankTellerTask;

    public BankTellerManager(ArrayList<BankTeller> bankTellerList, ArrayList<CustomerQueue> customerQueueList) {
        this.bankTellerList = bankTellerList;
        this.customerQueueList = customerQueueList;

        completedCustomers = 0;
        availableBankTellerList = new ArrayList<>(bankTellerList);
        bankTellerManagerService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(4);
    }

    private String timestamp () {
        return " @" + time + "s";
    }

    public void startBankTellerManageService () {
        System.out.println("Started Bank Teller Manager Service" + " " + timestamp());

        Runnable addCustomerToTeller = () -> {
            if (availableBankTellerList.isEmpty()) return;

            try {
                addCustomerTaskGuard.acquire();
            } catch (InterruptedException e) {e.printStackTrace();}

            BankTeller bt = availableBankTellerList
                    .stream()
                    .filter(e -> !e.busy)
                    .toList()
                    .getFirst();

            addCustomerTaskGuard.release();

            Iterator<CustomerQueue> cqi = customerQueueList.iterator();
            while (cqi.hasNext()) {
                CustomerQueue cq = cqi.next();
                if (cq.getCustomerQueue().isEmpty()) continue;
                Customer c = cq.getCustomerQueue().remove();
                bt.setCurrentCustomer(c);
                Platform.runLater(() -> {
                    if (bt.getTellerBox().getChildren().size() > 1)
                        bt.getTellerBox().getChildren().set(1, c.getLabelRef());
                    else
                        bt.getTellerBox().getChildren().add(c.getLabelRef());

                });
                bt.setBusy(true);
                availableBankTellerList.remove(bt);
                System.out.println("Added Customer" + c.getId() + " from CustomerQueue" + c.getCustomerQueue().getId() + " to BankTeller" + bt.getId() + " " + timestamp());
            }
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
                        removeCustomerTaskGuard.acquire();
                        System.out.println("Customer"+c.getId() + " has completed Service:" + c.getService().getServiceName() + " " + timestamp());
                        completedCustomers++;
                        c = null; // Completely destroy the costumer (I'll probably make a completed list later to store all completed customers)
                        Platform.runLater(bt::defaultCustomerLabel);
                        bt.setBusy(false);
                        availableBankTellerList.add(bt);
                        removeCustomerTaskGuard.release();
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
}
