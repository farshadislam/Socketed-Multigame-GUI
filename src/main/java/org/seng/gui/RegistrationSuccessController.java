package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class RegistrationSuccessController {

    @FXML
    private Button backToLoginButton;

    @FXML
    public void initialize() {
        backToLoginButton.setOnAction(e -> returnToLogin());
    }

    private void returnToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OMG Platform");
            WelcomePageController controller = fxmlLoader.getController();
            //controller.setLoginPage(this.loginPage);
            stage.show();

            Stage currentStage = (Stage) backToLoginButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
