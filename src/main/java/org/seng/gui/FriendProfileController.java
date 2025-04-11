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
import javafx.stage.Stage;
import org.seng.authentication.Player;

public class FriendProfileController {

    @FXML private Label friendNameLabel;
    @FXML private Label friendLastOnlineLabel;
    @FXML private Label friendWinsLabel;
    @FXML private Label friendLossesLabel;
    @FXML private Label friendTiesLabel;
    @FXML private Label friendMatchesLabel;
    @FXML private Label friendProfileTitleText;

    @FXML private ImageView backIcon;

    @FXML private TableView<FriendGameStat> friendGameStatsTable;
    @FXML private TableColumn<FriendGameStat, String> friendGameColumn;
    @FXML private TableColumn<FriendGameStat, String> friendWinsColumn;
    @FXML private TableColumn<FriendGameStat, String> friendLossesColumn;
    @FXML private TableColumn<FriendGameStat, String> friendTiesColumn;
    @FXML private TableColumn<FriendGameStat, String> friendrankColumn;
    @FXML private TableColumn<FriendGameStat, String> friendmmrColumn;

    @FXML private TableView<FriendGameHistory> friendGameHistoryTable;
    @FXML private TableColumn<FriendGameHistory, String> friendHistoryGameColumn;
    @FXML private TableColumn<FriendGameHistory, String> friendOpponentColumn;

    @FXML
    public void initialize() {
        try {
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            backIcon.setImage(backImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        backIcon.setOnMouseClicked(event -> goBack());

        // Set up TableColumn for FriendGameStat properties
        friendGameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        friendWinsColumn.setCellValueFactory(data -> data.getValue().winsProperty().asString());
        friendLossesColumn.setCellValueFactory(data -> data.getValue().lossesProperty().asString());
        friendTiesColumn.setCellValueFactory(data -> data.getValue().tiesProperty().asString());
        friendrankColumn.setCellValueFactory(data -> data.getValue().rankProperty());
        friendmmrColumn.setCellValueFactory(data -> data.getValue().mmrProperty().asString());

        friendHistoryGameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        friendOpponentColumn.setCellValueFactory(data -> data.getValue().opponentProperty());
    }

    public void setProfileData(String name, String lastOnline, int wins, int losses, int ties, Player friend) {
        friendNameLabel.setText(name);
        friendLastOnlineLabel.setText(lastOnline);
        friendWinsLabel.setText(String.valueOf(wins));
        friendLossesLabel.setText(String.valueOf(losses));
        friendTiesLabel.setText(String.valueOf(ties));
        friendMatchesLabel.setText(String.valueOf(wins + losses + ties));
        friendProfileTitleText.setText(name + "'s Profile");

        friendGameStatsTable.setItems(FXCollections.observableArrayList(
                new FriendGameStat("Checkers", String.valueOf(friend.getCheckersStats().getRank()), friend.getCheckersStats().getMMR(), friend.getCheckersStats().getWins(), friend.getCheckersStats().getLosses(), friend.getCheckersStats().get_ties()),
                new FriendGameStat("Tic Tac Toe", String.valueOf(friend.getTicTacToeStats().getRank()), friend.getTicTacToeStats().getMMR(), friend.getTicTacToeStats().get_wins(), friend.getTicTacToeStats().getLosses(), friend.getTicTacToeStats().get_ties()),
                new FriendGameStat("Connect 4", String.valueOf(friend.getConnect4Stats().getRank()), friend.getConnect4Stats().getMMR(), friend.getConnect4Stats().get_wins(), friend.getConnect4Stats().getLosses(), friend.getConnect4Stats().get_ties())
        ));

        friendGameHistoryTable.setItems(FXCollections.observableArrayList(
                new FriendGameHistory(String.valueOf(friend.getLast5MatchesObject().getGameTypeAt(0)), friend.getLast5MatchesObject().getPlayerAt(0)),
                new FriendGameHistory(String.valueOf(friend.getLast5MatchesObject().getGameTypeAt(1)), friend.getLast5MatchesObject().getPlayerAt(1)),
                new FriendGameHistory(String.valueOf(friend.getLast5MatchesObject().getGameTypeAt(2)), friend.getLast5MatchesObject().getPlayerAt(2)),
                new FriendGameHistory(String.valueOf(friend.getLast5MatchesObject().getGameTypeAt(3)), friend.getLast5MatchesObject().getPlayerAt(3)),
                new FriendGameHistory(String.valueOf(friend.getLast5MatchesObject().getGameTypeAt(4)), friend.getLast5MatchesObject().getPlayerAt(4))
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

    public static class FriendGameStat {
        private final SimpleStringProperty game;
        private final SimpleIntegerProperty wins;
        private final SimpleIntegerProperty losses;
        private final SimpleIntegerProperty ties;
        private final SimpleStringProperty rank;
        private final SimpleIntegerProperty mmr;

        public FriendGameStat(String game, String rank, int mmr, int wins, int losses, int ties) {
            this.game = new SimpleStringProperty(game);
            this.wins = new SimpleIntegerProperty(wins);
            this.losses = new SimpleIntegerProperty(losses);
            this.ties = new SimpleIntegerProperty(ties);
            this.rank = new SimpleStringProperty(rank);
            this.mmr = new SimpleIntegerProperty(mmr);
        }

        public SimpleStringProperty gameProperty() { return game; }
        public SimpleIntegerProperty winsProperty() { return wins; }
        public SimpleIntegerProperty lossesProperty() { return losses; }
        public SimpleIntegerProperty tiesProperty() { return ties; }
        public SimpleStringProperty rankProperty() {
            return rank;
        }
        public SimpleIntegerProperty mmrProperty() {
            return mmr;
        }
    }

    public static class FriendGameHistory {
        private final SimpleStringProperty game;
        private final SimpleStringProperty opponent;

        public FriendGameHistory(String game, String opponent) {
            this.game = new SimpleStringProperty(game);
            this.opponent = new SimpleStringProperty(opponent);
        }

        public SimpleStringProperty gameProperty() { return game; }
        public SimpleStringProperty opponentProperty() { return opponent; }
    }
}
