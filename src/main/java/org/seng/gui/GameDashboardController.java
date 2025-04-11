package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import org.seng.gamelogic.checkers.CheckersGame;
import org.seng.gamelogic.connectfour.ConnectFourGame;
import org.seng.gamelogic.tictactoe.TicTacToeGame;

import static org.seng.gui.HelloApplication.database;


import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.seng.authentication.Player;
import org.seng.authentication.Settings;
import org.seng.gamelogic.checkers.CheckersGame;
import org.seng.gamelogic.connectfour.ConnectFourGame;
import org.seng.gamelogic.tictactoe.TicTacToeGame;

import java.io.IOException;
import java.util.Optional;

import static org.seng.gui.HelloApplication.database;

public class GameDashboardController {

    @FXML
    private ImageView statsIcon, profileIcon, playIcon, settingsIcon, logoutIcon;
    @FXML
    private VBox viewStatsPane, profilePane, playGamesPane;

    private HomePage homePage;
    public static Player player;
    public static Settings setting;

    // Game logic instances (for offline or local play)
    public static CheckersGame checkersGame;
    public static TicTacToeGame tictactoeGame;
    public static ConnectFourGame connectFourGame;

    @FXML
    public void initialize() {
        try {
            // Load icons for leaderboard, profile, play, settings, logout.
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

    public void setHomePage(HomePage homePage) {
        this.homePage = homePage;
    }

    public void setPlayer(Player player1) {
        GameDashboardController.player = player1;
    }

    @FXML
    public void openLeaderboardPage() {
        openNewPage("leaderboard-page.fxml", "Leaderboard");
    }

    @FXML
    public void openProfilePage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile-page.fxml"));
            Scene newScene = new Scene(fxmlLoader.load(), 700, 550);
            newScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Your Profile");
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

            // Close current window (assumes viewStatsPane is part of the current scene)
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
        // Initialize the Settings object using the current player and the database reference
        setting = new Settings(player, database);
        System.out.println("Settings initialized successfully.");
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
