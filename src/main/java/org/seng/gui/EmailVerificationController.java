package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.io.IOException;

public class EmailVerificationController {

    @FXML
    private TextField code1, code2, code3, code4;

    @FXML
    private Button confirmButton, returnButton;

    @FXML
    private Label errorLabel;

    private String verificationCode = "";  // store 4 digit code

    @FXML
    public void initialize() {
        confirmButton.setOnAction(e -> handleVerification());
        returnButton.setOnAction(e -> returnToSettings());

        // Setup code field behavior
        setupCodeField(code1, null, code2);
        setupCodeField(code2, code1, code3);
        setupCodeField(code3, code2, code4);
        setupCodeField(code4, code3, null);
    }

    private void setupCodeField(TextField current, TextField prev, TextField next) {
        current.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 1) {
                current.setText(newValue.substring(0, 1));
            }
            if (!newValue.matches("\\d*")) {
                current.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() == 1 && next != null) {
                next.requestFocus();
            }
            updateVerificationCode();
        });

        current.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case BACK_SPACE:
                    if (current.getText().isEmpty() && prev != null) {
                        prev.requestFocus();
                        prev.clear();
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private void updateVerificationCode() {
        verificationCode = code1.getText() + code2.getText() + code3.getText() + code4.getText();
        System.out.println("Current Code: " + verificationCode);
    }

    private void handleVerification() {
        if (verificationCode.equals("1234")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("email-update-success.fxml"));
                Scene successScene = new Scene(fxmlLoader.load(), 600, 400);
                successScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
                Stage stage = new Stage();
                stage.setTitle("Email Updated");
                stage.setScene(successScene);
                stage.show();

                // Close the current verification window
                Stage currentStage = (Stage) confirmButton.getScene().getWindow();
                currentStage.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            displayErrorMessage("Incorrect verification code");
        }
    }

    private void displayErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> errorLabel.setVisible(false));
        pause.play();
    }

    private void returnToSettings() {
        Stage currentStage = (Stage) returnButton.getScene().getWindow();
        currentStage.close();
    }
}
