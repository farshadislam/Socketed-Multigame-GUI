package org.seng.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import static org.seng.gui.GameDashboardController.player;

public class ProfilePageController {

    @FXML private Label usernameLabel;
    @FXML private Label lastOnlineLabel;
    @FXML private Label myWinsLabel;
    @FXML private Label myLossesLabel;
    @FXML private Label myTiesLabel;
    @FXML private TableView<GameStat> gameStatsTable;
    @FXML private TableColumn<GameStat, String> gameColumn;
    @FXML private TableColumn<GameStat, Integer> winsColumn;
    @FXML private TableColumn<GameStat, Integer> lossesColumn;
    @FXML private TableColumn<GameStat, Integer> tiesColumn;
    @FXML private Button searchProfilesButton;

    @FXML
    public void initialize() {
        // Set up user profile dummy data
        usernameLabel.setText(player.getUsername());
        lastOnlineLabel.setText("Last Online: Just now");
        myWinsLabel.setText(String.valueOf(player.getTotalWins()));
        myLossesLabel.setText(String.valueOf(player.getTotalLosses()));
        myTiesLabel.setText(String.valueOf(player.getTotalTies()));

        gameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        winsColumn.setCellValueFactory(data -> data.getValue().winsProperty().asObject());
        lossesColumn.setCellValueFactory(data -> data.getValue().lossesProperty().asObject());
        tiesColumn.setCellValueFactory(data -> data.getValue().tiesProperty().asObject());
        gameStatsTable.setItems(FXCollections.observableArrayList(
                new GameStat("Checkers", player.getCheckersStats().getWins(), player.getCheckersStats().getLosses(), player.getCheckersStats().get_ties()),
                new GameStat("Tic Tac Toe", player.getTicTacToeStats().getWins(), player.getTicTacToeStats().getLosses(), player.getTicTacToeStats().get_ties()),
                new GameStat("Connect 4", player.getConnect4Stats().getWins(), player.getConnect4Stats().getLosses(), player.getConnect4Stats().get_ties())
        ));

        searchProfilesButton.setOnAction(e -> openSearchProfile());
    }

    private void openSearchProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search-profile.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Search Profiles");
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setProfileData(String playerName) {

    }
    public static class GameStat {
        private final SimpleStringProperty game;
        private final SimpleIntegerProperty wins;
        private final SimpleIntegerProperty losses;
        private final SimpleIntegerProperty ties;
        public GameStat(String game, int wins, int losses, int ties) {
            this.game = new SimpleStringProperty(game);
            this.wins = new SimpleIntegerProperty(wins);
            this.losses = new SimpleIntegerProperty(losses);
            this.ties = new SimpleIntegerProperty(ties);
        }
        public SimpleStringProperty gameProperty() { return game; }
        public SimpleIntegerProperty winsProperty() { return wins; }
        public SimpleIntegerProperty lossesProperty() { return losses; }
        public SimpleIntegerProperty tiesProperty() { return ties; }
    }
}
