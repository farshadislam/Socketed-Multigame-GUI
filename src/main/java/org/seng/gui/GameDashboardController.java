package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.animation.RotateTransition;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Optional;
import org.seng.authentication.*;
import org.seng.networking.SocketGameClient;
import org.seng.networking.leaderboard_matchmaking.GameType;

import static org.seng.gui.HelloApplication.database;

import static org.seng.gui.HelloApplication.database;

public class GameDashboardController {

    @FXML
    private ImageView statsIcon, profileIcon, playIcon, settingsIcon, logoutIcon;

    @FXML
    private VBox viewStatsPane, profilePane, playGamesPane;
    private HomePage homePage;
    public static Player player;
    public static Settings setting;
    private Player localPlayer; // this holds the player who just joined (used in waiting room UI)
    private GameType selectedGame; // this stores the game type they selected (Checkers, etc.)
    private SocketGameClient client; // this is the networking connection client for this player

    @FXML private Label player1Name;
    @FXML private Label gameTypeLabel;


    @FXML
    public void initialize() {
        try {
            Image statsImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/leaderboardicon.png"));
            Image profileImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/usericon.png"));
            Image playImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/playicon.png"));
            Image settingsImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/gear.png"));
            Image logoutImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/logouticon.png"));

            statsIcon.setImage(statsImage);
            profileIcon.setImage(profileImage);
            playIcon.setImage(playImage);
            settingsIcon.setImage(settingsImage);
            logoutIcon.setImage(logoutImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setHomePage(HomePage homePage){
        this.homePage = homePage;
    }

    public void setPlayer(Player player1){
        GameDashboardController.player = player1;
    }

    // this method is called when the waiting room scene is loaded
    // it gives the controller the needed info to update UI properly
    public void init(Player player, GameType gameType, SocketGameClient client) {
        this.localPlayer = player; // store the current player for reference
        this.selectedGame = gameType; // store the selected game
        this.client = client; // keep a reference to the networking client

        player1Name.setText(player.getUsername()); // show name in UI
        gameTypeLabel.setText("Game Mode: " + gameType.name()); // update game mode text
    }

    @FXML
    public void openLeaderboardPage() {
        openNewPage("leaderboard-page.fxml", "Leaderboard");
    }

    @FXML
    public void openProfilePage() {
        openNewPage("profile-page.fxml", "Your Profile");
    }

    @FXML
    public void openGamesPage() {
        openNewPage("games-page.fxml", "Play Games");
    }

    private void openNewPage(String fxmlFile, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene newScene = new Scene(fxmlLoader.load(), 700, 450);
            newScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(newScene);

            // Close current window
            Stage currentStage = (Stage) viewStatsPane.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {
        showLogoutConfirmation();
    }

    private void showLogoutConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Your current session will be closed.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            openWelcomePage();
        }
    }

    private void openWelcomePage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome-page.fxml"));
            Scene welcomeScene = new Scene(fxmlLoader.load(), 700, 450);
            welcomeScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Welcome");
            stage.setScene(welcomeScene);

            Stage currentStage = (Stage) logoutIcon.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void openSettings() {
        // Initializing the Setting object
        setting = new Settings(player, database);
        System.out.println("success");
        animateGear();
    }

    private void animateGear() {
        RotateTransition rotate = new RotateTransition(Duration.seconds(0.25), settingsIcon);
        rotate.setByAngle(360);
        rotate.setCycleCount(2);
        rotate.setOnFinished(event -> openNewPage("settings-page.fxml", "Settings"));
        rotate.play();
    }
}