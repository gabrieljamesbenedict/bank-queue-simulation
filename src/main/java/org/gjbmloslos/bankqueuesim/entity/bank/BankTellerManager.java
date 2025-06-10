package org.gjbmloslos.bankqueuesim.entity.bank;

import javafx.application.Platform;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.gjbmloslos.bankqueuesim.SimulationController.speed;
import static org.gjbmloslos.bankqueuesim.SimulationController.time;

public class BankTellerManager {

    ArrayList<BankTeller> bankTellerList;
    ArrayList<CustomerQueue> customerQueueList;

    ArrayList<BankTeller> availableBankTellerList;

    ScheduledThreadPoolExecutor bankTellerManagerService;
    Runnable manageBankTellerTask;

    public BankTellerManager(ArrayList<BankTeller> bankTellerList, ArrayList<CustomerQueue> customerQueueList) {
        this.bankTellerList = bankTellerList;
        this.customerQueueList = customerQueueList;

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

            BankTeller bt = availableBankTellerList
                    .stream()
                    .filter(e -> !e.busy)
                    .toList()
                    .getFirst();

            Iterator<CustomerQueue> cqi = customerQueueList.iterator();
            while (cqi.hasNext()) {
                CustomerQueue cq = cqi.next();
                if (cq.getCustomerQueue().isEmpty()) continue;
                Customer c = cq.getCustomerQueue().remove();
                bt.setCurrentCustomer(c);
                Platform.runLater(() -> bt.getTellerBox().getChildren().set(1, c.getLabelRef()));
                bt.setBusy(true);
                System.out.println("Added Customer" + c.getId() + " from CustomerQueue" + c.getCustomerQueue().getId() + " to BankTeller" + bt.getId());
            }
        };

        Runnable processCostumerAtTeller = () -> {
            List<BankTeller> busyBankTellerList = bankTellerList.stream().filter(BankTeller::isBusy).toList();
            System.out.println(busyBankTellerList.toString());
            if (busyBankTellerList.isEmpty()) return;
            Iterator<BankTeller> bti = busyBankTellerList.iterator();
            while (bti.hasNext()) {
                BankTeller bt = bti.next();
                Customer c = bt.getCurrentCustomer();
                int reducedDuration = c.getDuration() - 1;
                c.setDuration(reducedDuration);
                System.out.println("Customer" + c.getId() + " has " + reducedDuration + "ms left.");
                if (reducedDuration <= 0) {
                    System.out.println("Customer"+c.getId() + " has completed Service:" + c.getService().getServiceName());
                    c = null; // Completely destroy costumer
                    Platform.runLater(bt::defaultCustomerLabel);
                    bt.setBusy(false);
                    availableBankTellerList.add(bt);
                }
            }
        };

        bankTellerManagerService.scheduleWithFixedDelay(addCustomerToTeller, 0, 10, TimeUnit.MILLISECONDS);
        bankTellerManagerService.scheduleWithFixedDelay(processCostumerAtTeller, 0, (1000/speed), TimeUnit.MILLISECONDS);
    }

    public void endBankTellerManageService () {
        bankTellerManagerService.shutdownNow();
    }
}
