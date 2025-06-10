package org.gjbmloslos.bankqueuesim.entity.customer;

import javafx.scene.layout.VBox;

import java.util.Queue;

public class CustomerQueue {

    int id;
    Queue<Customer> customerQueue;
    VBox customerQueueContainer;

    public CustomerQueue(int id, Queue<Customer> customerQueue, VBox customerQueueContainer) {
        this.id = id;
        this.customerQueue = customerQueue;
        this.customerQueueContainer = customerQueueContainer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Queue<Customer> getCustomerQueue() {
        return customerQueue;
    }

    public void setCustomerQueue(Queue<Customer> customerQueue) {
        this.customerQueue = customerQueue;
    }

    public VBox getCustomerQueueContainer() {
        return customerQueueContainer;
    }

    public void setCustomerQueueContainer(VBox customerQueueContainer) {
        this.customerQueueContainer = customerQueueContainer;
    }
}
