package org.gjbmloslos.bankqueuesim.entity.customer;

import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.gjbmloslos.bankqueuesim.SimulationController.time;

public class CustomerQueueManager {

    ArrayList<Customer> costumerBufferList;
    ArrayList<CustomerQueue> customerQueueList;

    ScheduledExecutorService customerQueueService;
    Runnable manageQueueTask = () -> {
        CustomerQueue cq = customerQueueList
                .stream()
                .sorted(Comparator.comparingInt(a -> a.getCustomerQueue().size()))
                .toList()
                .getFirst();
        Customer c = costumerBufferList.removeFirst();
        cq.getCustomerQueue().add(c);
        Platform.runLater(() -> cq.getCustomerQueueContianer().getChildren().add(c.getLabelRef()));
        System.out.println("Added Customer" + c.getId() + " to CustomerQueue" + cq.getId());
        //System.out.println("CustomerQueue" + cq.getId() + ": " + cq.getCustomerQueue().stream().map(Customer::getId).toList());
    };

    public CustomerQueueManager(ArrayList<Customer> costumerBufferList, ArrayList<CustomerQueue> customerQueueList) {
        this.costumerBufferList = costumerBufferList;
        this.customerQueueList = customerQueueList;

        customerQueueService = Executors.newSingleThreadScheduledExecutor();
    }

    private String timestamp () {
        return " @" + time + "s";
    }

    public void startCustomerQueueService () {
        System.out.println("Started Customer Queue Service" + " " + timestamp());

        Runnable queueManagerTask = () -> {
            if (!costumerBufferList.isEmpty()) {
                customerQueueService.submit(manageQueueTask);
            }
        };
        customerQueueService.scheduleWithFixedDelay(queueManagerTask, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void endCustomerQueueService () {
        customerQueueService.shutdownNow();
    }
}
