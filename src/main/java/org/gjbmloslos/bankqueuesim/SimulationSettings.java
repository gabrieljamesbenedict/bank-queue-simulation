package org.gjbmloslos.bankqueuesim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationSettings extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(SimulationSettings.class.getResource("simulation-settings-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Set Configuration");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            Platform.exit(); // Properly shuts down JavaFX thread
            System.exit(0);  // Optional: Forces full JVM shutdown
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}