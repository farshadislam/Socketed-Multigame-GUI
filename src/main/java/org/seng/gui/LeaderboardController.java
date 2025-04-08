package org.seng.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import org.seng.authentication.Player; for later use

import java.io.IOException;

public class LeaderboardController {

    @FXML
    private ComboBox<String> gameTypeComboBox;

    @FXML
    private ListView<String> playerListView;

    @FXML
    private Label firstPlayerName, secondPlayerName, thirdPlayerName;

    @FXML
    private ImageView backIcon, firstPlaceIcon, secondPlaceIcon, thirdPlaceIcon;

    @FXML
    public void initialize() {
        try {
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            backIcon.setImage(backImage);

            Image firstImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/first.png"));
            firstPlaceIcon.setImage(firstImage);

            Image secondImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/second.png"));
            secondPlaceIcon.setImage(secondImage);

            Image thirdImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/third.png"));
            thirdPlaceIcon.setImage(thirdImage);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        gameTypeComboBox.setPromptText("Choose Game");

        gameTypeComboBox.setItems(FXCollections.observableArrayList("Checkers", "Tic Tac Toe", "Connect 4"));

        playerListView.getItems().addAll("Player 4", "Player 5", "Player 6", "Player 7", "Player 8");

        firstPlayerName.setText("JoyThief22");
        secondPlayerName.setText("FishEnjoyer2");
        thirdPlayerName.setText("notAMinor66");

        playerListView.setOnMouseClicked(event -> {
            String selectedPlayer = playerListView.getSelectionModel().getSelectedItem();
            if (selectedPlayer != null) {
                openPlayerProfile(selectedPlayer);
            }
        });
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

    private void openPlayerProfile(String playerName) {
        try {
            FXMLLoader loader = new  FXMLLoader(getClass().getResource("profile-page.fxml"));
            Parent root = loader.load();
            ProfilePageController controller = loader.getController();
            controller.setProfileData(playerName);
            Scene scene = new Scene(root, 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage stage = (Stage) playerListView.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(playerName + "'s Profile");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    public void changeGameType() {
        String gameType = gameTypeComboBox.getValue();
        System.out.println("Selected game type: " + gameType);
    }
}
