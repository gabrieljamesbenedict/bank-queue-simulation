package org.gjbmloslos.bankqueuesim.entity.customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.gjbmloslos.bankqueuesim.SimulationController;
import org.gjbmloslos.bankqueuesim.entity.interval.Interval;

import java.util.ArrayList;
import java.util.concurrent.*;

import static org.gjbmloslos.bankqueuesim.SimulationController.speed;
import static org.gjbmloslos.bankqueuesim.SimulationController.time;

public class CustomerSpawnManager {

    private ArrayList<Customer> costumerBufferList;
    private Interval spawnInterval;

    private ScheduledExecutorService customerSpawnService;
    Runnable bufferCustomer;

    public CustomerSpawnManager(ArrayList<Customer> costumerBufferList, Interval spawnInterval) {
        this.costumerBufferList = costumerBufferList;
        this.spawnInterval = spawnInterval;

        customerSpawnService = Executors.newSingleThreadScheduledExecutor();
    }

    private String timestamp () {
        return " @" + time + "s";
    }

    public void startCustomerSpawnService () {
        System.out.println("Started Customer Spawn Service. Interval: " + spawnInterval.getTimeInterval() + "s" + " " + timestamp());
        spawnCustomer(0);
    }

    private void spawnCustomer (int id) {
        if (customerSpawnService.isShutdown()) return;

        int delayInterval = (spawnInterval.getNextCustomerArrivalTime()*1000) / speed;

        bufferCustomer = () -> {
            Customer c = new Customer(id, null, null);
            Label l = new Label("Customer"+c.getId());
            l.setAlignment(Pos.CENTER);
            l.setMinSize(100, 45);
            l.setMaxSize(100, 45);
            l.setPadding(new Insets(5));
            //l.setFont(new Font(10));
            l.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(10), Insets.EMPTY)));
            c.setLabelRef(l);
            costumerBufferList.add(c);
            System.out.println("Spawned Customer" + c.getId() + " " + timestamp());
            spawnCustomer(id + 1);
        };

        Runnable spawnCustomerTask = () -> {
            customerSpawnService.submit(bufferCustomer);
        };

        //System.out.println("Delay: " + delayInterval + "ms");
        customerSpawnService.schedule(spawnCustomerTask, delayInterval, TimeUnit.MILLISECONDS);
    }

    public void endCustomerSpawnService () {
        customerSpawnService.shutdownNow();
    }

}
