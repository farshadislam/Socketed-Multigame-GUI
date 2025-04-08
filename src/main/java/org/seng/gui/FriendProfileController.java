package org.seng.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FriendProfileController {

    @FXML
    private Label friendNameLabel;
    @FXML
    private Label friendLastOnlineLabel;
    @FXML
    private Label friendWinsLabel;
    @FXML
    private Label friendLossesLabel;
    @FXML
    private Label friendTiesLabel;
    @FXML
    private TableView<ProfilePageController.GameStat> friendGameStatsTable;
    @FXML
    private TableColumn<ProfilePageController.GameStat, String> friendGameColumn;
    @FXML
    private TableColumn<ProfilePageController.GameStat, Integer> friendGameWinsColumn;
    @FXML
    private TableColumn<ProfilePageController.GameStat, Integer> friendGameLossesColumn;
    @FXML
    private TableColumn<ProfilePageController.GameStat, Integer> friendGameTiesColumn;
    @FXML
    private ImageView backIcon;


    @FXML
    public void initialize() {
        friendGameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        friendGameWinsColumn.setCellValueFactory(data -> data.getValue().winsProperty().asObject());
        friendGameLossesColumn.setCellValueFactory(data -> data.getValue().lossesProperty().asObject());
        friendGameTiesColumn.setCellValueFactory(data -> data.getValue().tiesProperty().asObject());
        try {
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            backIcon.setImage(backImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        backIcon.setOnMouseClicked(event -> goBack());

    }

    public void setProfileData(String name, String lastOnline, int wins, int losses, int ties) {
        friendNameLabel.setText(name);
        friendLastOnlineLabel.setText(lastOnline);
        friendWinsLabel.setText(String.valueOf(wins));
        friendLossesLabel.setText(String.valueOf(losses));
        friendTiesLabel.setText(String.valueOf(ties));
        friendGameStatsTable.setItems(FXCollections.observableArrayList(
                new ProfilePageController.GameStat("Checkers", 3, 1, 0),
                new ProfilePageController.GameStat("Tic Tac Toe", 2, 2, 1),
                new ProfilePageController.GameStat("Connect 4", 0, 0, 0)
        ));
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search-profile.fxml"));
            Scene scene = new Scene(loader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage stage = (Stage) backIcon.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
