package org.gjbmloslos.bankqueuesim;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.gjbmloslos.bankqueuesim.component.inclusion.InclusionMode;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;
import org.gjbmloslos.bankqueuesim.manager.bank.BankTellerManager;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;
import org.gjbmloslos.bankqueuesim.manager.customer.CustomerSpawnManager;
import org.gjbmloslos.bankqueuesim.component.interval.IntervalMode;
import org.gjbmloslos.bankqueuesim.manager.customer.CustomerQueueManager;
import org.gjbmloslos.bankqueuesim.entity.bank.BankTeller;
import org.gjbmloslos.bankqueuesim.simulation.SimulationStatistics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulationController {

    public static int time;
    public static int actualTime;
    public static int speed;

    int tellerAmount, queueAmount, simulationTime;
    IntervalMode customerIntervalMode;
    InclusionMode bankTellerInclusionMode;
    ArrayList<BankService> bankServiceList;

    @FXML Text simulationStatus;
    @FXML Text textMaxSimTime;
    @FXML Text textElapsedSimTime;
    @FXML Text textActualElapsedTime;
    @FXML Text textSimulationSpeed;
    @FXML Text textTellerAmount;
    @FXML Text textQueueAmount;
    @FXML Text textInclusionMode;
    @FXML Text textIntervalMode;
    @FXML Text textIntervalTime;

    @FXML ListView<String> lvBankService;

    @FXML Text textCustomersCompleted;
    @FXML Text textCustomersTotal;
    @FXML Text textCompletionRate   ;
    @FXML Text textAverageWaitingTime;
    @FXML Text textAverageTurnAroundTime;


    @FXML HBox tellerRow;
    @FXML HBox queueRow;

    ScheduledExecutorService timeRunnerService;
    Runnable timeRunner;
    Runnable actualTimeRunner;

    CustomerSpawnManager customerSpawnManager;
    CustomerQueueManager customerQueueManager;
    BankTellerManager bankTellerManager;

    ArrayList<Customer> customerBufferList;
    ArrayList<CustomerQueue> customerQueueList;
    ArrayList<BankTeller> bankTellerList;

    SimulationStatistics stats;

    private String timestamp () {
        return " @" + time + "s";
    }

    @FXML public void initialize () {

        time = 0;
        actualTime = 0;
        speed = Simulation.configuration.getSimulationSpeed();

        tellerAmount = Simulation.configuration.getTellerAmount();
        queueAmount = Simulation.configuration.getQueueAmount();
        bankTellerInclusionMode = Simulation.configuration.getInclusionMode();
        simulationTime = Simulation.configuration.getSimulationTime();
        customerIntervalMode = Simulation.configuration.getIntervalMode();
        bankServiceList = Simulation.configuration.getBankServiceList();

        textMaxSimTime.setText(Integer.toString(simulationTime));
        textElapsedSimTime.setText(Integer.toString(time));
        textElapsedSimTime.setText(Integer.toString(time));
        textActualElapsedTime.setText(Integer.toString(actualTime));
        textSimulationSpeed.setText(Integer.toString(speed));
        textTellerAmount.setText(Integer.toString(tellerAmount));
        textQueueAmount.setText(Integer.toString(queueAmount));
        textInclusionMode.setText(bankTellerInclusionMode.getMode());
        textIntervalMode.setText(customerIntervalMode.getMode());
        textIntervalTime.setText(customerIntervalMode.getTimeInterval());

        List<String> serviceList = bankServiceList.stream().map(e -> {
            return e.getServiceName() + " - " + e.getServiceDuration() + "s";
        }).toList();
        lvBankService.getItems().addAll(serviceList);

        bankTellerList = new ArrayList<>();
        for (int i = 0; i < tellerAmount; i++) {
            VBox tellerBox = new VBox();
            tellerBox.setSpacing(10);
            tellerBox.setMinSize(125, 100);
            tellerBox.setMaxSize(125, 100);
            tellerBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, new CornerRadii(15), Insets.EMPTY)));
            BankTeller bt = new BankTeller(i, tellerBox);
            bankTellerList.add(bt);
            tellerRow.getChildren().add(tellerBox);
        }

        customerQueueList = new ArrayList<>();
        for (int i = 0; i < queueAmount; i++) {
            VBox customerLane = new VBox();
            customerLane.setSpacing(5);
            customerLane.setMinWidth(125);
            customerLane.setMaxWidth(125);
            customerLane.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, new CornerRadii(15), Insets.EMPTY)));
            queueRow.getChildren().add(customerLane);
            CustomerQueue cq = new CustomerQueue(i, new ArrayDeque<>(), customerLane);
            customerQueueList.add(cq);
        }

        customerBufferList = new ArrayList<>();

        timeRunnerService = Executors.newSingleThreadScheduledExecutor();

        customerSpawnManager = new CustomerSpawnManager(customerBufferList, customerIntervalMode, bankServiceList);
        customerQueueManager = new CustomerQueueManager(customerBufferList, customerQueueList);
        bankTellerManager = new BankTellerManager(bankTellerList, bankTellerInclusionMode, customerQueueList);

        simulationTime += 1; // sim time padding

        stats = new SimulationStatistics(
                textCustomersCompleted,
                textCustomersTotal,
                textCompletionRate,
                textAverageWaitingTime,
                textAverageTurnAroundTime);

    }

    @FXML public void begin () {
        simulationStatus.setText("Simulation Running");

        customerSpawnManager.startCustomerSpawnService();
        customerQueueManager.startCustomerQueueService();
        bankTellerManager.startBankTellerManageService();

        stats.attachCustomerStatistics(customerSpawnManager, bankTellerManager);

        timeRunner = () -> {
            if (time >= simulationTime) {
                System.out.println("Simulation maximum time has been reached");
                Platform.runLater(() -> {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Simulation maximum time has been reached", ButtonType.OK);
                    a.show();
                });
                end(false);
                timeRunnerService.shutdown();
            }
            stats.calculateStatistics();
            stats.calculateThroughput();
            Platform.runLater(() -> textElapsedSimTime.setText(Integer.toString(time)));
            time++;
        };

        actualTimeRunner = () -> {
            Platform.runLater(() -> textActualElapsedTime.setText(Integer.toString(actualTime)));
            actualTime++;
        };

        timeRunnerService.scheduleWithFixedDelay(actualTimeRunner, 0, 1, TimeUnit.SECONDS);
        timeRunnerService.scheduleWithFixedDelay(timeRunner, 0, (1000/speed), TimeUnit.MILLISECONDS);
    }

    @FXML public void pause () {

    }

    @FXML public void end () {
        end(true);
    }

    public void end (boolean endedEarly) {
        if (endedEarly) {
            System.out.println("Simulation has ended early" + timestamp());
        } else {
            System.out.println("Simulation has ended" + timestamp());
        }
        customerSpawnManager.endCustomerSpawnService();
        customerQueueManager.endCustomerQueueService();
        bankTellerManager.endBankTellerManageService();
        timeRunnerService.shutdownNow();
    }
}
