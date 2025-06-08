package org.gjbmloslos.bankqueuesim;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Simulation {

    Stage simStage;
    public static SimulationConfiguration configuration;

    public Simulation (SimulationConfiguration configuration) {
        Simulation.configuration = configuration;
    }

    public void start () {
        FXMLLoader fxmlLoader;
        Parent root;
        try {
            fxmlLoader = new FXMLLoader(SimulationSettings.class.getResource("simulation-view.fxml"));
            root = fxmlLoader.load();
        } catch (IOException e) {throw new RuntimeException(e);}

        simStage = new Stage();
        Scene scene = new Scene(root);
        simStage.setTitle("Bank Queue Simulation");
        simStage.setScene(scene);
        simStage.show();
    }

}
