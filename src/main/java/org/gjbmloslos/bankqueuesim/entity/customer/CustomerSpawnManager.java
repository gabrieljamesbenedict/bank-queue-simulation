package org.gjbmloslos.bankqueuesim.entity.customer;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.gjbmloslos.bankqueuesim.SimulationController;
import org.gjbmloslos.bankqueuesim.entity.interval.Interval;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.*;

import static org.gjbmloslos.bankqueuesim.SimulationController.time;

public class CustomerSpawnManager {

    private final ScheduledExecutorService customerSpawnService = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledThreadPoolExecutor customerBufferService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    private ArrayList<Customer> costumerBufferList;
    private Interval spawnInterval;

    private Runnable bufferCustomer;

    public CustomerSpawnManager(ArrayList<Customer> costumerBufferList, Interval spawnInterval) {
        this.costumerBufferList = costumerBufferList;
        this.spawnInterval = spawnInterval;

        customerBufferService.setKeepAliveTime(1, TimeUnit.SECONDS);
        customerBufferService.allowCoreThreadTimeOut(true);
    }

    public void startCustomerSpawnService () {
        System.out.println("Started Customer Spawn Service. Interval: " + spawnInterval.getTimeInterval() + "s" + " @" + time + "s");
        spawnCustomer(0);
    }

    private void spawnCustomer (int id) {
        if (customerSpawnService.isShutdown()) return;
        Runnable spawnCustomerTask = () -> {
            bufferCustomer = () -> {
                Customer c = new Customer(id, null, null);
                Label l = new Label("Customer"+c.getId());
                l.setFont(new Font(10));
                l.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(10), Insets.EMPTY)));
                c.setLabelRef(l);
                costumerBufferList.add(c);
                System.out.println("Spawned Customer" + c.getId() + " @" + time + "s");
            };
            customerBufferService.submit(bufferCustomer);
            if (!customerSpawnService.isShutdown()) spawnCustomer(id+1);
        };
        customerSpawnService.schedule(spawnCustomerTask, spawnInterval.getNextCustomerArrivalTime(), TimeUnit.SECONDS);
    }

    public void endCustomerSpawnService () {
        customerSpawnService.shutdown();
        customerBufferService.shutdown();
    }

}
