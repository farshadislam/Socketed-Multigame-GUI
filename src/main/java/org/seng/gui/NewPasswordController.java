package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import org.seng.authentication.LoginPage;

import java.io.IOException;

public class NewPasswordController {

    @FXML
    private PasswordField newPasswordField, confirmPasswordField;

    @FXML
    private Button updatePasswordButton, returnButton;

    @FXML
    private Label errorLabel;
    private LoginPage loginPage;
    private String username;

    @FXML
    public void initialize() {
        updatePasswordButton.setOnAction(e -> handleUpdatePassword());
        returnButton.setOnAction(e -> returnToWelcome());
    }
    public void setLoginPage(LoginPage loginPage){
        this.loginPage = loginPage;
    }

    public void setUsername(String username){
        this.username = username;
    }

    // handle password update click
    private void handleUpdatePassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (!newPassword.equals(confirmPassword)) {
            displayErrorMessage("Make Sure Passwords Match!");
            indicatePasswordError();
            return;
        }
        if(!loginPage.changePassword(this.username, newPassword, confirmPassword)){
            displayErrorMessage("Please Choose A Valid Password!");
            indicatePasswordError();
            return;
        }

        indicatePasswordSuccess();
        displaySuccessMessage("Password updated");
    }

    // Display error message for non-matching passwords
    private void displayErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(true);

        // Flash message for 2 seconds and then clear
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> errorLabel.setVisible(false));
        pause.play();
    }

    // Display success message for matching passwords
    private void displaySuccessMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setVisible(true);

        // Flash message for 2 seconds and then clear
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> errorLabel.setVisible(false));
        pause.play();
    }

    // Highlight password fields in red when they don't match
    private void indicatePasswordError() {
        newPasswordField.getStyleClass().add("error-prompt");
        confirmPasswordField.getStyleClass().add("error-prompt");

        // Remove the error style after a short delay
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            newPasswordField.getStyleClass().remove("error-prompt");
            confirmPasswordField.getStyleClass().remove("error-prompt");
        });
        pause.play();
    }

    // Highlight password fields in green when they match
    private void indicatePasswordSuccess() {
        newPasswordField.getStyleClass().add("success-prompt");
        confirmPasswordField.getStyleClass().add("success-prompt");

        // Remove the success style after a short delay
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            newPasswordField.getStyleClass().remove("success-prompt");
            confirmPasswordField.getStyleClass().remove("success-prompt");
        });
        pause.play();
    }

    // Return to the Welcome page
    private void returnToWelcome() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome-page.fxml"));
            Scene welcomePageScene = new Scene(fxmlLoader.load(), 700, 450);
            welcomePageScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("OMG Platform");
            stage.setScene(welcomePageScene);
            WelcomePageController controller = fxmlLoader.getController();
            controller.setLoginPage(this.loginPage);
            // Close current window
            Stage currentStage = (Stage) returnButton.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
