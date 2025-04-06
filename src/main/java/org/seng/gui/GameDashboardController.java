package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
public class GameDashboardController {

    @FXML
    private Label dashboardLabel;

    @FXML
    private ImageView statsIcon;

    @FXML
    private ImageView profileIcon;

    @FXML
    private ImageView playIcon;

    @FXML
    private VBox viewStatsPane;

    @FXML
    private VBox profilePane;

    @FXML
    private VBox playGamesPane;


    @FXML
    public void initialize() {
        // load images per section
        Image statsImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/leaderboardicon.png"));
        Image profileImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/usericon.png"));
        Image playImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/playicon.png"));

        // set icons
        statsIcon.setImage(statsImage);
        profileIcon.setImage(profileImage);
        playIcon.setImage(playImage);
    }

    // Method to open the Leaderboard Page
    @FXML
    private void openLeaderboardPage() {
        openNewPage("leaderboard-page.fxml", "Leaderboard");
    }

    // Method to open the Profile Page
    @FXML
    private void openProfilePage() {
        openNewPage("profile-page.fxml", "Your Profile");
    }

    // Method to open the Games Page
    @FXML
    private void openGamesPage() {
        openNewPage("games-page.fxml", "Play Games");
    }

    // Helper method to open a new page
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
}
