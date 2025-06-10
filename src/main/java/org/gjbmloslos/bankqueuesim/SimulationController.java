package org.gjbmloslos.bankqueuesim;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;
import org.gjbmloslos.bankqueuesim.entity.bank.BankTellerManager;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueue;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerSpawnManager;
import org.gjbmloslos.bankqueuesim.entity.interval.Interval;
import org.gjbmloslos.bankqueuesim.entity.customer.CustomerQueueManager;
import org.gjbmloslos.bankqueuesim.entity.bank.BankTeller;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulationController {

    public static int time;
    public static int actualTime;
    public static int speed;

    int tellerAmount, queueAmount, simulationTime;
    Boolean strictExclusivity;
    Interval customerInterval;
    ArrayList<BankService> bankServiceList;

    @FXML Text simulationStatus;
    @FXML Text textMaxSimTime;
    @FXML Text textElapsedSimTime;
    @FXML Text textActualElapsedTime;
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
    Runnable actualTimeRunner;

    CustomerSpawnManager customerSpawnManager;
    CustomerQueueManager customerQueueManager;
    BankTellerManager bankTellerManager;

    ArrayList<Customer> customerBufferList;
    ArrayList<CustomerQueue> customerQueueList;
    ArrayList<BankTeller> bankTellerList;

    private String timestamp () {
        return " @" + time + "s";
    }

    @FXML public void initialize () {

        time = 0;
        actualTime = 0;
        speed = Simulation.configuration.getSimulationSpeed();

        tellerAmount = Simulation.configuration.getTellerAmount();
        queueAmount = Simulation.configuration.getQueueAmount();
        simulationTime = Simulation.configuration.getSimulationTime();
        strictExclusivity = Simulation.configuration.getStrictExclusivity();
        customerInterval = Simulation.configuration.getInterval();
        bankServiceList = Simulation.configuration.getBankServiceList();

        textMaxSimTime.setText(Integer.toString(simulationTime));
        textElapsedSimTime.setText(Integer.toString(time));
        textElapsedSimTime.setText(Integer.toString(time));
        textSimulationSpeed.setText(Integer.toString(speed));
        textTellerAmount.setText(Integer.toString(tellerAmount));
        textQueueAmount.setText(Integer.toString(queueAmount));
        textStrictExclusivity.setText(Boolean.toString(strictExclusivity));
        textIntervalMode.setText(customerInterval.getMode());
        textIntervalTime.setText(customerInterval.getTimeInterval());
        lvBankService.getItems().addAll(bankServiceList);

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

        customerSpawnManager = new CustomerSpawnManager(customerBufferList, customerInterval, bankServiceList);
        customerQueueManager = new CustomerQueueManager(customerBufferList, customerQueueList);
        bankTellerManager = new BankTellerManager(bankTellerList, customerQueueList);

        simulationTime += 1; // sim time padding
    }

    @FXML public void begin () {
        simulationStatus.setText("Simulation Running");

        customerSpawnManager.startCustomerSpawnService();
        customerQueueManager.startCustomerQueueService();
        bankTellerManager.startBankTellerManageService();

        timeRunner = () -> {
            System.out.println("==================================================================================================");
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
    }
}
