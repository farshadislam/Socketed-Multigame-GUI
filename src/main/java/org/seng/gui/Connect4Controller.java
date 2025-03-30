package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Connect4Controller {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("   Welcome to Connect4! Make a four in a row to win!");
    }
}
