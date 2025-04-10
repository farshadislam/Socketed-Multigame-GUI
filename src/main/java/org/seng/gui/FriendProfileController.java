package org.seng.gui;

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

    @FXML private TableView<FriendGameHistory> friendGameHistoryTable;
    @FXML private TableColumn<FriendGameHistory, String> friendHistoryGameColumn;
    @FXML private TableColumn<FriendGameHistory, String> friendResultColumn;
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

        friendGameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        friendWinsColumn.setCellValueFactory(data -> data.getValue().winsProperty());
        friendLossesColumn.setCellValueFactory(data -> data.getValue().lossesProperty());
        friendTiesColumn.setCellValueFactory(data -> data.getValue().tiesProperty());

        friendHistoryGameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        friendResultColumn.setCellValueFactory(data -> data.getValue().resultProperty());
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
//                new ProfilePageController.GameStat("Checkers", String.valueOf(friend.getCheckersStats().getRank()), friend.getCheckersStats().getMMR(), friend.getCheckersStats().getWins(), friend.getCheckersStats().getLosses(), friend.getCheckersStats().get_ties()),
//                new ProfilePageController.GameStat("Tic Tac Toe", String.valueOf(friend.getTicTacToeStats().getRank()), friend.getTicTacToeStats().getMMR(), friend.getTicTacToeStats().get_wins(), friend.getTicTacToeStats().getLosses(), friend.getTicTacToeStats().get_ties()),
//                new ProfilePageController.GameStat("Connect 4", String.valueOf(friend.getConnect4Stats().getRank()), friend.getConnect4Stats().getMMR(), friend.getConnect4Stats().get_wins(), friend.getConnect4Stats().getLosses(), friend.getConnect4Stats().get_ties())
                new FriendGameStat("Checkers", "2", "1", "0"),
                new FriendGameStat("Tic Tac Toe", "3", "1", "1"),
                new FriendGameStat("Connect 4", "0", "1", "0")
        ));

        friendGameHistoryTable.setItems(FXCollections.observableArrayList(
                new FriendGameHistory("Checkers", "Loss", "Online"),
                new FriendGameHistory("Tic Tac Toe", "Win", "BOT"),
                new FriendGameHistory("Connect 4", "Tie", "BOT"),
                new FriendGameHistory("Tic Tac Toe", "Loss", "Online"),
                new FriendGameHistory("Checkers", "Win", "BOT")
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
        private final SimpleStringProperty wins;
        private final SimpleStringProperty losses;
        private final SimpleStringProperty ties;

        public FriendGameStat(String game, String wins, String losses, String ties) {
            this.game = new SimpleStringProperty(game);
            this.wins = new SimpleStringProperty(wins);
            this.losses = new SimpleStringProperty(losses);
            this.ties = new SimpleStringProperty(ties);
        }

        public SimpleStringProperty gameProperty() { return game; }
        public SimpleStringProperty winsProperty() { return wins; }
        public SimpleStringProperty lossesProperty() { return losses; }
        public SimpleStringProperty tiesProperty() { return ties; }
    }

    public static class FriendGameHistory {
        private final SimpleStringProperty game;
        private final SimpleStringProperty result;
        private final SimpleStringProperty opponent;

        public FriendGameHistory(String game, String result, String opponent) {
            this.game = new SimpleStringProperty(game);
            this.result = new SimpleStringProperty(result);
            this.opponent = new SimpleStringProperty(opponent);
        }

        public SimpleStringProperty gameProperty() { return game; }
        public SimpleStringProperty resultProperty() { return result; }
        public SimpleStringProperty opponentProperty() { return opponent; }
    }
}
