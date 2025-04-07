package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Optional;

public class SettingsController {

    @FXML
    private Label contentTitle, usernameError, emailError;

    @FXML
    private TextField usernameField, emailField;

    @FXML
    private VBox manageAccountArea, networkConnectivityArea;

    @FXML
    private ImageView backIcon;

    @FXML
    public void initialize() {
        try {
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            backIcon.setImage(backImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene dashboardScene = new Scene(fxmlLoader.load(), 700, 450);
            dashboardScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage stage = (Stage) backIcon.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void showManageAccount() {
        contentTitle.setText("Manage Account");

        // Show Manage Account area and hide Network Connectivity area
        manageAccountArea.setVisible(true);
        manageAccountArea.setManaged(true);
        networkConnectivityArea.setVisible(false);
        networkConnectivityArea.setManaged(false);
    }
    @FXML
    public void showNetworkConnectivity() {
        contentTitle.setText("Network Connectivity");

        // Show Network Connectivity area and hide Manage Account area
        networkConnectivityArea.setVisible(true);
        networkConnectivityArea.setManaged(true);
        manageAccountArea.setVisible(false);
        manageAccountArea.setManaged(false);
    }

    @FXML
    public void changeUsername() {
        String newUsername = usernameField.getText();
        if (newUsername.isEmpty() || newUsername.length() < 3) {
            usernameField.setPromptText("Must be at least 3 characters");
            usernameField.setStyle("-fx-border-color: red; -fx-prompt-text-fill: red;");
            usernameField.clear();
        } else {
            usernameField.setPromptText("New Username");
            usernameField.setStyle("-fx-border-color: #ddd; -fx-prompt-text-fill: #aaa;");
            // Handle the username change logic here
            System.out.println("Username changed to: " + newUsername);
        }
    }

    @FXML
    public void changeEmail() {
        String newEmail = emailField.getText();
        if (!newEmail.contains("@") || !newEmail.contains(".")) {
            emailField.setPromptText("Invalid email format");
            emailField.setStyle("-fx-border-color: red; -fx-prompt-text-fill: red;");
            emailField.clear();
        } else {
            emailField.setPromptText("New Email");
            emailField.setStyle("-fx-border-color: #ddd; -fx-prompt-text-fill: #aaa;");
            // Handle the email change logic here
            System.out.println("Email changed to: " + newEmail);
        }
    }

    @FXML
    public void redirectToVerification() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("verification-page.fxml"));
            Scene verificationScene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Verification");
            stage.setScene(verificationScene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("You will be returned to the welcome page.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome-page.fxml"));
                Scene welcomeScene = new Scene(fxmlLoader.load(), 700, 450);
                welcomeScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

                Stage stage = new Stage();
                stage.setTitle("Welcome");
                stage.setScene(welcomeScene);

                Stage currentStage = (Stage) contentTitle.getScene().getWindow();
                currentStage.close();

                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void deleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // go to welcome page
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome-page.fxml"));
                    Scene welcomeScene = new Scene(fxmlLoader.load(), 700, 450);
                    welcomeScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

                    Stage stage = new Stage();
                    stage.setTitle("Welcome");
                    stage.setScene(welcomeScene);

                    Stage currentStage = (Stage) contentTitle.getScene().getWindow();
                    currentStage.close();

                    stage.show();
                    System.out.println("Account deleted and logged out.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
