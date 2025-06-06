package org.gjbmloslos.bankqueuesim;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import org.gjbmloslos.bankqueuesim.model.BankService;
import org.gjbmloslos.bankqueuesim.model.Interval;

import java.util.ArrayList;

public class SimulationController {

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

    ArrayList<VBox> customerLaneList;


    @FXML public void initialize () {

        System.out.println("Hello World2");

        tellerAmount = Simulation.configuration.getTellerAmount();
        queueAmount = Simulation.configuration.getQueueAmount();
        simulationTime = Simulation.configuration.getSimulationTime();
        strictExclusivity = Simulation.configuration.getStrictExclusivity();
        customerInterval = Simulation.configuration.getInterval();
        bankServiceList = Simulation.configuration.getBankServiceList();

        textMaxSimTime.setText(Integer.toString(simulationTime));
        textElapsedSimTime.setText(Integer.toString(0));
        textTellerAmount.setText(Integer.toString(tellerAmount));
        textQueueAmount.setText(Integer.toString(queueAmount));
        textStrictExclusivity.setText(Boolean.toString(strictExclusivity));
        textIntervalMode.setText(customerInterval.getMode());
        textIntervalTime.setText(customerInterval.getTimeInterval());
        lvBankService.getItems().addAll(bankServiceList);

        customerLaneList = new ArrayList<>();
        for (int i = 0; i < tellerAmount; i++) {
            BorderPane teller = new BorderPane();
            teller.setPrefSize(50, 50);
            teller.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            Label tellerIndex = new Label(Integer.toString(i));
            teller.setCenter(tellerIndex);
            tellerRow.getChildren().add(teller);
        }
        for (int i = 0; i < queueAmount; i++) {
            VBox customerLane = new VBox();
            customerLane.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
            queueRow.getChildren().add(customerLane);
            customerLaneList.add(customerLane);
        }

    }

    @FXML public void beginSimulation () {
        simulationStatus.setText("Simulation Running");
    }

}
