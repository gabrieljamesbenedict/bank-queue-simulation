package org.gjbmloslos.bankqueuesim.entity.bank;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.gjbmloslos.bankqueuesim.entity.customer.Customer;

public class BankTeller {

    int id;
    VBox tellerBox;

    Customer currentCustomer;
    boolean busy;
    boolean complete;

    public BankTeller(int id, VBox tellerBox) {
        this.id = id;
        this.tellerBox = tellerBox;

        tellerBox.getChildren().addAll(new Label(), new Label());
        defaultTellerLabel();
        defaultCustomerLabel();
    }

    public void defaultTellerLabel() {
        Label tellerLabel = new Label("Teller"+Integer.toString(id));
        tellerLabel.setMinSize(125, 45);
        tellerLabel.setMaxSize(125, 45);
        tellerLabel.setAlignment(Pos.CENTER);
        tellerLabel.setTextAlignment(TextAlignment.CENTER);
        tellerLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTSALMON, new CornerRadii(15), Insets.EMPTY)));
        tellerBox.getChildren().set(0, tellerLabel);
    }

    public void defaultCustomerLabel() {
        Label currentCustomerLabel = new Label("None");
        currentCustomerLabel.setMinSize(125, 45);
        currentCustomerLabel.setMaxSize(125, 45);
        currentCustomerLabel.setAlignment(Pos.CENTER);
        currentCustomerLabel.setTextAlignment(TextAlignment.CENTER);
        currentCustomerLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(15), Insets.EMPTY)));
        tellerBox.getChildren().set(1, currentCustomerLabel);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VBox getTellerBox() {
        return tellerBox;
    }

    public void setTellerBox(VBox tellerBox) {
        this.tellerBox = tellerBox;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
