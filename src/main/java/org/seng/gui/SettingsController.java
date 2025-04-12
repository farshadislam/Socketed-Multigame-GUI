package org.seng.gui;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.seng.authentication.Player;

import java.io.IOException;
import java.util.Optional;

import static org.seng.gui.GameDashboardController.player;
import static org.seng.gui.GameDashboardController.setting;
import static org.seng.gui.HelloApplication.database;
import static org.seng.gui.HelloApplication.loginPage;

public class SettingsController {
    public Label passwordSuccess;
    @FXML
    private Label emailSuccessPrompt;

    @FXML
    private Label contentTitle, usernameError, emailError, passwordError;

    @FXML
    private TextField usernameField, emailField;

    @FXML
    private VBox manageAccountArea, networkConnectivityArea;

    @FXML
    private ImageView backIcon;

    @FXML
    private PasswordField newPasswordField, confirmPasswordField;

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
        manageAccountArea.setVisible(true);
        manageAccountArea.setManaged(true);
        networkConnectivityArea.setVisible(false);
        networkConnectivityArea.setManaged(false);
    }

    @FXML
    public void showNetworkConnectivity() {
        contentTitle.setText("Network Connectivity");
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

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                usernameField.setStyle("-fx-border-color: #ddd; -fx-prompt-text-fill: #aaa;");
                usernameField.setPromptText("New username");
            });
            pause.play();

        } else {
            String updatedUsername = usernameField.getText();
            boolean success = setting.changeUsername(updatedUsername);

            if (success) {
                player.setUsername(updatedUsername);

                usernameField.setPromptText("New username");
                usernameField.setStyle("-fx-border-color: green");
                passwordSuccess.setVisible(true);
                passwordSuccess.setManaged(true);
                passwordSuccess.setText("Username updated");

                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(e -> {
                    passwordSuccess.setVisible(false);
                    passwordSuccess.setManaged(false);
                    usernameField.setStyle("-fx-border-color: #ddd");
                });
                pause.play();
            } else {
                usernameField.setPromptText("Username taken or error");
                usernameField.setStyle("-fx-border-color: red; -fx-prompt-text-fill: red;");
            }
        }
    }

    @FXML
    public void changeEmail() {
        String newEmail = emailField.getText();
        if (!(loginPage.verifyEmailFormat(newEmail))) {
            emailField.setPromptText("Invalid email format");
            emailField.setStyle("-fx-border-color: red; -fx-prompt-text-fill: red;");
            emailField.clear();

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                emailField.setStyle("-fx-border-color: #ddd; -fx-prompt-text-fill: #aaa;");
                emailField.setPromptText("New email");
            });
            pause.play();

        } else {
            emailField.setPromptText("New Email");
            emailField.setStyle("-fx-border-color: #ddd; -fx-prompt-text-fill: #aaa;");
            System.out.println("Email changed to: " + newEmail);
            verifyEmail();
        }
    }

    @FXML
    public void verifyEmail() {

        passwordSuccess.setText("Verification email sent.");
        passwordSuccess.setVisible(true);
        passwordSuccess.setManaged(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            passwordSuccess.setVisible(false);
            passwordSuccess.setManaged(false);
        });
        pause.play();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("email-verification.fxml"));
            Scene verificationScene = new Scene(fxmlLoader.load(), 600, 400);
            verificationScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Email Verification");
            stage.setScene(verificationScene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void updatePassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            passwordError.setText("Both fields are required.");
            passwordError.setVisible(true);
            passwordError.setManaged(true);

            newPasswordField.setStyle("-fx-border-color: #f30d0d;");
            confirmPasswordField.setStyle("-fx-border-color: #f30d0d;");

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                passwordError.setVisible(false);
                passwordError.setManaged(false);
                newPasswordField.setStyle("-fx-border-color: #f0f0f0;");
                confirmPasswordField.setStyle("-fx-border-color: #ddd");
            });
            pause.play();

        } else if (!newPassword.equals(confirmPassword)) {
            passwordError.setText("Passwords do not match.");
            passwordError.setVisible(true);
            passwordError.setManaged(true);
            newPasswordField.setStyle("-fx-border-color: #f30d0d;");
            confirmPasswordField.setStyle("-fx-border-color: #f30d0d;");

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                passwordError.setVisible(false);
                passwordError.setManaged(false);
                newPasswordField.setStyle("-fx-border-color: #f0f0f0;");
                confirmPasswordField.setStyle("-fx-border-color: #ddd");
            });
            pause.play();

        } else if (!(loginPage.verifyPasswordFormat(newPassword))) {
            passwordError.setText("Password format incorrect.");
            passwordError.setVisible(true);
            passwordError.setManaged(true);
            newPasswordField.setStyle("-fx-border-color: #f30d0d;");
            confirmPasswordField.setStyle("-fx-border-color: #f30d0d;");

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                passwordError.setVisible(false);
                passwordError.setManaged(false);
                newPasswordField.setStyle("-fx-border-color: #f0f0f0;");
                confirmPasswordField.setStyle("-fx-border-color: #ddd");
            });
            pause.play();
        }

        else {
            setting.changePassword(newPassword);

            passwordSuccess.setText("Password updated.");
            passwordSuccess.setVisible(true);
            passwordSuccess.setManaged(true);
            newPasswordField.setStyle("-fx-border-color: green;");
            confirmPasswordField.setStyle("-fx-border-color: green;");

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                passwordSuccess.setVisible(false);
                passwordSuccess.setManaged(false);
                newPasswordField.setStyle("-fx-background-color: #f0f0f0");
                confirmPasswordField.setStyle("-fx-border-color: #ddd");
            });
            pause.play();

            System.out.println("Password updated successfully.");

        }
    }

    @FXML
    public void onVerificationSuccess() {
        emailSuccessPrompt.setText("Email changed successfully.");
        emailSuccessPrompt.setVisible(true);
        emailSuccessPrompt.setManaged(true);
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
        setting.deleteAccount();
        database.saveDatabase("");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
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


    @FXML
    public void openInstructions() {
        // For example, show a simple Alert with instructions, or open a modal dialog.
        Alert instructionsAlert = new Alert(Alert.AlertType.INFORMATION);
        instructionsAlert.setTitle("Instructions");
        instructionsAlert.setHeaderText("Network / Multiplayer Instructions");
        instructionsAlert.setContentText(
                "It is default set so that 2 users can run “Hello Application” " +
                        "on the same device and play locally. This is through the localhost. " +
                        "The server must be run first in “SocketGameServer.java”.\n\n" +
                        "For remote play on separate devices in the same network, " +
                        "the host runs the server normally, and the second player changes " +
                        "the 'localhost' address to the host’s IPv4.\n\n" +
                        "These changes are made in the code where “localhost” is referenced."
        );
        instructionsAlert.showAndWait();
    }


}
