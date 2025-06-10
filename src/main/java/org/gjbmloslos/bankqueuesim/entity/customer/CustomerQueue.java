package org.gjbmloslos.bankqueuesim.entity.customer;

import javafx.scene.layout.VBox;

import java.util.Queue;

public class CustomerQueue {

    int id;
    Queue<Customer> customerQueue;
    VBox customerQueueContianer;

    public CustomerQueue(int id, Queue<Customer> customerQueue, VBox customerQueueContianer) {
        this.id = id;
        this.customerQueue = customerQueue;
        this.customerQueueContianer = customerQueueContianer;
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

    public VBox getCustomerQueueContianer() {
        return customerQueueContianer;
    }

    public void setCustomerQueueContianer(VBox customerQueueContianer) {
        this.customerQueueContianer = customerQueueContianer;
    }
}
