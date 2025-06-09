package org.gjbmloslos.bankqueuesim;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerSpawnManager;
import org.gjbmloslos.bankqueuesim.entity.interval.Interval;
import org.gjbmloslos.bankqueuesim.entity.queue.CustomerQueue;
import org.gjbmloslos.bankqueuesim.entity.queue.CustomerQueueManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulationController {

    public static int time;

    int tellerAmount, queueAmount, simulationTime;
    Boolean strictExclusivity;
    Interval customerInterval;
    ArrayList<BankService> bankServiceList;

    @FXML Text simulationStatus;
    @FXML Text textMaxSimTime;
    @FXML Text textElapsedSimTime;
    @FXML Text textTellerAmount;
    @FXML Text textQueueAmount;
    @FXML Text textStrictExclusivity;
    @FXML Text textIntervalMode;
    @FXML Text textIntervalTime;
    @FXML ListView<BankService> lvBankService;

    @FXML HBox tellerRow;
    @FXML HBox queueRow;

    ScheduledExecutorService timeRunnerService;
    Runnable timeRunner;

    CustomerSpawnManager customerSpawnManager;
    CustomerQueueManager customerQueueManager;

    ArrayList<CustomerQueue> customerQueueList;
    ArrayList<VBox> customerQueueContainerList;
    ArrayList<Customer> customerBufferList;


    @FXML public void initialize () {

        time = 0;

        tellerAmount = Simulation.configuration.getTellerAmount();
        queueAmount = Simulation.configuration.getQueueAmount();
        simulationTime = Simulation.configuration.getSimulationTime();
        strictExclusivity = Simulation.configuration.getStrictExclusivity();
        customerInterval = Simulation.configuration.getInterval();
        bankServiceList = Simulation.configuration.getBankServiceList();

        textMaxSimTime.setText(Integer.toString(simulationTime));
        textElapsedSimTime.setText(Integer.toString(time));
        textTellerAmount.setText(Integer.toString(tellerAmount));
        textQueueAmount.setText(Integer.toString(queueAmount));
        textStrictExclusivity.setText(Boolean.toString(strictExclusivity));
        textIntervalMode.setText(customerInterval.getMode());
        textIntervalTime.setText(customerInterval.getTimeInterval());
        lvBankService.getItems().addAll(bankServiceList);

        customerQueueContainerList = new ArrayList<>();
        customerQueueList = new ArrayList<>();
        for (int i = 0; i < tellerAmount; i++) {
            BorderPane teller = new BorderPane();
            teller.setPrefSize(50, 50);
            teller.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(5), Insets.EMPTY)));
            Label tellerIndex = new Label("Teller"+Integer.toString(i));
            teller.setCenter(tellerIndex);
            tellerRow.getChildren().add(teller);
        }
        for (int i = 0; i < queueAmount; i++) {
            VBox customerLane = new VBox();
            customerLane.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
            queueRow.getChildren().add(customerLane);
            customerQueueContainerList.add(customerLane);
            CustomerQueue cq = new CustomerQueue(new ArrayDeque<>(), customerLane);
            customerQueueList.add(cq);
        }

        customerBufferList = new ArrayList<>();

        timeRunnerService = Executors.newSingleThreadScheduledExecutor();

        customerSpawnManager = new CustomerSpawnManager(customerBufferList, customerInterval);
        customerQueueManager = new CustomerQueueManager(customerQueueList);
    }

    @FXML public void beginSimulation () {
        simulationStatus.setText("Simulation Running");

        customerSpawnManager.startCustomerSpawnService();

        timeRunner = () -> {
            if (time >= simulationTime) {
                System.out.println("Simulation maximum time has been reached");
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Simulation maximum time has been reached", ButtonType.OK);
                a.show();
                timeRunnerService.shutdown();
            }
            Platform.runLater(() -> textElapsedSimTime.setText(Integer.toString(time)));
            time++;
        };

        timeRunnerService.scheduleWithFixedDelay(timeRunner, 0, 1000, TimeUnit.MILLISECONDS);
    }

}
