package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class ForgotPasswordController {

    public Label messageLabel;
    @FXML
    private TextField emailField;

    @FXML
    private Button resetButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // ** for authentication handle password resetting
        resetButton.setOnAction(e -> handlePasswordReset());

        // back button click
        backButton.setOnAction(e -> goBack());

        // Clear error style when typing
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                emailField.getStyleClass().remove("error-prompt");
                emailField.setPromptText("Enter your email");
            }
        });
    }

    private void handlePasswordReset() {
        String email = emailField.getText();
        if (email.isEmpty()) {
            // Display error prompt text in red
            emailField.setPromptText("Please enter a valid email");
            emailField.clear();
            emailField.getStyleClass().add("error-prompt");
            return;
        }

        System.out.println("Reset link sent to: " + email);
        openVerificationCodeWindow();
    }

    private void goBack() {
        try {
            // Load the Welcome Page scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome-page.fxml"));
            Scene welcomePageScene = new Scene(fxmlLoader.load(), 700, 450);

            // Load the original stylesheet
            welcomePageScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            // Create a new stage for the welcome page
            Stage stage = new Stage();
            stage.setTitle("OMG Platform");
            stage.setScene(welcomePageScene);

            // Close the current forgot password window
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            // Show the welcome page window
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openVerificationCodeWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("verification-code.fxml"));
            Scene verificationScene = new Scene(fxmlLoader.load(), 700, 450);
            verificationScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Verification Code");
            stage.setScene(verificationScene);

            // Close the current forgot password window
            Stage currentStage = (Stage) resetButton.getScene().getWindow();
            currentStage.close();

            // Show the verification code window
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
