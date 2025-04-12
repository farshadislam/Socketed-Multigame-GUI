package org.seng.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FriendWaitingRoomController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private ImageView yourAvatarImage;
    @FXML private ImageView friendAvatarImage;
    @FXML private Label yourNameLabel;
    @FXML private Label friendNameLabel;
    @FXML private Label statusLabel;
    @FXML private ComboBox<String> gameModeComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image userPlaceholder = new Image(getClass().getResourceAsStream("/org/seng/gui/images/avatar-placeholder1.png"));
            yourAvatarImage.setImage(userPlaceholder);

            Image friendPlaceholder = new Image(getClass().getResourceAsStream("/org/seng/gui/images/avatar-placeholder3.png"));
            friendAvatarImage.setImage(friendPlaceholder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameModeComboBox.getItems().addAll("Checkers", "Tic Tac Toe", "Connect Four", "Chess");
        gameModeComboBox.setValue("Tic Tac Toe");
    }

    public void init(String yourName, String friendName) {
        yourNameLabel.setText(yourName);
        friendNameLabel.setText(friendName);
        statusLabel.setText("Waiting for opponent...");
    }


    @FXML
    public void onStartGameClicked() {
        String selectedGame = gameModeComboBox.getValue();
        statusLabel.setText("Challenging " + friendNameLabel.getText() + " in " + selectedGame + "...");
    }

    @FXML
    public void onBackClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Friend-Profile.fxml"));
            Scene profileScene = new Scene(loader.load(), 700, 517);

            FriendProfileController profileController = loader.getController();
            if (yourNameLabel.getText() != null) {
                profileController.setCurrentUsername(yourNameLabel.getText());
            }
            if (friendNameLabel.getText() != null) {
                profileController.setProfileData(
                        friendNameLabel.getText(),
                        "Last online: Today",
                        5,
                        3,
                        1
                );
            }

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(profileScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
