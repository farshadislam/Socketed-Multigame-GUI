package org.seng.gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class TicTacToeController {
    // game board
    // buttons
    @FXML
    private Label welcomeText;
    protected void onHelloButtonClick() {
        welcomeText.setText("   Welcome to TicTacToe!");
    }




}
