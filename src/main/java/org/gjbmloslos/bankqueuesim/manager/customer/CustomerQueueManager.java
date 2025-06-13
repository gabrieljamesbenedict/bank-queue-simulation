package org.gjbmloslos.bankqueuesim.manager.customer;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.gjbmloslos.bankqueuesim.component.inclusion.InclusionMode;
import org.gjbmloslos.bankqueuesim.component.inclusion.InclusiveMode;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.gjbmloslos.bankqueuesim.SimulationController.time;

public class CustomerQueueManager {

    ArrayList<Customer> costumerBufferList;
    ArrayList<CustomerQueue> customerQueueList;

    ScheduledExecutorService customerQueueService;

    Runnable addCustomerToQueueTask = () -> {
        CustomerQueue cq = customerQueueList
                .stream()
                .sorted(Comparator.comparingInt(a -> a.getCustomerQueue().size()))
                .toList()
                .getFirst();
        Customer c = costumerBufferList.removeFirst();
        cq.getCustomerQueue().add(c);
        c.setCustomerQueue(cq);
        Platform.runLater(() -> cq.getCustomerQueueContainer().getChildren().add(c.getLabelRef()));
        System.out.println("Added Customer" + c.getId() + " to CustomerQueue" + cq.getId() + " " + timestamp());
        //System.out.println("CustomerQueue" + cq.getId() + ": " + cq.getCustomerQueue().stream().map(Customer::getId).toList());
    };

    Runnable cleanQueueTask = () -> {
        try {
            Iterator<CustomerQueue> cqi = customerQueueList.iterator();
            while (cqi.hasNext()) {
                CustomerQueue cq = cqi.next();
                Queue<Customer> q = cq.getCustomerQueue();
                VBox qc = cq.getCustomerQueueContainer();
                Platform.runLater(() -> {
                    //System.out.println("HELLO WORLD");
                    q.stream().filter(e -> e.getRemainingDuration() <= 0).forEach(q::remove);
                    qc.getChildren().stream().map(e -> (Label)e).filter(e -> e.getText().contains("-0s")).forEach(e -> qc.getChildren().remove(e));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    };

    public CustomerQueueManager(ArrayList<Customer> costumerBufferList, ArrayList<CustomerQueue> customerQueueList) {
        this.costumerBufferList = costumerBufferList;
        this.customerQueueList = customerQueueList;

        customerQueueService = Executors.newScheduledThreadPool(2);
    }

    private String timestamp () {
        return " @" + time + "s";
    }

    public void startCustomerQueueService () {
        System.out.println("Started Customer Queue Service" + " " + timestamp());

        Runnable queueManagerTask = () -> {
            if (!costumerBufferList.isEmpty()) {
                customerQueueService.submit(addCustomerToQueueTask);
            }
        };

        customerQueueService.scheduleWithFixedDelay(queueManagerTask, 0, 10, TimeUnit.MILLISECONDS);
        //customerQueueService.scheduleWithFixedDelay(cleanQueueTask, 0, 5, TimeUnit.SECONDS);
    }

    public void endCustomerQueueService () {
        customerQueueService.shutdownNow();
    }
}
