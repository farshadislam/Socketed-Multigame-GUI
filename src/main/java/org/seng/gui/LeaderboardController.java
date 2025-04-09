package org.seng.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.leaderboard_matchmaking.Leaderboard;
//import org.seng.authentication.Player; for later use

import java.io.IOException;
import java.util.List;

public class LeaderboardController {

//    UNCOMMENT WHEN TESTING (this is the fake info for test)
//    private final CredentialsDatabase credentialsDatabase = new CredentialsDatabase();
//
//       {
//        // Initialize artificial players with varied stats
//        Player p1 = new Player("AlphaWolf", "alpha@example.com", "pass");
//        p1.getCheckersStats().setWins(10);
//        p1.getCheckersStats().setGamesPlayed(15);
//        p1.getTicTacToeStats().setWins(2);
//        p1.getTicTacToeStats().setGamesPlayed(3);
//        p1.getConnect4Stats().setWins(1);
//        p1.getConnect4Stats().setGamesPlayed(1);
//
//        Player p2 = new Player("BetaBird", "beta@example.com", "pass");
//        p2.getCheckersStats().setWins(4);
//        p2.getCheckersStats().setGamesPlayed(5);
//        p2.getTicTacToeStats().setWins(7);
//        p2.getTicTacToeStats().setGamesPlayed(10);
//        p2.getConnect4Stats().setWins(3);
//        p2.getConnect4Stats().setGamesPlayed(5);
//
//        Player p3 = new Player("GammaGamer", "gamma@example.com", "pass");
//        p3.getCheckersStats().setWins(1);
//        p3.getTicTacToeStats().setWins(8);
//        p3.getConnect4Stats().setWins(2);
//
//        Player p4 = new Player("DeltaDestroyer", "delta@example.com", "pass");
//        p4.getCheckersStats().setWins(6);
//        p4.getTicTacToeStats().setWins(1);
//        p4.getConnect4Stats().setWins(6);
//
//        Player p5 = new Player("EpsilonElite", "epsilon@example.com", "pass");
//        p5.getCheckersStats().setWins(2);
//        p5.getTicTacToeStats().setWins(9);
//        p5.getConnect4Stats().setWins(1);
//
//        Player p6 = new Player("ZetaZen", "zeta@example.com", "pass");
//        p6.getCheckersStats().setWins(0);
//        p6.getTicTacToeStats().setWins(3);
//        p6.getConnect4Stats().setWins(10);
//
//        // Add all to credentials database
//        credentialsDatabase.addNewPlayer(p1.getUsername(), p1);
//        credentialsDatabase.addNewPlayer(p2.getUsername(), p2);
//        credentialsDatabase.addNewPlayer(p3.getUsername(), p3);
//        credentialsDatabase.addNewPlayer(p4.getUsername(), p4);
//        credentialsDatabase.addNewPlayer(p5.getUsername(), p5);
//        credentialsDatabase.addNewPlayer(p6.getUsername(), p6);
//    }






    @FXML
    private ComboBox<String> gameTypeComboBox;

    @FXML
    private ListView<String> playerListView;

    @FXML
    private Label firstPlayerName, secondPlayerName, thirdPlayerName;

    @FXML
    private ImageView backIcon, firstPlaceIcon, secondPlaceIcon, thirdPlaceIcon;

    //COMMENT THESE NEXT 2 LINES WHEN TESTING
    private CredentialsDatabase credentialsDatabase; // Injected from main

    public void setCredentialsDatabase(CredentialsDatabase db) {
        this.credentialsDatabase = db;
    }

    @FXML
    public void initialize() {
        try {
            backIcon.setImage(new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png")));
            firstPlaceIcon.setImage(new Image(getClass().getResourceAsStream("/org/seng/gui/images/first.png")));
            secondPlaceIcon.setImage(new Image(getClass().getResourceAsStream("/org/seng/gui/images/second.png")));
            thirdPlaceIcon.setImage(new Image(getClass().getResourceAsStream("/org/seng/gui/images/third.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        gameTypeComboBox.setPromptText("Choose Game");
        gameTypeComboBox.setItems(FXCollections.observableArrayList("Checkers", "Tic Tac Toe", "Connect 4"));
        gameTypeComboBox.setOnAction(event -> changeGameType());

        // Optional: Load a default game leaderboard on startup
        gameTypeComboBox.getSelectionModel().select("Checkers");
        changeGameType();
        playerListView.getItems().addAll("Player 4", "Player 5", "Player 6", "Player 7", "Player 8");

        // Click on list item to open player profile
        firstPlayerName.setText("JoyThief22");
        secondPlayerName.setText("FishEnjoyer2");
        thirdPlayerName.setText("notAMinor66");

        playerListView.setOnMouseClicked(event -> {
            String selectedPlayer = playerListView.getSelectionModel().getSelectedItem();
            if (selectedPlayer != null) {
                String username = selectedPlayer.split(" - ")[0];
                openPlayerProfile(username);
            }
        });
    }

    @FXML
    public void changeGameType() {
        String gameType = gameTypeComboBox.getValue();
        if (gameType == null || credentialsDatabase == null) return;

        List<Player> ranked = Leaderboard.getRankedPlayersByGame(credentialsDatabase, gameType);

        playerListView.getItems().clear();

        int index = 0;
        for (Player player : ranked) {
            int wins = switch (gameType.toLowerCase()) {
                case "checkers" -> player.getCheckersStats().get_wins();
                case "tic tac toe" -> player.getTicTacToeStats().get_wins();
                case "connect 4" -> player.getConnect4Stats().get_wins();
                default -> 0;
            };

            playerListView.getItems().add(player.getUsername() + " - Wins: " + wins);

            if (index == 0) firstPlayerName.setText(player.getUsername());
            else if (index == 1) secondPlayerName.setText(player.getUsername());
            else if (index == 2) thirdPlayerName.setText(player.getUsername());

            index++;
        }

        // Clear podium if fewer than 3 players
        if (ranked.size() < 3) {
            if (ranked.size() < 1) firstPlayerName.setText("-");
            if (ranked.size() < 2) secondPlayerName.setText("-");
            if (ranked.size() < 3) thirdPlayerName.setText("-");
        }
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
