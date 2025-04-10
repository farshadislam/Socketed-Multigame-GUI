package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
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
import java.util.UUID;


import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import org.seng.gamelogic.checkers.AIBotCheckers;
import org.seng.gamelogic.checkers.CheckersGame;
import org.seng.gamelogic.tictactoe.AIBotTicTacToe;
import org.seng.gamelogic.tictactoe.TicTacToeGame;
import org.seng.gamelogic.connectfour.AIBotConnectFour;
import org.seng.gamelogic.connectfour.ConnectFourGame;
import org.seng.networking.SocketGameClient;
import org.seng.networking.leaderboard_matchmaking.GameType;
import java.io.IOException;
import java.util.UUID;

public class GamesPageController {

    @FXML
    private ImageView backIcon, checkersIcon, ticTacToeIcon, connect4Icon;
    @FXML
    private ToggleButton playComputerButton, playOnlineButton;

    private String playMode = "Computer"; // default mode is local play

    @FXML
    public void initialize() {
        try {
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            Image checkersImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/checkers.png"));
            Image ticTacToeImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/tictactoe.png"));
            Image connect4Image = new Image(getClass().getResourceAsStream("/org/seng/gui/images/connect4.png"));
            backIcon.setImage(backImage);
            checkersIcon.setImage(checkersImage);
            ticTacToeIcon.setImage(ticTacToeImage);
            connect4Icon.setImage(connect4Image);

            playComputerButton.setSelected(true);
            playOnlineButton.setSelected(false);
        } catch (Exception ex) {
            ex.printStackTrace();
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

    @FXML
    public void togglePlayMode(ActionEvent event) {
        ToggleButton clickedButton = (ToggleButton) event.getSource();
        if (clickedButton == playComputerButton) {
            playComputerButton.setSelected(true);
            playOnlineButton.setSelected(false);
            playMode = "Computer";
        } else if (clickedButton == playOnlineButton) {
            playComputerButton.setSelected(false);
            playOnlineButton.setSelected(true);
            playMode = "Online";
        }
        System.out.println("Selected mode: " + playMode);
    }

    @FXML
    public void onCheckersClicked(MouseEvent event) {
        handleGameClick(GameType.CHECKERS);
    }

    @FXML
    public void onTicTacToeClicked(MouseEvent event) {
        handleGameClick(GameType.TICTACTOE);
    }

    @FXML
    public void onConnect4Clicked(MouseEvent event) {
        handleGameClick(GameType.CONNECT4);
    }

    private void handleGameClick(GameType gameType) {
        if ("Online".equals(playMode)) {
            try {
                // Use the authenticated player if available; otherwise generate a random name.
                String username;
                if (GameDashboardController.player != null) {
                    username = GameDashboardController.player.getUsername();
                    System.out.println("Using authenticated player: " + username);
                } else {
                    username = "Player_" + UUID.randomUUID().toString().substring(0, 5);
                    System.out.println("Warning: using random username");
                }
                // Connect to the multiplayer server and send username and game choice.
                SocketGameClient client = new SocketGameClient("10.13.180.57", 12345);
                client.sendMessage(username);
                client.sendMessage(getGameChoiceNumber(gameType));

                // Load the waiting room UI.
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/seng/gui/waiting-room.fxml"));
                Scene scene = new Scene(loader.load(), 700, 450);
                scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
                WaitingRoomController controller = loader.getController();
                controller.init(username, gameType, client);
                Stage stage = (Stage) checkersIcon.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // Local play
            String cssFile = switch (gameType) {
                case CHECKERS -> "checkerstyles.css";
                case TICTACTOE -> "basic-styles.css";
                case CONNECT4 -> "connectfourstyles.css";
            };
            String fxmlFile = switch (gameType) {
                case CHECKERS -> "checkersBoard.fxml";
                case TICTACTOE -> "tictactoe.fxml";
                case CONNECT4 -> "connect4-board.fxml";
            };

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Scene scene = new Scene(loader.load(), 700, 450);
                scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
                // For local play, you can set up AI if desired.
                if (gameType == GameType.CONNECT4 && "Computer".equals(playMode)) {
                    Connect4Controller controller = loader.getController();
                    controller.setAIBot(true);
                } else if (gameType == GameType.TICTACTOE && "Computer".equals(playMode)) {
                    TicTacToeController controller = loader.getController();
                    controller.setAIBot(true);
                }
                Stage stage = (Stage) checkersIcon.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Maps a GameType to the corresponding number string to send to the server.
    private String getGameChoiceNumber(GameType gameType) {
        return switch (gameType) {
            case CHECKERS -> "1";
            case CONNECT4 -> "2";
            case TICTACTOE -> "3";
        };
    }
}
