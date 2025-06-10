package org.gjbmloslos.bankqueuesim.entity.customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;
import org.gjbmloslos.bankqueuesim.entity.interval.Interval;

import java.util.ArrayList;
import java.util.concurrent.*;

import static org.gjbmloslos.bankqueuesim.SimulationController.speed;
import static org.gjbmloslos.bankqueuesim.SimulationController.time;

public class CustomerSpawnManager {

    private ArrayList<Customer> costumerBufferList;
    private Interval spawnInterval;
    private ArrayList<BankService> bankServiceList;

    private ScheduledExecutorService customerSpawnManagerService;
    Runnable bufferCustomer;

    public CustomerSpawnManager(ArrayList<Customer> costumerBufferList, Interval spawnInterval, ArrayList<BankService> bankServiceList) {
        this.costumerBufferList = costumerBufferList;
        this.spawnInterval = spawnInterval;
        this.bankServiceList = bankServiceList;

        customerSpawnManagerService = Executors.newSingleThreadScheduledExecutor();
    }

    private String timestamp () {
        return " @" + time + "s";
    }

    public void startCustomerSpawnService () {
        System.out.println("Started Customer Spawn Service. Interval: " + spawnInterval.getTimeInterval() + "s" + " " + timestamp());
        spawnCustomer(0);
    }

    private void spawnCustomer (int id) {
        if (customerSpawnManagerService.isShutdown()) return;

        int delayInterval = (spawnInterval.getNextCustomerArrivalTime()*1000) / speed;

        bufferCustomer = () -> {
            try {
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
                System.out.println("Spawned Customer" + c.getId() + " with Service: " + bs.getServiceName() + timestamp());
                spawnCustomer(id + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

}
