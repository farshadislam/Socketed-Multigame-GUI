package org.seng.gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class TicTacToeController {
    // game board
    // buttons
    @FXML
    private Label welcomeText;


    @FXML
    public void initialize() {
        System.out.println("Game initialized");
    }


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("   Welcome to TicTacToe!");
    }

    @FXML
    protected void resetGame() {
        welcomeText.setText("Welcome to TicTacToe!");
        System.out.println("Game reset");

    }


}
