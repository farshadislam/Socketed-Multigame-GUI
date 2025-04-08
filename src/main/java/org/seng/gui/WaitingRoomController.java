package org.seng.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.seng.networking.SocketGameClient;
import org.seng.networking.leaderboard_matchmaking.GameType;
import java.io.IOException;
import java.net.URL;

public class WaitingRoomController {

    @FXML private Label gameTypeLabel;
    @FXML private ProgressBar matchProgressBar;
    @FXML private Label player1Name, player2Name;
    @FXML private Label player1Status, player2Status;
    @FXML private Circle player1StatusIndicator, player2StatusIndicator;
    @FXML private Label player1ReadyStatus, player2ReadyStatus;
    @FXML private Label matchTimer;
    @FXML private Button readyButton;
    @FXML private Label systemMessage;
    @FXML private Button leaveButton;

    private SocketGameClient client;
    private String username;
    private GameType gameType;
    private Timeline timerTimeline;
    private int secondsElapsed = 0;

    public void init(String username, GameType gameType, SocketGameClient client) {
        this.username = username;
        this.gameType = gameType;
        this.client = client;

        System.out.println("WaitingRoom init: " + username + ", type: " + gameType);

        gameTypeLabel.setText("Game Mode: " + gameType);
        player1Name.setText(username);
        player1Status.setText("\u2705 Loaded");
        player1StatusIndicator.setFill(Color.web("#a855f7"));

        startTimerAndProgressBar();
        listenForUpdates();

        leaveButton.setOnAction(e -> goBack());
    }

    private void applyFuturisticTheme() {
        Platform.runLater(() -> {
            Scene scene = readyButton.getScene();
            if (scene != null) {
                URL css = getClass().getResource("/org/seng/gui/waiting-room.css");
                if (css != null) {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(css.toExternalForm());
                } else {
                    systemMessage.setText("\u26A0 Style not found: waiting-room.css");
                }
            }
        });
    }

    private void startTimerAndProgressBar() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++;
            matchTimer.setText(String.format("00:%02d", secondsElapsed));
            double progress = Math.min(1.0, secondsElapsed / 30.0);
            matchProgressBar.setProgress(progress);
        }));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
        timerTimeline.play();

        applyFuturisticTheme();
    }

    private void listenForUpdates() {
        Thread listenerThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = client.receiveMessage()) != null) {
                    String finalMsg = msg;
                    Platform.runLater(() -> handleServerMessage(finalMsg));
                }
            } catch (IOException e) {
                Platform.runLater(() -> systemMessage.setText("Disconnected from server."));
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void handleServerMessage(String message) {
        if (message.equals("OPPONENT_READY")) {
            player2ReadyStatus.setText("Ready: Yes");
            player2Status.setText("\u2705 Loaded");
            player2StatusIndicator.setFill(Color.web("#a855f7"));
        } else if (message.equals("START_GAME")) {
            systemMessage.setText("Both players ready. Launching game...");
            if (timerTimeline != null) timerTimeline.stop();
            loadGameScene();
        } else if (message.startsWith("Welcome ")) {
            systemMessage.setText(message);
        }
    }

    private void loadGameScene() {
        try {
            String fxml = switch (gameType) {
                case CHECKERS -> "/org/seng/gui/checkers-game.fxml";
                case CONNECT4 -> "/org/seng/gui/connect4-game.fxml";
                case TICTACTOE -> "/org/seng/gui/tictactoe-game.fxml";
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("/org/seng/gui/styles/basic-styles.css").toExternalForm());

            Stage stage = (Stage) readyButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            systemMessage.setText("Failed to load game scene.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onReadyClicked() {
        try {
            client.sendMessage("READY");
            player1ReadyStatus.setText("Ready: Yes");
            systemMessage.setText("Waiting for opponent to be ready...");
        } catch (IOException e) {
            systemMessage.setText("Failed to send ready message.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onLeaveClicked() {
        try {
            if (client != null) {
                client.sendMessage("LEFT");
                client.close();
            }
            if (timerTimeline != null) timerTimeline.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URL fxmlUrl = getClass().getClassLoader().getResource("org/seng/gui/games-page.fxml");
            if (fxmlUrl == null) {
                System.err.println("\u26A0 games-page.fxml not found at: org/seng/gui/games-page.fxml");
                throw new IOException("games-page.fxml not found in classpath.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("/org/seng/gui/styles/basic-styles.css").toExternalForm());

            Stage stage = (Stage) leaveButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            systemMessage.setText("\u26A0 Failed to load games page");
            e.printStackTrace();
        }
    }

    private void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OMG Platform");
            stage.show();

            Stage currentStage = (Stage) leaveButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
