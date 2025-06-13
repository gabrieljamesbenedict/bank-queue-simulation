package org.gjbmloslos.bankqueuesim.simulation;

import javafx.application.Platform;
import javafx.scene.text.Text;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.manager.bank.BankTellerManager;
import org.gjbmloslos.bankqueuesim.manager.customer.CustomerSpawnManager;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SimulationStatistics {

    Text textCustomersCompleted;
    Text textCustomersTotal;
    Text textCompletionRate   ;
    Text textAverageWaitingTime;
    Text textAverageTurnAroundTime;

    int customerCompleted, customerTotal;
    double completionRate, aveWaitingTime, aveTurnAroundTime;
    ArrayList<Integer> throughputHistory;

    ScheduledExecutorService statService;
    
    public SimulationStatistics(Text textCustomersCompleted, Text textCustomersTotal, Text textCompletionRate, Text textAverageWaitingTime, Text textAverageTurnAroundTime) {
        this.textCustomersCompleted = textCustomersCompleted;
        this.textCustomersTotal = textCustomersTotal;
        this.textCompletionRate = textCompletionRate;
        this.textAverageWaitingTime = textAverageWaitingTime;
        this.textAverageTurnAroundTime = textAverageTurnAroundTime;

        throughputHistory = new ArrayList<>();
        statService = Executors.newSingleThreadScheduledExecutor();
    }

    CustomerSpawnManager customerSpawnManager;
    BankTellerManager bankTellerManager;
    public void attachCustomerStatistics (CustomerSpawnManager customerSpawnManager, BankTellerManager bankTellerManager) {
        this.customerSpawnManager = customerSpawnManager;
        this.bankTellerManager = bankTellerManager;
    }

    Runnable customerTimeStatisticsTask;
    public void calculateStatistics() {
        customerTimeStatisticsTask = () -> {
            try {
                ArrayList<Customer> masterList = customerSpawnManager.getCustomerMasterList();
                customerTotal = masterList.size();
                for (Customer c : masterList) {
                    if (!c.isCompleted()) {
                        c.setTurnAroundTime(c.getTurnAroundTime()+1);
                    }
                    if (!c.isProcessing()) {
                        c.setWaitingTime(c.getWaitingTime()+1);
                    }
                }
                customerCompleted = bankTellerManager.getCompletedCustomers();

                aveWaitingTime = (double) masterList
                        .stream()
                        .map(Customer::getWaitingTime)
                        .reduce(Integer::sum)
                        .orElse(0) / masterList.size();

                aveTurnAroundTime = (double) masterList
                        .stream()
                        .map(Customer::getTurnAroundTime)
                        .reduce(Integer::sum)
                        .orElse(0) / masterList.size();

                Platform.runLater(() -> {
                    textCustomersTotal.setText(Integer.toString(customerTotal));
                    textCustomersCompleted.setText(Integer.toString(customerCompleted));
                    textAverageWaitingTime.setText(Double.toString((double)Math.round(aveWaitingTime*10)/10));
                    textAverageTurnAroundTime.setText(Double.toString((double)Math.round(aveTurnAroundTime*10)/10));
                });
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };

        statService.submit(customerTimeStatisticsTask);
    }

    final int THROUGHPUT_MAX_TIME_INTERVAL = 10;
    int throughputTimeIntervalTracker = 0;
    Runnable customerThroughputTask;
    public void calculateThroughput() {

        throughputTimeIntervalTracker += 1;
        if (throughputTimeIntervalTracker >= THROUGHPUT_MAX_TIME_INTERVAL) {
            customerThroughputTask = () -> {
                ArrayList<Customer> tempList = bankTellerManager.getTempCompletedCustomerList();
                int throughput = tempList.size();

                //System.out.println("THROUGHPUT: " + tempList.size() );
                throughputHistory.add(throughput);

                double aveThroughput = (double) throughputHistory
                        .stream()
                        .reduce(Integer::sum)
                        .get() / throughputHistory.size();

                Platform.runLater(() -> {
                    textCompletionRate.setText(Double.toString((double)Math.round(aveThroughput*10)/10) + " custumers/" + THROUGHPUT_MAX_TIME_INTERVAL + "s");
                });

                throughputTimeIntervalTracker = 0;
                bankTellerManager.getTempCompletedCustomerList().clear();
            };

            statService.submit(customerThroughputTask);
            throughputTimeIntervalTracker = 0;
        }

    }
}
