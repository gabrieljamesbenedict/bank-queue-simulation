package org.gjbmloslos.bankqueuesim;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.gjbmloslos.bankqueuesim.component.inclusion.ExclusiveMode;
import org.gjbmloslos.bankqueuesim.component.inclusion.InclusionMode;
import org.gjbmloslos.bankqueuesim.component.inclusion.InclusiveMode;
import org.gjbmloslos.bankqueuesim.component.inclusion.StrictExclusiveMode;
import org.gjbmloslos.bankqueuesim.component.interval.IntervalMode;
import org.gjbmloslos.bankqueuesim.component.interval.RandomIntervalMode;
import org.gjbmloslos.bankqueuesim.component.interval.StaticIntervalMode;
import org.gjbmloslos.bankqueuesim.entity.bank.BankService;
import org.gjbmloslos.bankqueuesim.simulation.SimulationConfiguration;

import java.util.ArrayList;

public class SimulationSettingsController {

    @FXML Spinner<Integer> tellerAmount;
    @FXML Spinner<Integer> queueAmount;
    @FXML ComboBox<String> inclusionModePicker;
    @FXML Label inclusionToolTip;

    @FXML ComboBox<String> customerArrivalIntervalMode;
    @FXML Spinner<Integer> customerStaticInterval;
    @FXML Spinner<Integer> customerRandomIntervalMin;
    @FXML Spinner<Integer> customerRandomIntervalMax;
    @FXML HBox staticIntervalContainer;
    @FXML HBox randomIntervalContainer;

    @FXML Spinner<Integer> simulationTime;
    @FXML Spinner<Integer> simulationSpeed;

    @FXML TextField serviceNameInput;
    @FXML TextField serviceDurationInput;
    @FXML Button bankServiceAdd;
    @FXML Button bankServiceEdit  ;
    @FXML Button bankServiceRemove;
    @FXML Button bankServiceClear;
    @FXML TableView<BankService> bankServiceList;
    @FXML TableColumn<BankService, String> bankServiceNameList;
    @FXML TableColumn<BankService, Integer> bankServiceDurationList;

    @FXML Button simulationStart;
    @FXML Button simulationClear;
    @FXML Button simulationCancel;

    InclusionMode inclusionMode;
    IntervalMode intervalMode;
    SimulationConfiguration configuration;

    @FXML
    public void initialize() {
        System.out.println(Runtime.getRuntime().availableProcessors());

        String[] inclusionModeOptions = {"Inclusive", "Exclusive", "Strict Exclusive"};
        inclusionModePicker.getItems().clear();
        inclusionModePicker.getItems().addAll(inclusionModeOptions);
        inclusionModePicker.getSelectionModel().select("Inclusive");
        changeInclusionMode();

        String[] customerArrivalIntervalModeOptions = {"Static", "Random"};
        customerArrivalIntervalMode.getItems().clear();
        customerArrivalIntervalMode.getItems().addAll(customerArrivalIntervalModeOptions);
        customerArrivalIntervalMode.getSelectionModel().select("Static");
        toggleIntervalMode();

        bankServiceNameList.setCellValueFactory(new PropertyValueFactory<BankService, String>("serviceName"));
        bankServiceDurationList.setCellValueFactory(new PropertyValueFactory<BankService, Integer>("serviceDuration"));
        populateWithDefaultBankServices();

        Spinner spinners[] = {
                tellerAmount,
                queueAmount,
                customerStaticInterval,
                customerRandomIntervalMin,
                customerRandomIntervalMax,
                simulationTime
        };
        for (Spinner s : spinners) {
            s.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory (1, Integer.MAX_VALUE));
        }
        simulationTime.getValueFactory().setValue(60);
        simulationSpeed.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory (1, 100));

    }


    @FXML public void toggleIntervalMode () {
        String selectedItem = customerArrivalIntervalMode.getSelectionModel().getSelectedItem().toString();
        if (selectedItem == "Static") {
            staticIntervalContainer.setVisible(true);
            staticIntervalContainer.setManaged(true);
            randomIntervalContainer.setVisible(false);
            randomIntervalContainer.setManaged(false);
        } else if (selectedItem == "Random") {
            staticIntervalContainer.setVisible(false);
            staticIntervalContainer.setManaged(false);
            randomIntervalContainer.setVisible(true);
            randomIntervalContainer.setManaged(true);
        }
    }

    @FXML public void changeInclusionMode () {
        String s = inclusionModePicker.getSelectionModel().getSelectedItem();
        if (s.equals("Inclusive")) {
            inclusionMode = new InclusiveMode();
            inclusionMode.setToolTip("Can go from any queue to any teller.");
        } else if (s.equals("Exclusive")) {
            inclusionMode = new ExclusiveMode();
            inclusionMode.setToolTip("A teller owns a queue but can take from others if free.");
        } else if (s.equals("Strict Exclusive")) {
            inclusionMode = new StrictExclusiveMode();
            inclusionMode.setToolTip("A teller owns a queue and will not take from other queues.");
        } else {
            inclusionMode = null;
            inclusionMode.setToolTip("Err");
        }
        inclusionToolTip.setText(inclusionMode.getToolTip());
    }

    private void populateWithDefaultBankServices () {

        bankServiceList.getItems().add(new BankService("Withdrawal", 10));
        bankServiceList.getItems().add(new BankService("Deposit", 20));
        bankServiceList.getItems().add(new BankService("Loan", 40));
        bankServiceList.getItems().add(new BankService("Transfer", 60));
        bankServiceList.getItems().add(new BankService("CustomerCare", 120));
        bankServiceList.getItems().add(new BankService("Inquiry", 180));

    }

    @FXML public void addBankService () {
        String name = serviceNameInput.getText();
        int duration = Integer.parseInt(serviceDurationInput.getText());
        BankService bankService = new BankService(name, duration);
        bankServiceList.getItems().add(bankService);
    }

    @FXML public void editBankService () {
        int index = bankServiceList.getSelectionModel().getSelectedIndex();
        String name = serviceNameInput.getText();
        String duration = serviceDurationInput.getText();
        BankService bankService = bankServiceList.getSelectionModel().getSelectedItem();
        if (!name.isBlank()) {
            bankService.setServiceName(name);
            bankServiceList.getItems().set(index, bankService);
        }
        if (!duration.isBlank()) {
            bankService.setServiceDuration(Integer.parseInt(duration));
            bankServiceList.getItems().set(index, bankService);
        }

    }

    @FXML public void deleteBankService () {
        int index = bankServiceList.getSelectionModel().getSelectedIndex();
        bankServiceList.getItems().remove(index);
    }

    @FXML public void clearBankService () {
        bankServiceList.getItems().removeAll(bankServiceList.getItems());
    }

    private Boolean validateInputFields () {
        // return true if inputs are valid, otherwise return false.
        return true;
    }

    @FXML public void startSim () {

        if (!validateInputFields()) {
            return;
        }

        configuration = new SimulationConfiguration(
                tellerAmount.getValue(),
                queueAmount.getValue(),
                simulationTime.getValue(),
                simulationSpeed.getValue(),
                inclusionMode,
                (customerArrivalIntervalMode.getSelectionModel().getSelectedItem().equals("Static"))?
                    new StaticIntervalMode(customerStaticInterval.getValue())
                    :
                    new RandomIntervalMode(customerRandomIntervalMin.getValue(), customerRandomIntervalMax.getValue()),
                new ArrayList<>(bankServiceList.getItems())
        );

        Simulation sim = new Simulation(configuration);
        sim.start();


    }

    @FXML public void clearConfig () {

    }

    @FXML public void exitConfig () {

    }


}