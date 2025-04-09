package org.seng.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

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

    @FXML private Button searchProfilesButton;
    @FXML private ImageView backIcon;

    @FXML
    public void initialize() {
        try {
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            backIcon.setImage(backImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        backIcon.setOnMouseClicked(event -> goBack());

        setProfileData("MyUsername");

        searchProfilesButton.setOnAction(e -> openSearchProfile());
    }

    public void setProfileData(String playerName) {
        usernameLabel.setText(playerName);
        profileTitleText.setText(playerName + "'s Profile");
        lastOnlineLabel.setText("Last Online: Just now");

        int wins = 5;  // player.getTotalWins();
        int losses = 2;
        int ties = 1;
        int total = wins + losses + ties; // player.getTotalGamesPlayed();

        myWinsLabel.setText(String.valueOf(wins));
        myLossesLabel.setText(String.valueOf(losses));
        myTiesLabel.setText(String.valueOf(ties));
        myTotalGamesLabel.setText(String.valueOf(total));

        gameColumn.setCellValueFactory(data -> data.getValue().gameProperty());
        winsColumn.setCellValueFactory(data -> data.getValue().winsProperty().asObject());
        lossesColumn.setCellValueFactory(data -> data.getValue().lossesProperty().asObject());
        tiesColumn.setCellValueFactory(data -> data.getValue().tiesProperty().asObject());

        gameStatsTable.setItems(FXCollections.observableArrayList(
                new ProfilePageController.GameStat("Checkers", "Silver", 3, 3, 1, 1), // player.getCheckersStats().get_wins(),  player.getCheckersStats().get_losses(),player.getCheckersStats().get_ties())
                new ProfilePageController.GameStat("Tic Tac Toe", "Gold", 2, 2, 2, 0),
                new ProfilePageController.GameStat("Connect 4", "Bronze", 1, 1, 0, 2)
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
        private final SimpleStringProperty game;
        private final SimpleStringProperty rank;
        private final SimpleIntegerProperty mmr;

        private final SimpleIntegerProperty wins;
        private final SimpleIntegerProperty losses;
        private final SimpleIntegerProperty ties;

        public GameStat(String game, String rank, int mmr, int wins, int losses, int ties)
        {
            this.game = new SimpleStringProperty(game);
            this.rank = new SimpleStringProperty(rank);
            this.mmr = new SimpleIntegerProperty(mmr);
            this.wins = new SimpleIntegerProperty(wins);
            this.losses = new SimpleIntegerProperty(losses);
            this.ties = new SimpleIntegerProperty(ties);

        }

        public SimpleStringProperty gameProperty() {
            return game;
        }

        public SimpleStringProperty rankProperty() {
            return rank;
        }
        public SimpleIntegerProperty mmrProperty() {
            return mmr;
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
    }
}
