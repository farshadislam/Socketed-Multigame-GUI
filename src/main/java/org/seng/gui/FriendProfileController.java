package org.seng.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class FriendProfileController {

    @FXML private Label friendNameLabel;
    @FXML private Label friendLastOnlineLabel;
    @FXML private Label friendWinsLabel;
    @FXML private Label friendLossesLabel;
    @FXML private Label friendTiesLabel;
    @FXML private TableView<ProfilePageController.GameStat> friendGameStatsTable;
    @FXML private TableColumn<ProfilePageController.GameStat, String> friendGameColumn;
    @FXML private TableColumn<ProfilePageController.GameStat, Integer> friendGameWinsColumn;
    @FXML private TableColumn<ProfilePageController.GameStat, Integer> friendGameLossesColumn;
    @FXML private TableColumn<ProfilePageController.GameStat, Integer> friendGameTiesColumn;

    @FXML
    public void initialize() {
        friendGameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        friendGameWinsColumn.setCellValueFactory(data -> data.getValue().winsProperty().asObject());
        friendGameLossesColumn.setCellValueFactory(data -> data.getValue().lossesProperty().asObject());
        friendGameTiesColumn.setCellValueFactory(data -> data.getValue().tiesProperty().asObject());
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
}
