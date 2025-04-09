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

public class GamesPageController {

    @FXML
    private ImageView backIcon, checkersIcon, ticTacToeIcon, connect4Icon;

    @FXML
    private ToggleButton playComputerButton, playOnlineButton;

    private String playMode = "Computer";  // Default play mode

    @FXML
    public void initialize() {
        try {
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            backIcon.setImage(backImage);

            Image checkersImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/checkers.png"));
            checkersIcon.setImage(checkersImage);

            Image ticTacToeImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/tictactoe.png"));
            ticTacToeIcon.setImage(ticTacToeImage);

            Image connect4Image = new Image(getClass().getResourceAsStream("/org/seng/gui/images/connect4.png"));
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
                // Generate a random username for this client
                String username = "Player_" + UUID.randomUUID().toString().substring(0, 5);

                // Connect to the server socket
                SocketGameClient client = new SocketGameClient("localhost", 12345);
                client.sendMessage(username);
                client.sendMessage(getGameChoiceNumber(gameType)); // send "1", "2", or "3"

                // Load the waiting room FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/seng/gui/waiting-room.fxml"));
                Scene scene = new Scene(loader.load(), 700, 450);
                scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

                // Get controller and pass info to it
                WaitingRoomController controller = loader.getController();
                controller.init(username, gameType, client);

                // Show the waiting room
                Stage stage = (Stage) checkersIcon.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Offline mode — just open the local game scene
            String fxmlFile = switch (gameType) {
                case CHECKERS -> "checkers-game.fxml";
                case TICTACTOE -> "tictactoe-game.fxml";
                case CONNECT4 -> "connect4-game.fxml";
            };
            if (gameType == GameType.CHECKERS) {
                CheckersBoard board = new CheckersBoard();
                CheckersPlayer[] players = new CheckersPlayer[]{new CheckersPlayer(GameDashboardController.player.getUsername(), GameDashboardController.player.getEmail(), GameDashboardController.player.getPassword()), new CheckersPlayer("AIBot", "test", "test")};
                GameDashboardController.checkersGame = new CheckersGame(board, players, 1);
                AIBotCheckers AIBot = new AIBotCheckers('O', GameDashboardController.checkersGame, board);
                GameDashboardController.checkersGame.players[1] = AIBot;
            } else if (gameType == GameType.TICTACTOE) {
                TicTacToeBoard board = new TicTacToeBoard();
                TicTacToePlayer[] players = new TicTacToePlayer[]{new TicTacToePlayer(GameDashboardController.player.getUsername(), GameDashboardController.player.getEmail(), GameDashboardController.player.getPassword()), new TicTacToePlayer("AIBot", "test", "test")};
                GameDashboardController.tictactoeGame = new TicTacToeGame(board, players, 1);
                AIBotTicTacToe AIBot = new AIBotTicTacToe('O', GameDashboardController.tictactoeGame, board);
                GameDashboardController.tictactoeGame.players[1] = AIBot;
            } else if (gameType == GameType.CONNECT4) {
                ConnectFourBoard board = new ConnectFourBoard();
                ConnectFourPlayer[] players = new ConnectFourPlayer[]{new ConnectFourPlayer(GameDashboardController.player.getUsername(), GameDashboardController.player.getEmail(), GameDashboardController.player.getPassword()), new ConnectFourPlayer("AIBot", "test", "test")};
                GameDashboardController.connectFourGame = new ConnectFourGame(board, players, 1);
                AIBotConnectFour AIBot = new AIBotConnectFour('O', GameDashboardController.connectFourGame, board);
                GameDashboardController.connectFourGame.players[1] = AIBot;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Scene scene = new Scene(loader.load(), 700, 450);
                scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

                Stage stage = (Stage) checkersIcon.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
