package org.seng.gui;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Random;

public class WelcomePageController {

    @FXML
    private Label titleLabel;

    @FXML
    private Pane iconPane;

    @FXML
    public void initialize() {
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

        // animation sequence
        dropAnimation.setOnFinished(e -> pulseAnimation.play());
        dropAnimation.play();

        // add controller icons
        iconPane.widthProperty().addListener((obs, oldVal, newVal) -> addControllerIcons());
        iconPane.heightProperty().addListener((obs, oldVal, newVal) -> addControllerIcons());
    }

    private void addControllerIcons() {
        // clear previous icons
        iconPane.getChildren().clear();

        // load controller image
        Image controllerImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/controller.png"));
        Random random = new Random();
        int numberOfIcons = 30;

        for (int i = 0; i < numberOfIcons; i++) {
            ImageView icon = new ImageView(controllerImage);

            // set icon size
            icon.setFitWidth(50);
            icon.setFitHeight(50);

            // get width and height of current window
            double paneWidth = iconPane.getWidth();
            double paneHeight = iconPane.getHeight();

            // add to random position in pane
            double x = random.nextDouble() * (paneWidth - 50);  // Adjust for icon size
            double y = random.nextDouble() * (paneHeight - 50); // Adjust for icon size
            icon.setLayoutX(x);
            icon.setLayoutY(y);

            // randomly rotate them
            double angle = random.nextDouble() * 360;
            icon.getTransforms().add(new Rotate(angle, 25, 25)); // Rotate around the center

            // apply transparency
            icon.getStyleClass().add("controller-icon");

            // add to icon pain
            iconPane.getChildren().add(icon);
        }
    }
}
