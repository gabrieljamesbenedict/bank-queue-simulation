package org.gjbmloslos.bankqueuesim.entity.customer;

import org.gjbmloslos.bankqueuesim.entity.interval.Interval;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.*;

public class CustomerSpawnManager {

    static int costumerId = 0;
    ArrayList<Customer> costumerBufferList;
    Interval spawnInterval;

    ScheduledExecutorService spawnService = Executors.newSingleThreadScheduledExecutor();
    Runnable spawnCustomerTask = new Runnable() {
        @Override
        public void run() {
            costumerBufferList.add(new Customer(costumerId++, null));
        }
    };

    Timer customerSpawnTimer;

    public void startSpawnCustomerTask () {

    }

    private void spawnCustomer () {
        spawnService.schedule(spawnCustomerTask, spawnInterval.getNextCustomerArrivalTime(), TimeUnit.SECONDS);
    }


    public CustomerSpawnManager(ArrayList<Customer> costumerBufferList, Interval spawnInterval) {
        this.costumerBufferList = costumerBufferList;
        this.spawnInterval = spawnInterval;
    }

}
