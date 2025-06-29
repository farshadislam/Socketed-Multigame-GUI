package org.seng.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ComingSoonController {

    @FXML
    private javafx.scene.layout.AnchorPane rootPane;

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleReturn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
