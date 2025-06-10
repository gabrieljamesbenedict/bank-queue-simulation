package org.gjbmloslos.bankqueuesim;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerSpawnManager;
import org.gjbmloslos.bankqueuesim.entity.interval.Interval;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueueManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulationController {

    public static int time;
    public static int speed;

    int tellerAmount, queueAmount, simulationTime;
    Boolean strictExclusivity;
    Interval customerInterval;
    ArrayList<BankService> bankServiceList;

    @FXML Text simulationStatus;
    @FXML Text textMaxSimTime;
    @FXML Text textElapsedSimTime;
    @FXML Text textSimulationSpeed;
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

    ArrayList<Customer> customerBufferList;
    ArrayList<CustomerQueue> customerQueueList;

    private String timestamp () {
        return " @" + time + "s";
    }

    @FXML public void initialize () {

        time = 0;
        speed = Simulation.configuration.getSimulationSpeed();

        tellerAmount = Simulation.configuration.getTellerAmount();
        queueAmount = Simulation.configuration.getQueueAmount();
        simulationTime = Simulation.configuration.getSimulationTime();
        strictExclusivity = Simulation.configuration.getStrictExclusivity();
        customerInterval = Simulation.configuration.getInterval();
        bankServiceList = Simulation.configuration.getBankServiceList();

        textMaxSimTime.setText(Integer.toString(simulationTime));
        textElapsedSimTime.setText(Integer.toString(time));
        textSimulationSpeed.setText(Integer.toString(time));
        textTellerAmount.setText(Integer.toString(tellerAmount));
        textQueueAmount.setText(Integer.toString(queueAmount));
        textStrictExclusivity.setText(Boolean.toString(strictExclusivity));
        textIntervalMode.setText(customerInterval.getMode());
        textIntervalTime.setText(customerInterval.getTimeInterval());
        lvBankService.getItems().addAll(bankServiceList);


        for (int i = 0; i < tellerAmount; i++) {
            VBox tellerBox = new VBox();
            tellerBox.setSpacing(10);
            tellerBox.setMinSize(100, 100);
            tellerBox.setMaxSize(100, 100);
            tellerBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, new CornerRadii(15), Insets.EMPTY)));
            Label teller = new Label("Teller"+Integer.toString(i));
            teller.setMinSize(100, 45);
            teller.setMaxSize(100, 45);
            teller.setAlignment(Pos.CENTER);
            teller.setBackground(new Background(new BackgroundFill(Color.LIGHTSALMON, new CornerRadii(15), Insets.EMPTY)));
            Label currentCustomer = new Label("None");
            currentCustomer.setMinSize(100, 45);
            currentCustomer.setMaxSize(100, 45);
            currentCustomer.setAlignment(Pos.CENTER);
            currentCustomer.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(15), Insets.EMPTY)));
            tellerBox.getChildren().addAll(new Label[]{teller, currentCustomer});
            tellerRow.getChildren().add(tellerBox);
        }

        customerQueueList = new ArrayList<>();
        for (int i = 0; i < queueAmount; i++) {
            VBox customerLane = new VBox();
            customerLane.setSpacing(5);
            customerLane.setMinWidth(100);
            customerLane.setMaxWidth(100);
            customerLane.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, new CornerRadii(15), Insets.EMPTY)));
            queueRow.getChildren().add(customerLane);
            CustomerQueue cq = new CustomerQueue(i, new ArrayDeque<>(), customerLane);
            customerQueueList.add(cq);
        }

        customerBufferList = new ArrayList<>();

        timeRunnerService = Executors.newSingleThreadScheduledExecutor();

        customerSpawnManager = new CustomerSpawnManager(customerBufferList, customerInterval);
        customerQueueManager = new CustomerQueueManager(customerBufferList, customerQueueList);
    }

    @FXML public void begin () {
        simulationStatus.setText("Simulation Running");

        customerSpawnManager.startCustomerSpawnService();
        customerQueueManager.startCustomerQueueService();

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
            Platform.runLater(() -> textElapsedSimTime.setText(Integer.toString(time)));
            time++;
        };

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
    }
}
