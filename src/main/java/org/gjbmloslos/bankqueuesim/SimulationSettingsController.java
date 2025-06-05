package org.gjbmloslos.bankqueuesim;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.gjbmloslos.bankqueuesim.model.BankService;

public class SimulationSettingsController {

    @FXML Spinner<Integer> tellerAmount;
    @FXML Spinner<Integer> queueAmount;
    @FXML CheckBox strictExclusivity;

    @FXML ComboBox custumerArrivalIntervalMode;
    @FXML Spinner<Integer> customerStaticInterval;
    @FXML Spinner<Integer> customerRandomIntervalMin;
    @FXML Spinner<Integer> customerRandomIntervalMax;
    @FXML HBox staticIntervalContainer;
    @FXML HBox randomIntervalContainer;

    @FXML Spinner<Integer> simulationTime;

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

    @FXML
    public void initialize() {

        String[] custumerArrivalIntervalModeOptions = {"Static", "Random"};
        custumerArrivalIntervalMode.getItems().removeAll(custumerArrivalIntervalMode.getItems());
        custumerArrivalIntervalMode.getItems().addAll(custumerArrivalIntervalModeOptions);
        custumerArrivalIntervalMode.getSelectionModel().select("Static");
        toggleIntervalMode();

        bankServiceNameList.setCellValueFactory(new PropertyValueFactory<BankService, String>("serviceName"));
        bankServiceDurationList.setCellValueFactory(new PropertyValueFactory<BankService, Integer>("serviceDuration"));
        populateWithDefaultBankServices();

    }

    @FXML public void toggleIntervalMode () {
        String selectedItem = custumerArrivalIntervalMode.getSelectionModel().getSelectedItem().toString();
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

    private void populateWithDefaultBankServices () {

        bankServiceList.getItems().add(new BankService("Deposit", 30));
        bankServiceList.getItems().add(new BankService("Withdrawal", 10));
        bankServiceList.getItems().add(new BankService("Customer Care", 180));
        bankServiceList.getItems().add(new BankService("Create Account", 300));

    }

}