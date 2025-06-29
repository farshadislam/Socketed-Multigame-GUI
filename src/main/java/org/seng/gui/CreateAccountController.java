package org.seng.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import org.seng.authentication.LoginPage;

import java.io.IOException;

public class CreateAccountController {

    @FXML
    private TextField usernameField, emailField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    private Button registerButton, backButton;

    @FXML
    private Button reqInfo;

    // Called automatically after FXML components are loaded
    @FXML
    public void initialize() {
        // Set actions for buttons
        registerButton.setOnAction(e -> handleRegistration());
        backButton.setOnAction(e -> returnToLogin());

        // Set focus to the back button on startup
        Platform.runLater(() -> {
            backButton.requestFocus();
        });
    }

    // Handles the registration logic, including validation and redirection
    private void handleRegistration() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        boolean hasError = false;

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            displayPasswordsMatchingError();
            return;
        }

        // Validate password format
        if (!HelloApplication.loginPage.verifyPasswordFormat(password)) {
            displayPasswordsFormatError();
            hasError = true;
        }

        // Validate email format
        if (!HelloApplication.loginPage.verifyEmailFormat(email)) {
            displayEmailFormatError();
            hasError = true;
        }

        // Validate username format
        if (!HelloApplication.loginPage.verifyUsernameFormat(username)) {
            displayUsernameFormatError();
            hasError = true;
        }

        // Stop registration if any validation failed
        if (hasError) {
            return;
        }

        // Attempt to register the user
        LoginPage.State state = HelloApplication.loginPage.register(username, email, password);
        System.out.println(state);

        // If registration succeeded, move to verification page
        if (state == LoginPage.State.VERIFICATION_CODE_SENT) {
            openVerificationPage(username);
        }
    }

    // Show error for invalid username format
    private void displayUsernameFormatError() {
        usernameField.getStyleClass().add("error-prompt");
        usernameField.setPromptText("Please enter a valid username!");
        usernameField.clear();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            usernameField.getStyleClass().remove("error-prompt");
            usernameField.setPromptText("Username");
        });
        pause.play();
    }

    // Show error for invalid email format
    private void displayEmailFormatError() {
        emailField.getStyleClass().add("error-prompt");
        emailField.setPromptText("Please enter a valid email!");
        emailField.clear();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            emailField.getStyleClass().remove("error-prompt");
            emailField.setPromptText("Email");
        });
        pause.play();
    }

    // Show error for invalid password format
    private void displayPasswordsFormatError() {
        passwordField.getStyleClass().add("error-prompt");
        confirmPasswordField.getStyleClass().add("error-prompt");
        passwordField.setPromptText("Please enter a valid password!");
        confirmPasswordField.setPromptText("Please enter a valid password!");
        passwordField.clear();
        confirmPasswordField.clear();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            passwordField.getStyleClass().remove("error-prompt");
            confirmPasswordField.getStyleClass().remove("error-prompt");
            passwordField.setPromptText("Password");
            confirmPasswordField.setPromptText("Confirm Password");
        });
        pause.play();
    }

    // Show error if passwords do not match
    private void displayPasswordsMatchingError() {
        passwordField.getStyleClass().add("error-prompt");
        confirmPasswordField.getStyleClass().add("error-prompt");
        passwordField.setPromptText("Passwords do not match");
        confirmPasswordField.setPromptText("Passwords do not match");
        passwordField.clear();
        confirmPasswordField.clear();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            passwordField.getStyleClass().remove("error-prompt");
            confirmPasswordField.getStyleClass().remove("error-prompt");
            passwordField.setPromptText("Password");
            confirmPasswordField.setPromptText("Confirm Password");
        });
        pause.play();
    }

    // Navigates back to the welcome (login) page
    private void returnToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OMG Platform");
            stage.show();

            // Close current window
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Opens the email verification page after successful registration
    private void openVerificationPage(String username) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create-account-verification.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Verify Your Email");

            // Pass the username to the verification controller
            CreateAccountVerificationController controller = fxmlLoader.getController();
            controller.setUsername(username);
            stage.show();

            // Close current window
            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Displays an alert dialog with input format requirements
    public void reqClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input Requirements");
        alert.setHeaderText("Please follow these rules for account creation:");
        alert.setContentText(
                "• Username:\tAt least 5 characters, no whitespaces,\n" +
                        "            only certain special characters allowed.\n\n" +
                        "• Email:\tMust end with @gmail.com,\n" +
                        "            prefix must follow the same rules as username.\n\n" +
                        "• Password:\tAt least 8 characters, no whitespaces,\n" +
                        "            only certain special characters allowed."
        );

        alert.getDialogPane().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-alert");

        alert.showAndWait();
    }
}
