package org.seng.gui;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class WinningPageController {

    @FXML
    private Button rematch;

    @FXML
    private Button returnLobby;

    @FXML
    private Label winLabel;

    @FXML
    public void initialize() {
        winLabel.setTranslateY(-200); // Start above screen

        // Drop animation
        TranslateTransition dropAnimation = new TranslateTransition(Duration.seconds(2.0), winLabel);
        dropAnimation.setFromY(-200);
        dropAnimation.setToY(0);
        dropAnimation.setCycleCount(1);

        // Pulse animation
        ScaleTransition pulseAnimation = new ScaleTransition(Duration.seconds(0.8), winLabel);
        pulseAnimation.setFromX(1.0);
        pulseAnimation.setFromY(1.0);
        pulseAnimation.setToX(1.1);
        pulseAnimation.setToY(1.1);
        pulseAnimation.setAutoReverse(true);
        pulseAnimation.setCycleCount(ScaleTransition.INDEFINITE);

        // Play animations in sequence
        dropAnimation.setOnFinished(e -> pulseAnimation.play());
        dropAnimation.play();
    }

}
