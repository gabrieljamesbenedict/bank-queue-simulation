package org.gjbmloslos.bankqueuesim.manager.customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.component.interval.IntervalMode;

import java.util.ArrayList;
import java.util.concurrent.*;

import static org.gjbmloslos.bankqueuesim.SimulationController.speed;
import static org.gjbmloslos.bankqueuesim.SimulationController.time;

public class CustomerSpawnManager {

    private final ArrayList<Customer> costumerBufferList;
    private final IntervalMode spawnIntervalMode;
    private final ArrayList<BankService> bankServiceList;

    private final ArrayList<Customer> customerMasterList;

    private final ScheduledExecutorService customerSpawnManagerService;
    Runnable bufferCustomer;

    public CustomerSpawnManager(ArrayList<Customer> costumerBufferList, IntervalMode spawnIntervalMode, ArrayList<BankService> bankServiceList) {
        this.costumerBufferList = costumerBufferList;
        this.spawnIntervalMode = spawnIntervalMode;
        this.bankServiceList = bankServiceList;

        customerMasterList = new ArrayList<>();
        customerSpawnManagerService = Executors.newSingleThreadScheduledExecutor();
    }

    private String timestamp () {
        return " @" + time + "s";
    }

    public void startCustomerSpawnService () {
        System.out.println("Started Customer Spawn Service. Interval: " + spawnIntervalMode.getTimeInterval() + "s" + " " + timestamp());
        spawnCustomer(0);
    }

    private void spawnCustomer (int id) {
        if (customerSpawnManagerService.isShutdown()) return;

        int delayInterval = (spawnIntervalMode.getNextCustomerArrivalTime()*1000) / speed;

        bufferCustomer = () -> {
            Customer c = new Customer(id);
            BankService bs = BankService.getRandomBankService(bankServiceList);
            c.setService(bs);
            Label l = new Label();
            l.setAlignment(Pos.CENTER);
            l.setTextAlignment(TextAlignment.CENTER);
            l.setMinSize(125, 45);
            l.setMaxSize(125, 45);
            l.setPadding(new Insets(5));
            l.setText(c.toString());
            l.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(10), Insets.EMPTY)));
            c.setLabelRef(l);
            costumerBufferList.add(c);
            customerMasterList.add(c);
            System.out.println("Spawned Customer" + c.getId() + " with Service:" + bs.getServiceName() + " after " + delayInterval + "ms " + timestamp());
            spawnCustomer(id + 1);
        };

        Runnable spawnCustomerTask = () -> {
            customerSpawnManagerService.submit(bufferCustomer);
        };

        //System.out.println("Delay: " + delayInterval + "ms");
        customerSpawnManagerService.schedule(spawnCustomerTask, (id == 0)? 0 : delayInterval, TimeUnit.MILLISECONDS);
    }

    public void endCustomerSpawnService () {
        customerSpawnManagerService.shutdownNow();
    }

    public ArrayList<Customer> getCustomerMasterList() {
        return customerMasterList;
    }
}
