package org.seng.gui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WinningPageController {

    @FXML
    private Button rematch;

    @FXML
    private Button returnLobby;

    @FXML
    private Pane trophyIconPane;

    @FXML
    private Label winLabel;

    private final int ICON_SIZE = 50; // width and height of each icon
    private final int BUFFER = ICON_SIZE; // buffer equal to size of the icon
    private final int MAX_RETRIES = 50; // max tries to place icon without overlap



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

        trophyIconPane.widthProperty().addListener((obs, oldVal, newVal) -> addControllerIcons());
        trophyIconPane.heightProperty().addListener((obs, oldVal, newVal) -> addControllerIcons());
    }

    private void addControllerIcons() {
        trophyIconPane.getChildren().clear();

        Image controllerImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/trophies.png"));
        Random random = new Random();
        int numberOfIcons = 15;
        List<double[]> placedPositions = new ArrayList<>();

        for (int i = 0; i < numberOfIcons; i++) {
            ImageView icon = new ImageView(controllerImage);
            icon.setFitWidth(ICON_SIZE);
            icon.setFitHeight(ICON_SIZE);

            double x, y;
            int attempts = 0;
            boolean positionValid;

            do {
                positionValid = true;
                x = random.nextDouble() * (trophyIconPane.getWidth() - ICON_SIZE);
                y = random.nextDouble() * (trophyIconPane.getHeight() - ICON_SIZE);

                for (double[] pos : placedPositions) {
                    double distance = Math.hypot((pos[0] - x), (pos[1] - y));
                    if (distance < 1.5 * ICON_SIZE) {
                        positionValid = false;
                        break;
                    }
                }
                attempts++;
            } while (!positionValid && attempts < MAX_RETRIES);

            if (!positionValid) continue;

            placedPositions.add(new double[]{x + ICON_SIZE / 2.0, y + ICON_SIZE / 2.0});
            icon.setLayoutX(x);
            icon.setLayoutY(y);
            icon.getTransforms().add(new Rotate(random.nextDouble() * 360, ICON_SIZE / 2.0, ICON_SIZE / 2.0));
            icon.getStyleClass().add("controller-icon");

            TranslateTransition floatAnim = new TranslateTransition(Duration.seconds(2), icon);
            floatAnim.setByY(10);
            floatAnim.setAutoReverse(true);
            floatAnim.setCycleCount(Animation.INDEFINITE);

            RotateTransition rotateAnim = new RotateTransition(Duration.seconds(10), icon);
            rotateAnim.setByAngle(360);
            rotateAnim.setCycleCount(Animation.INDEFINITE);

            ScaleTransition scaleAnim = new ScaleTransition(Duration.seconds(3), icon);
            scaleAnim.setFromX(0.9);
            scaleAnim.setFromY(0.9);
            scaleAnim.setToX(1.1);
            scaleAnim.setToY(1.1);
            scaleAnim.setAutoReverse(true);
            scaleAnim.setCycleCount(Animation.INDEFINITE);

            new ParallelTransition(icon, floatAnim, rotateAnim, scaleAnim).play();

            trophyIconPane.getChildren().add(icon);
        }
    }


}
