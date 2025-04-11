package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
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
import java.util.Optional;
import java.util.UUID;
import org.seng.leaderboard_matchmaking.matchmaking.QueueMatchMaker;

public class GamesPageController {

    @FXML
    private ImageView backIcon, checkersIcon, ticTacToeIcon, connect4Icon;

    @FXML
    private ToggleButton playComputerButton, playOnlineButton;

    private String playMode = "Computer";

    @FXML
    public void initialize() {
        try {
            // load all the icon images
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            Image checkersImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/checkers.png"));
            Image ticTacToeImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/tictactoe.png"));
            Image connect4Image = new Image(getClass().getResourceAsStream("/org/seng/gui/images/connect4.png"));

            // assign them to the views
            backIcon.setImage(backImage);
            checkersIcon.setImage(checkersImage);
            ticTacToeIcon.setImage(ticTacToeImage);
            connect4Icon.setImage(connect4Image);

            // default toggle is "Computer"
            playComputerButton.setSelected(true);
            playOnlineButton.setSelected(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        try {
            // load the dashboard again
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene dashboardScene = new Scene(fxmlLoader.load(), 700, 450);
            dashboardScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            // switch the current stage to the dashboard
            Stage stage = (Stage) backIcon.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void togglePlayMode(ActionEvent event) {
        // this lets you pick either "Computer" or "Online" mode
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
            Alert modeChoice = new Alert(Alert.AlertType.CONFIRMATION);
            modeChoice.setTitle("Multiplayer Setup");
            modeChoice.setHeaderText("Do you want to host or join a game?");
            modeChoice.setContentText("Choose your option:");
            ButtonType hostButton = new ButtonType("Host Game");
            ButtonType joinButton = new ButtonType("Join Game");
            modeChoice.getButtonTypes().setAll(hostButton, joinButton);
            Optional<ButtonType> result = modeChoice.showAndWait();

            if (result.isPresent()) {
                String hostIP = "localhost";

                if (result.get() == hostButton) {
                    try {
                        new Thread(() -> {
                            try {
                                org.seng.leaderboard_matchmaking.matchmaking.QueueMatchMaker matchMaker = new org.seng.leaderboard_matchmaking.matchmaking.QueueMatchMaker();
                                matchMaker.startMatchmakingLoop();
                                org.seng.networking.SocketMatchServer server = new org.seng.networking.SocketMatchServer(12345);
                                server.start();
                            } catch (IOException e) {
                                System.err.println("Failed to start server: " + e.getMessage());
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hostIP = "localhost";

                } else if (result.get() == joinButton) {
                    TextInputDialog ipDialog = new TextInputDialog("192.168.0.x");
                    ipDialog.setTitle("Join Game");
                    ipDialog.setHeaderText("Enter Host IP Address");
                    ipDialog.setContentText("IP Address:");

                    Optional<String> ipResult = ipDialog.showAndWait();
                    if (ipResult.isPresent()) {
                        hostIP = ipResult.get().trim();
                    } else {
                        System.out.println("No IP entered. Cancelled join.");
                        return;
                    }
                }

                String username;
                if (GameDashboardController.player != null) {
                    username = GameDashboardController.player.getUsername();
                } else {
                    username = "Player_" + UUID.randomUUID().toString().substring(0, 5);
                }

                try {
                    // Connect to server
                    SocketGameClient client = new SocketGameClient(hostIP, 12345);
                    client.sendMessage(username);
                    client.sendMessage(getGameChoiceNumber(gameType));

                    if (gameType == GameType.TICTACTOE) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/seng/gui/waiting-room.fxml"));
                        Scene scene = new Scene(loader.load(), 700, 450);
                        scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
                        WaitingRoomController controller = loader.getController();
                        controller.init(username, gameType, client);
                        Stage stage = (Stage) checkersIcon.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/seng/gui/coming-soon.fxml"));
                        Scene scene = new Scene(loader.load(), 700, 450);
                        scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
                        Stage stage = (Stage) checkersIcon.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            // if we’re doing local play instead
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

                int width;
                int height;

                if (gameType == GameType.TICTACTOE) {
                    width = 700;
                    height = 450;
                } else if (gameType == GameType.CONNECT4) {
                    width = 825;
                    height = 525;
                } else {
                    width = 750;
                    height = 550;
                }

                Scene scene = new Scene(loader.load(), width, height);
                scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());

                if (gameType == GameType.CONNECT4 && "Computer".equals(playMode)) {
                    Connect4Controller controller = loader.getController();
                    controller.setAIBot(true);
                } else if (gameType == GameType.TICTACTOE && "Computer".equals(playMode)) {
                    TicTacToeController controller = loader.getController();
                    controller.setAIBot(true);
                } else if (gameType == GameType.CHECKERS && "Computer".equals(playMode)) {
                    CheckersBoardController controller = loader.getController();
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

    // this maps a GameType to what number we send the server
    private String getGameChoiceNumber(GameType gameType) {
        return switch (gameType) {
            case CHECKERS -> "1";
            case CONNECT4 -> "2";
            case TICTACTOE -> "3";
        };
    }
}
