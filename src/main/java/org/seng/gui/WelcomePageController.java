package org.seng.gui;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class WelcomePageController {

    @FXML
    private Label titleLabel;

    @FXML
    public void initialize() {
        // Start with text outside frame
        titleLabel.setTranslateY(-200);

        // drop animation
        TranslateTransition dropAnimation = new TranslateTransition(Duration.seconds(0.5), titleLabel);
        dropAnimation.setFromY(-200);  // Start above the screen
        dropAnimation.setToY(0);       // Final position
        dropAnimation.setCycleCount(1); // One-time drop

        // pulse animation
        ScaleTransition pulseAnimation = new ScaleTransition(Duration.seconds(0.8), titleLabel);
        pulseAnimation.setFromX(1.0);
        pulseAnimation.setFromY(1.0);
        pulseAnimation.setToX(1.1);
        pulseAnimation.setToY(1.1);
        pulseAnimation.setAutoReverse(true);
        pulseAnimation.setCycleCount(ScaleTransition.INDEFINITE);

        // drop animation then play animation
        dropAnimation.setOnFinished(e -> pulseAnimation.play());
        dropAnimation.play();
    }
}
