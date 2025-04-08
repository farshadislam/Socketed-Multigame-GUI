package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.io.IOException;

public class CreateAccountController {

    @FXML
    private TextField usernameField, emailField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    private Button registerButton, backButton;

    @FXML
    public void initialize() {
        registerButton.setText("Register");
        registerButton.setOnAction(e -> handleRegistration());
        backButton.setOnAction(e -> returnToLogin());

        // Reset prompt text when typing
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> resetPrompt(usernameField, "Username"));
        emailField.textProperty().addListener((observable, oldValue, newValue) -> resetPrompt(emailField, "Email"));
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        boolean hasError = false;

        // Check if username is empty
        if (username.isEmpty()) {
            indicateFieldError(usernameField, "Please enter a valid username");
            hasError = true;
        }

        // Check if email is empty
        if (email.isEmpty()) {
            indicateFieldError(emailField, "Please enter a valid email");
            hasError = true;
        }

        // Validate passwords
        if (!password.equals(confirmPassword)) {
            displayPasswordError();
            hasError = true;
        }

        if (hasError) return;

        // Open verification code page
        openVerificationPage();
    }

    private void openVerificationPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create-account-verification.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Verify Your Email");
            stage.show();

            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void resetPrompt(TextField field, String defaultPrompt) {
        field.getStyleClass().remove("error-prompt");
        field.setPromptText(defaultPrompt);
    }

    private void indicateFieldError(TextField field, String message) {
        field.getStyleClass().add("error-prompt");
        field.setPromptText(message);
        field.clear();
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            field.getStyleClass().remove("error-prompt");
            field.setPromptText(field == usernameField ? "Username" : "Email");
        });
        pause.play();
    }

    private void displayPasswordError() {
        passwordField.getStyleClass().add("error-prompt");
        confirmPasswordField.getStyleClass().add("error-prompt");
        passwordField.setPromptText("Passwords do not match");
        confirmPasswordField.setPromptText("Passwords do not match");
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void returnToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OMG Platform");
            stage.show();

            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
