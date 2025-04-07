package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import java.io.IOException;

public class GamesPageController {

    @FXML
    private ImageView backIcon, checkersIcon, ticTacToeIcon, connect4Icon;

    @FXML
    private ToggleButton playComputerButton, playOnlineButton;

    private String playMode = "Computer";  // Default play mode

    @FXML
    public void initialize() {
        try {
            // Load icons
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            backIcon.setImage(backImage);

            Image checkersImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/checkers.png"));
            checkersIcon.setImage(checkersImage);

            Image ticTacToeImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/tictactoe.png"));
            ticTacToeIcon.setImage(ticTacToeImage);

            Image connect4Image = new Image(getClass().getResourceAsStream("/org/seng/gui/images/connect4.png"));
            connect4Icon.setImage(connect4Image);

            // Set default play mode
            playComputerButton.setSelected(true);
            playOnlineButton.setSelected(false);
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
    public void togglePlayMode(ActionEvent event) {
        ToggleButton clickedButton = (ToggleButton) event.getSource();

        if (clickedButton == playComputerButton) {
            playComputerButton.setSelected(true);
            playOnlineButton.setSelected(false);
        } else if (clickedButton == playOnlineButton) {
            playComputerButton.setSelected(false);
            playOnlineButton.setSelected(true);
        }

        System.out.println("Selected mode: " + (playComputerButton.isSelected() ? "Play the Computer" : "Play Online"));
    }

}

