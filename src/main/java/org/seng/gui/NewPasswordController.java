package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class NewPasswordController {

    @FXML
    private PasswordField newPasswordField, confirmPasswordField;

    @FXML
    private Button updateButton, backButton;

    @FXML
    public void initialize() {
        updateButton.setOnAction(e -> handlePasswordUpdate());
        backButton.setOnAction(e -> goBack());
    }

    // storing passwords ** authentication **
    private void handlePasswordUpdate() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.equals(confirmPassword)) {
            System.out.println("Password updated successfully!");
            goBack();
        } else {
            System.out.println("Passwords do not match.");
        }
    }

    private void goBack() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
    }
}
