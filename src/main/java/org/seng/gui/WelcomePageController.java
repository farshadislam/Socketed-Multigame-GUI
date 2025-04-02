package org.seng.gui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WelcomePageController {
    public VBox loginBox;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Label titleLabel;

    @FXML
    private Pane iconPane;

    private final int ICON_SIZE = 50; // width and height of each icon
    private final int BUFFER = ICON_SIZE; // buffer equal to size of the icon
    private final int MAX_RETRIES = 50; // max tries to place icon without overlap

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

        // animation order
        dropAnimation.setOnFinished(e -> pulseAnimation.play());
        dropAnimation.play();

        // adding controller icons
        iconPane.widthProperty().addListener((obs, oldVal, newVal) -> addControllerIcons());
        iconPane.heightProperty().addListener((obs, oldVal, newVal) -> addControllerIcons());

        // handle login button click
        loginButton.setOnAction(e -> handleLogin());

        // handle create account button click
        createAccountButton.setOnAction(e -> handleCreateAccount());

        // handle forgot password link click
        forgotPasswordLink.setOnAction(e -> openForgotPasswordWindow());

        // handle create account button click
        createAccountButton.setOnAction(e -> openCreateAccountPage());
    }

    private void addControllerIcons() {
        // clear previous icons
        iconPane.getChildren().clear();

        // load image
        Image controllerImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/controller.png"));
        Random random = new Random();
        int numberOfIcons = 15;

        // store positions of icons to make sure no overlap
        List<double[]> placedPositions = new ArrayList<>();

        for (int i = 0; i < numberOfIcons; i++) {
            ImageView icon = new ImageView(controllerImage);

            // set icon size
            icon.setFitWidth(ICON_SIZE);
            icon.setFitHeight(ICON_SIZE);

            // positioning variables
            double x, y;
            int attempts = 0;
            boolean positionValid;

            do {
                positionValid = true;
                x = random.nextDouble() * (iconPane.getWidth() - ICON_SIZE);
                y = random.nextDouble() * (iconPane.getHeight() - ICON_SIZE);

                // check if overlap in positions
                for (double[] pos : placedPositions) {
                    double distance = Math.hypot((pos[0] - x), (pos[1] - y));
                    if (distance < (1.5 * ICON_SIZE)) {  // compare with buffer
                        positionValid = false;
                        break;
                    }
                }
                attempts++;
            } while (!positionValid && attempts < MAX_RETRIES);

            // if max retries, skip placing
            if (!positionValid) continue;

            // save valid position
            placedPositions.add(new double[]{x + ICON_SIZE / 2.0, y + ICON_SIZE / 2.0});

            // set position and rotate randomly
            icon.setLayoutX(x);
            icon.setLayoutY(y);
            double angle = random.nextDouble() * 360;
            icon.getTransforms().add(new Rotate(angle, ICON_SIZE / 2.0, ICON_SIZE / 2.0));
            icon.getStyleClass().add("controller-icon");

            // floating animation
            TranslateTransition floatAnimation = new TranslateTransition(Duration.seconds(2), icon);
            floatAnimation.setByY(10);
            floatAnimation.setAutoReverse(true);
            floatAnimation.setCycleCount(TranslateTransition.INDEFINITE);

            // rotation animation
            RotateTransition rotateAnimation = new RotateTransition(Duration.seconds(10), icon);
            rotateAnimation.setByAngle(360);
            rotateAnimation.setCycleCount(RotateTransition.INDEFINITE);

            // pulsing animation
            ScaleTransition scaleAnimation = new ScaleTransition(Duration.seconds(3), icon);
            scaleAnimation.setFromX(0.9);
            scaleAnimation.setFromY(0.9);
            scaleAnimation.setToX(1.1);
            scaleAnimation.setToY(1.1);
            scaleAnimation.setAutoReverse(true);
            scaleAnimation.setCycleCount(ScaleTransition.INDEFINITE);

            // combine all animations
            ParallelTransition combinedAnimation = new ParallelTransition(floatAnimation, rotateAnimation, scaleAnimation);
            combinedAnimation.play();

            iconPane.getChildren().add(icon);
        }
    }

    // login button click handler
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        System.out.println("Login Attempt: Username - " + username + ", Password - " + password);
    }

    // create account button click handler
    private void handleCreateAccount() {
        System.out.println("Create Account Clicked");
    }

    // open forgot password window
    private void openForgotPasswordWindow() {
        try {
            // load password forget screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("forgot-password.fxml"));
            Scene forgotPasswordScene = new Scene(fxmlLoader.load(), 700, 450);

            // load basic style sheet
            forgotPasswordScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            // create new stage for forgot password
            Stage stage = new Stage();
            stage.setTitle("Forgot Password");
            stage.setScene(forgotPasswordScene);

            // close current welcome stage
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            // show forgot password window
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openCreateAccountPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create-account.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Create Account");
            stage.show();

            Stage currentStage = (Stage) createAccountButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
