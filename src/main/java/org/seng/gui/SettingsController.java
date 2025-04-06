package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Optional;

public class SettingsController {

    @FXML
    private TextField usernameField, emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        // Load current user info (mocked for now)
        usernameField.setText("Player1");
        emailField.setText("player1@example.com");
    }

    @FXML
    private void changeUsername() {
        String newUsername = usernameField.getText();
        showAlert("Username Updated", "Your username has been changed to: " + newUsername);
        // Implement backend call to update username here
    }

    @FXML
    private void changeEmail() {
        String newEmail = emailField.getText();
        showAlert("Email Updated", "Your email has been changed to: " + newEmail);
        // Implement backend call to update email here
    }

    @FXML
    private void changePassword() {
        String newPassword = passwordField.getText();
        showAlert("Password Changed", "Your password has been successfully updated.");
        // Implement backend call to update password here
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome-page.fxml"));
            Scene welcomeScene = new Scene(fxmlLoader.load(), 700, 450);
            Stage stage = new Stage();
            stage.setTitle("Welcome");
            stage.setScene(welcomeScene);

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void deleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            handleLogout(); // Redirect to welcome after deleting
            showAlert("Account Deleted", "Your account has been successfully deleted.");
            // Implement backend call to delete the account here
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
