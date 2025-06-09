package org.gjbmloslos.bankqueuesim.entity.queue;

import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;

import java.util.Queue;

public class CustomerQueue {

    Queue<Customer> customerQueue;
    VBox customerQueueContainer;

    public CustomerQueue(Queue<Customer> customerQueue, VBox customerQueueContainer) {
        this.customerQueue = customerQueue;
        this.customerQueueContainer = customerQueueContainer;
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
