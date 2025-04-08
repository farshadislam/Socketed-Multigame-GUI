package org.seng.gui;

import javafx.event.ActionEvent;
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

public class CreateAccountVerificationController {

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
        returnButton.setOnAction(e -> returnToCreateAccount());

        // shift focus after entering a digit
        setupCodeField(code1, null, code2);
        setupCodeField(code2, code1, code3);
        setupCodeField(code3, code2, code4);
        setupCodeField(code4, code3, null); // doesn't move after last one
    }

    // text field moves after each input and handles backspace
    private void setupCodeField(TextField current, TextField prev, TextField next) {
        current.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 1) {
                current.setText(newValue.substring(0, 1));  // one character
            }
            if (!newValue.matches("\\d*")) {
                current.setText(newValue.replaceAll("[^\\d]", ""));  // only digits
            }
            if (newValue.length() == 1 && next != null) {
                next.requestFocus();  // move to next field
            }
            updateVerificationCode();
        });

        // handle backspace to move to previous field
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

    // update stored verification code
    private void updateVerificationCode() {
        verificationCode = code1.getText() + code2.getText() + code3.getText() + code4.getText();
        System.out.println("Current Code: " + verificationCode);
    }

    // compare verification code against stored
    private void handleVerification() {
        if (verificationCode.equals("1234")) {
            openSuccessPage();
        } else {
            displayErrorMessage("Incorrect verification code");
        }
    }

    // display error message for invalid code
    private void displayErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        // Flash message for 2 seconds and then clear
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> errorLabel.setVisible(false));
        pause.play();
    }

    private void openSuccessPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration-success.fxml"));
            Scene successScene = new Scene(fxmlLoader.load(), 700, 450);
            successScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Registration Success");
            stage.setScene(successScene);

            // Close current window
            Stage currentStage = (Stage) confirmButton.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // back button click handler - return to create account page
    private void returnToCreateAccount() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create-account.fxml"));
            Scene createAccountScene = new Scene(fxmlLoader.load(), 700, 450);
            createAccountScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Create Your Account");
            stage.setScene(createAccountScene);

            // Close the current verification code window
            Stage currentStage = (Stage) returnButton.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void confirmcode(ActionEvent event) {
        handleVerification();
    }
}
