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
import org.seng.gamelogic.checkers.AIBotCheckers;
import org.seng.gamelogic.checkers.CheckersBoard;
import org.seng.gamelogic.checkers.CheckersGame;
import org.seng.gamelogic.checkers.CheckersPlayer;
import org.seng.gamelogic.connectfour.AIBotConnectFour;
import org.seng.gamelogic.connectfour.ConnectFourBoard;
import org.seng.gamelogic.connectfour.ConnectFourGame;
import org.seng.gamelogic.connectfour.ConnectFourPlayer;
import org.seng.gamelogic.tictactoe.AIBotTicTacToe;
import org.seng.gamelogic.tictactoe.TicTacToeBoard;
import org.seng.gamelogic.tictactoe.TicTacToeGame;
import org.seng.gamelogic.tictactoe.TicTacToePlayer;

import org.seng.networking.SocketGameClient;
import org.seng.networking.leaderboard_matchmaking.GameType;

import java.io.IOException;
import java.net.URL;

import static org.seng.networking.leaderboard_matchmaking.GameType.*;

public class WaitingRoomController {

    // all the UI elements hooked from the FXML file
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

    private SocketGameClient client; // the actual connection to server
    private String username; // store the name of this player
    private GameType gameType; // store which game is being played

    private Timeline timerTimeline; // to update the match waiting timer
    private int secondsElapsed = 0; // keeps track of time passed

    // called when the waiting room gets loaded
    public void init(String username, GameType gameType, SocketGameClient client) {
        this.username = username;
        this.gameType = gameType;
        this.client = client;

        gameTypeLabel.setText("Game Mode: " + gameType); // shows the selected mode
        player1Name.setText(username); // shows current player's name
        player1Status.setText("\u2705 Loaded"); // shows loaded status with checkmark
        player1StatusIndicator.setFill(Color.web("#a855f7")); // purple indicator to show ready

        startTimerAndProgressBar(); // starts ticking timer + bar
        listenForUpdates(); // begins listening for incoming server messages
    }

    // tries to load the waiting-room.css for the theme
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

    // starts the match countdown and progress bar
    private void startTimerAndProgressBar() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++; // every second passed
            matchTimer.setText(String.format("00:%02d", secondsElapsed)); // update label
            double progress = Math.min(1.0, secondsElapsed / 30.0); // fills bar over 30 sec
            matchProgressBar.setProgress(progress); // visually update the bar
        }));
        timerTimeline.setCycleCount(Timeline.INDEFINITE); // keeps going until stopped
        timerTimeline.play(); // actually starts it

        applyFuturisticTheme(); // makes it look nice
    }

    // starts a background thread to handle any messages coming from server
    private void listenForUpdates() {
        Thread listenerThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = client.receiveMessage()) != null) {
                    String finalMsg = msg;
                    Platform.runLater(() -> handleServerMessage(finalMsg)); // does UI work on main thread
                }
            } catch (IOException e) {
                Platform.runLater(() -> systemMessage.setText("Disconnected from server.")); // shows disconnect msg
            }
        });
        listenerThread.setDaemon(true); // won’t keep app alive if closed
        listenerThread.start(); // go!
    }

    // reacts to what the server sends us
    private void handleServerMessage(String message) {
        if (message.equals("OPPONENT_READY")) {
            player2ReadyStatus.setText("Ready: Yes");
            player2Status.setText("\u2705 Loaded");
            player2StatusIndicator.setFill(Color.web("#a855f7")); // gives them their own purple dot
        } else if (message.equals("START_GAME")) {
            systemMessage.setText("Both players ready. Launching game...");
            if (timerTimeline != null) timerTimeline.stop(); // stops countdown
            loadGameScene(); // switch screen
        } else if (message.startsWith("Welcome ")) {
            systemMessage.setText(message); // sends greeting message
        }
    }

    // loads the actual game scene based on which game was selected
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
            stage.show(); // finally shows the actual game screen
        } catch (IOException e) {
            systemMessage.setText("Failed to load game scene.");
            e.printStackTrace(); // something’s off
        }
    }

    // when the user clicks ready
    @FXML
    public void onReadyClicked() {
        try {
            client.sendMessage("READY"); // tells server this player is ready
            player1ReadyStatus.setText("Ready: Yes"); // updates label
            systemMessage.setText("Waiting for opponent to be ready..."); // user feedback
        } catch (IOException e) {
            systemMessage.setText("Failed to send ready message."); // this is when something went wrong
            e.printStackTrace();
        }
    }

    // when the user decides to leave the waiting room
    @FXML
    public void onLeaveClicked() {
        try {
            if (client != null) {
                client.sendMessage("LEFT"); // this lets the server know we dipped
                client.close(); // this closes connection
            }
            if (timerTimeline != null) timerTimeline.stop(); // this stops timer too
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
            stage.show(); // goes back to the games selection screen
        } catch (IOException e) {
            systemMessage.setText("\u26A0 Failed to load games page");
            e.printStackTrace();
        }
    }
}
