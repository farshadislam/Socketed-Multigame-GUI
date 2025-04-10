package org.seng.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.seng.authentication.Player;

public class FriendProfileController {

    @FXML private Label friendNameLabel;
    @FXML private Label friendLastOnlineLabel;
    @FXML private Label friendWinsLabel;
    @FXML private Label friendLossesLabel;
    @FXML private Label friendTiesLabel;
    @FXML private Label friendMatchesLabel;

    @FXML private ImageView backIcon;

    @FXML private TableView<ProfilePageController.GameStat> friendGameStatsTable;
    @FXML private TableColumn<ProfilePageController.GameStat, String> friendGameColumn;
    @FXML private TableColumn<ProfilePageController.GameStat, String> friendRankColumn;
    @FXML private TableColumn<ProfilePageController.GameStat, Integer> friendMmrColumn;
    @FXML private TableColumn<ProfilePageController.GameStat, Integer> friendGameWinsColumn;
    @FXML private TableColumn<ProfilePageController.GameStat, Integer> friendGameLossesColumn;
    @FXML private TableColumn<ProfilePageController.GameStat, Integer> friendGameTiesColumn;

    @FXML
    public void initialize() {
        friendGameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        friendRankColumn.setCellValueFactory(data -> data.getValue().rankProperty());
        friendMmrColumn.setCellValueFactory(data -> data.getValue().mmrProperty().asObject());
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

    public void setProfileData(String name, String lastOnline, int wins, int losses, int ties, Player friend) {
        friendNameLabel.setText(name);
        friendLastOnlineLabel.setText(lastOnline);
        friendWinsLabel.setText(String.valueOf(wins));
        friendLossesLabel.setText(String.valueOf(losses));
        friendTiesLabel.setText(String.valueOf(ties));

        int totalMatches = wins + losses + ties;
        friendMatchesLabel.setText(String.valueOf(totalMatches));

        friendGameStatsTable.setItems(FXCollections.observableArrayList(
                new ProfilePageController.GameStat("Checkers", String.valueOf(friend.getCheckersStats().getRank()), friend.getCheckersStats().getMMR(), friend.getCheckersStats().getWins(), friend.getCheckersStats().getLosses(), friend.getCheckersStats().get_ties()),
                new ProfilePageController.GameStat("Tic Tac Toe", String.valueOf(friend.getTicTacToeStats().getRank()), friend.getTicTacToeStats().getMMR(), friend.getTicTacToeStats().get_wins(), friend.getTicTacToeStats().getLosses(), friend.getTicTacToeStats().get_ties()),
                new ProfilePageController.GameStat("Connect 4", String.valueOf(friend.getConnect4Stats().getRank()), friend.getConnect4Stats().getMMR(), friend.getConnect4Stats().get_wins(), friend.getConnect4Stats().getLosses(), friend.getConnect4Stats().get_ties())
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
