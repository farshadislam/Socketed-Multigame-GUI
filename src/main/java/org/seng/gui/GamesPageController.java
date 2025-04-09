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
import org.seng.networking.SocketGameClient;
import org.seng.networking.leaderboard_matchmaking.GameType;

import java.io.IOException;
import java.util.UUID;

public class GamesPageController {

    // these imageviews show the back icon and the game icons
    @FXML
    private ImageView backIcon, checkersIcon, ticTacToeIcon, connect4Icon;

    // these togglebuttons let you choose between computer mode and online mode
    @FXML
    private ToggleButton playComputerButton, playOnlineButton;

    // this stores the play mode; default is Computer mode
    private String playMode = "Computer";

    // this method loads all icons and sets the default mode
    @FXML
    public void initialize() {
        try {
            // this gets the back icon image
            Image backImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/backicon.png"));
            // this gets the checkers icon image
            Image checkersImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/checkers.png"));
            // this gets the tic tac toe icon image
            Image ticTacToeImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/tictactoe.png"));
            // this gets the connect4 icon image
            Image connect4Image = new Image(getClass().getResourceAsStream("/org/seng/gui/images/connect4.png"));

            // this loads the images into the views
            backIcon.setImage(backImage);
            checkersIcon.setImage(checkersImage);
            ticTacToeIcon.setImage(ticTacToeImage);
            connect4Icon.setImage(connect4Image);

            // this selects the computer mode by default
            playComputerButton.setSelected(true);
            playOnlineButton.setSelected(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // this loads the dashboard scene when the back icon is clicked
    @FXML
    public void goBack() {
        try {
            // this gets the dashboard fxml and loads the scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene dashboardScene = new Scene(fxmlLoader.load(), 700, 450);
            dashboardScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            // this gets the stage from backIcon and sets the dashboard scene
            Stage stage = (Stage) backIcon.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // this toggles the play mode between Computer and Online
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
        System.out.println("selected mode: " + playMode);
    }

    // this gets called when the checkers icon is clicked
    @FXML
    public void onCheckersClicked(MouseEvent event) {
        handleGameClick(GameType.CHECKERS);
    }

    // this gets called when the tic tac toe icon is clicked
    @FXML
    public void onTicTacToeClicked(MouseEvent event) {
        handleGameClick(GameType.TICTACTOE);
    }

    // this gets called when the connect4 icon is clicked
    @FXML
    public void onConnect4Clicked(MouseEvent event) {
        handleGameClick(GameType.CONNECT4);
    }

    // this handles the game click based on the game type chosen
    private void handleGameClick(GameType gameType) {
        if ("Online".equals(playMode)) {
            try {
                // this gets the username from the authenticated player if available
                String username;
                if (GameDashboardController.player != null) {
                    username = GameDashboardController.player.getUsername();
                    System.out.println("using authenticated player: " + username);
                } else {
                    // this gets a random username as a fallback if no player is authenticated
                    username = "Player_" + UUID.randomUUID().toString().substring(0, 5);
                    System.out.println("warning using random username");
                }

                // this connects to the multiplayer server
                SocketGameClient client = new SocketGameClient("localhost", 12345);
                // this sends the username to the server
                client.sendMessage(username);
                // this sends the game choice number to the server
                client.sendMessage(getGameChoiceNumber(gameType));

                // this loads the waiting room fxml and its scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/seng/gui/waiting-room.fxml"));
                Scene scene = new Scene(loader.load(), 700, 450);
                scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

                // this gets the waiting room controller and initializes it with the username game type and client
                WaitingRoomController controller = loader.getController();
                controller.init(username, gameType, client);

                // this gets the current stage from checkersIcon and sets the waiting room scene
                Stage stage = (Stage) checkersIcon.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else { // local play mode
            String fxmlFile = switch (gameType) {
                case CHECKERS -> "checkers-game.fxml";
                case TICTACTOE -> "tictactoe-game.fxml";
                case CONNECT4 -> "connect4-game.fxml";
            };

            try {
                // this loads the local game fxml file and creates the scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Scene scene = new Scene(loader.load(), 700, 450);
                scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

                // this gets the stage from checkersIcon and sets the local game scene
                Stage stage = (Stage) checkersIcon.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // this maps the game type to a number string to send to the server
    private String getGameChoiceNumber(GameType gameType) {
        return switch (gameType) {
            case CHECKERS -> "1";
            case CONNECT4 -> "2";
            case TICTACTOE -> "3";
        };
    }
}
