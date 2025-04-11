package org.seng.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.seng.networking.SocketGameClient;
import org.seng.networking.leaderboard_matchmaking.GameType;

import java.io.IOException;
import java.util.UUID;

public class MatchmakingController {

    @FXML
    private Label statusLabel;

    private GameType selectedGame;

    public void setSelectedGameType(GameType gameType) {
        this.selectedGame = gameType;
        System.out.println("Joining online for: " + gameType);

        statusLabel.setText("Waiting for an opponent in " + gameType + "...");

        new Thread(this::joinGame).start();
    }

    private void joinGame() {
        try {
            // Fake data just for simulation
            String username = "Player_" + UUID.randomUUID().toString().substring(0, 5);

            // Set up client socket
            SocketGameClient client = new SocketGameClient("localhost", 12345);
            client.sendMessage(username); // send username
            client.sendMessage(getGameChoiceNumber(selectedGame)); // send game selection (1/2/3)

            // Load waiting room
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/seng/gui/waiting-room.fxml"));
            Scene waitingScene = new Scene(loader.load());
            WaitingRoomController waitingController = loader.getController();
            waitingController.init(username, selectedGame, client);

            Platform.runLater(() -> {
                Stage stage = (Stage) statusLabel.getScene().getWindow();
                stage.setScene(waitingScene);
                stage.show();
            });

        } catch (IOException e) {
            Platform.runLater(() -> statusLabel.setText("Error: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private String getGameChoiceNumber(GameType gameType) {
        return switch (gameType) {
            case CHECKERS -> "1";
            case CONNECT4 -> "2";
            case TICTACTOE -> "3";
        };
    }
}