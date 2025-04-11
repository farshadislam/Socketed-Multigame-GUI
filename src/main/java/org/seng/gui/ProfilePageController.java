package org.seng.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static org.seng.gui.GameDashboardController.player;

public class ProfilePageController {

    @FXML private Label usernameLabel;
    @FXML private Label lastOnlineLabel;
    @FXML private Label myWinsLabel;
    @FXML private Label myLossesLabel;
    @FXML private Label myTiesLabel;
    @FXML private Label myTotalGamesLabel;
    @FXML private Label profileTitleText;

    @FXML private TableView<GameStat> gameStatsTable;
    @FXML private TableColumn<GameStat, String> gameColumn;
    @FXML private TableColumn<GameStat, Integer> winsColumn;
    @FXML private TableColumn<GameStat, Integer> lossesColumn;
    @FXML private TableColumn<GameStat, Integer> tiesColumn;
    @FXML private TableColumn<GameStat, String> rankColumn;
    @FXML private TableColumn<GameStat, Integer> mmrColumn;

    @FXML private TableView<GameHistory> gameHistoryTable;
    @FXML private TableColumn<GameHistory, String> historyGameColumn;
    @FXML private TableColumn<GameHistory, String> opponentColumn;

    @FXML
    private Button searchProfilesButton;
    @FXML
    private ImageView backIcon;

    @FXML
    public void initialize() {
        try {
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            backIcon.setImage(backImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        backIcon.setOnMouseClicked(event -> goBack());

        searchProfilesButton.setOnAction(e -> openSearchProfile());

        setProfileData(player.getUsername());
    }

    public void setProfileData(String playerName) {

        usernameLabel.setText(playerName);
        profileTitleText.setText(playerName + "'s Profile");
        lastOnlineLabel.setText("Last Online: Just now"); // connect with networking
        myTotalGamesLabel.setText(String.valueOf(player.getTotalGamesPlayed()));
        myWinsLabel.setText(String.valueOf(player.getTotalWins()));
        myLossesLabel.setText(String.valueOf(player.getTotalLosses()));
        myTiesLabel.setText(String.valueOf(player.getTotalTies()));

        gameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        winsColumn.setCellValueFactory(data -> data.getValue().winsProperty().asObject());
        lossesColumn.setCellValueFactory(data -> data.getValue().lossesProperty().asObject());
        tiesColumn.setCellValueFactory(data -> data.getValue().tiesProperty().asObject());
        rankColumn.setCellValueFactory(data -> data.getValue().rankProperty());
        mmrColumn.setCellValueFactory(data -> data.getValue().mmrProperty().asObject());


        gameStatsTable.setItems(FXCollections.observableArrayList(
                new GameStat("Checkers", String.valueOf(player.getCheckersStats().getRank()), player.getCheckersStats().getMMR(), player.getCheckersStats().get_wins(), player.getCheckersStats().getLosses(), player.getConnect4Stats().get_ties()),
                new GameStat("Tic Tac Toe", String.valueOf(player.getTicTacToeStats().getRank()), player.getTicTacToeStats().getMMR(), player.getTicTacToeStats().get_wins(), player.getTicTacToeStats().getLosses(), player.getTicTacToeStats().get_ties()),
                new GameStat("Connect 4", String.valueOf(player.getConnect4Stats().getRank()), player.getConnect4Stats().getMMR(), player.getConnect4Stats().getWins(), player.getConnect4Stats().getLosses(), player.getConnect4Stats().get_ties())
        ));

        historyGameColumn.setCellValueFactory(data -> data.getValue().gameName);
        opponentColumn.setCellValueFactory(data -> data.getValue().opponentType);

        gameHistoryTable.setItems(FXCollections.observableArrayList(
                new GameHistory(String.valueOf(player.getLast5MatchesObject().getGameTypeAt(0)), player.getLast5MatchesObject().getPlayerAt(0)),
                new GameHistory(String.valueOf(player.getLast5MatchesObject().getGameTypeAt(1)), player.getLast5MatchesObject().getPlayerAt(1)),
                new GameHistory(String.valueOf(player.getLast5MatchesObject().getGameTypeAt(2)), player.getLast5MatchesObject().getPlayerAt(2)),
                new GameHistory(String.valueOf(player.getLast5MatchesObject().getGameTypeAt(3)), player.getLast5MatchesObject().getPlayerAt(3)),
                new GameHistory(String.valueOf(player.getLast5MatchesObject().getGameTypeAt(4)), player.getLast5MatchesObject().getPlayerAt(4))
        ));
    }

    private void openSearchProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search-profile.fxml"));
            Scene scene = new Scene(loader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = (Stage) searchProfilesButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Search Profiles");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene dashboardScene = new Scene(fxmlLoader.load(), 700, 450);
            dashboardScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = (Stage) backIcon.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static class GameStat {
        private final SimpleStringProperty gameName;
        private final SimpleIntegerProperty wins;
        private final SimpleIntegerProperty losses;
        private final SimpleIntegerProperty ties;
        private final SimpleStringProperty rank;
        private final SimpleIntegerProperty mmr;

        public GameStat(String game, String rank, int mmr, int wins, int losses, int ties) {
            this.gameName = new SimpleStringProperty(game);
            this.wins = new SimpleIntegerProperty(wins);
            this.losses = new SimpleIntegerProperty(losses);
            this.ties = new SimpleIntegerProperty(ties);
            this.rank = new SimpleStringProperty(rank);
            this.mmr = new SimpleIntegerProperty(mmr);

        }

        public SimpleStringProperty gameProperty() {
            return gameName;
        }

        public SimpleIntegerProperty winsProperty() {
            return wins;
        }

        public SimpleIntegerProperty lossesProperty() {
            return losses;
        }

        public SimpleIntegerProperty tiesProperty() {
            return ties;
        }

        public SimpleStringProperty rankProperty() {
            return rank;
        }

        public SimpleIntegerProperty mmrProperty() {
            return mmr;
        }
    }

    public static class GameHistory {
        private final SimpleStringProperty gameName;
        private final SimpleStringProperty opponentType;

        public GameHistory(String game, String opponentType) {
            this.gameName = new SimpleStringProperty(game);
            this.opponentType = new SimpleStringProperty(opponentType);
        }

        public SimpleStringProperty gameNameProperty() {
            return gameName;
        }

        public SimpleStringProperty opponentTypeProperty() {
            return opponentType;
        }
    }
}
