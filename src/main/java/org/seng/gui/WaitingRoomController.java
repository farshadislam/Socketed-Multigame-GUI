package org.seng.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.seng.networking.SocketGameClient;
import org.seng.networking.leaderboard_matchmaking.GameType;

import java.io.IOException;
import java.net.URL;

/**
 * this initializes a waitingroomcontroller that sets up and manages
 * this makes the display of the waiting room screen that players see when waiting for an opponent
 * this handles the initial display settings
 * this listens for messages from the server and transitions to the in-game scene.
 */
public class WaitingRoomController {

    // this does designate fxml elements for displaying game mode, progress and statuses.
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
    @FXML private ImageView player1Avatar, player2Avatar;

    // this holds the socketgameclient which is the network client connection for communication with the server
    private SocketGameClient client;
    // this stores the username of the current player
    private String username;
    // this represents the selected game type
    private GameType gameType;

    // this creates a timeline used to track and update match progress
    private Timeline timerTimeline;
    // this tracks seconds in the waiting room.
    private int secondsElapsed = 0;

    // this  represents the role assignment from the server
    // this  is store das a boolean so it can be null until assigned.
    private Boolean amIPlayerOne = null;

    // this defines a thread that  listens to incoming messages from the server
    private Thread listenerThread;

    /**
     * this initializes the waiting room.
     * this sets up the ui based on the game type and loads avatar images
     * this starts the timer and listener thread and wires the leave button
     *
     * @param username the player's username
     * @param gameType the selected game type.
     * @param client the socket client instance used for server communication
     */
    public void init(String username, GameType gameType, SocketGameClient client) {
        // this saves the parameters into the instance variables.
        this.username = username;
        this.gameType = gameType;
        this.client = client;

        // this logs the initialization for debugging
        System.out.println("[WaitingRoom DEBUG] init: username = " + username + ", gameType = " + gameType);

        // this uses the authenticated player data from the gamedashboardcontroller
        org.seng.authentication.Player authPlayer = GameDashboardController.player;
        if (authPlayer != null && authPlayer.getUsername().equals(username)) {
            System.out.println("[WaitingRoom DEBUG] using authenticated player data for " + username);
        }

        try {
            // this loads two avatar images for both players
            Image avatar1 = new Image(getClass().getResourceAsStream("/org/seng/gui/images/avatar-placeholder1.png"));
            Image avatar2 = new Image(getClass().getResourceAsStream("/org/seng/gui/images/avatar-placeholder2.png"));
            // this set these images up to the corresponding imageviews
            player1Avatar.setImage(avatar1);
            player2Avatar.setImage(avatar2);

            // this creates circular clipping for a round avatar appearance
            Circle clip1 = new Circle(45, 45, 45);
            player1Avatar.setClip(clip1);
            Circle clip2 = new Circle(45, 45, 45);
            player2Avatar.setClip(clip2);

            // this applies an effect to give a glowing outline to the avatar
            player1Avatar.setStyle("-fx-effect: dropshadow(three-pass-box, #a855f7, 8, 0.8, 0, 0);");
            player2Avatar.setStyle("-fx-effect: dropshadow(three-pass-box, #5e60ce, 8, 0.8, 0, 0);");
        } catch (Exception e) {
            // this logs errors in loading the avatar images
            System.err.println("[WaitingRoom DEBUG] failed to load placeholder avatar: " + e.getMessage());
            e.printStackTrace();
        }

        // this sets the game type label to display the selected game mode.
        gameTypeLabel.setText("Game mode: " + gameType);
        // this sets the player's name label to the username
        player1Name.setText(username);
        // this indicates that player one's status is loaded successfully
        player1Status.setText("\u2705 loaded");
        player1StatusIndicator.setFill(Color.web("#a855f7"));

        // this starts the timer  that indicates how long the player has been waiting
        startTimerAndProgressBar();
        // this starts the background thread to listen for server messages
        startListenerThread();

        // this wires the leave button so that clicking it will return the player to a previous screen
        leaveButton.setOnAction(e -> goBack());
    }

    // --- ui styling methods ---
    /**
     * this updates the styling of the scene by loading a css file.
     * this ensures that our waiting room gets the appropriate theme.
     */
    private void dashboardTheme() {
        Platform.runLater(() -> {
            // this retrieves the scene from the ready button
            Scene scene = readyButton.getScene();
            if (scene != null) {
                // this gets the css file for the waiting room
                URL css = getClass().getResource("/org/seng/gui/waiting-room.css");
                if (css != null) {
                    // this clears any previously loaded stylesheets and adds the new one
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(css.toExternalForm());
                } else {
                    // this shows a warning message in the system message label if the css is not found
                    systemMessage.setText("\u26A0 style not found: waiting-room.css");
                }
            }
        });
    }

    /**
     * this starts a timer that updates the progress bar and a match timer label.
     * this increments the timer every second
     */
    private void startTimerAndProgressBar() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++; // this increases elapsed seconds
            // this updates the timer label with the time
            matchTimer.setText(String.format("00:%02d", secondsElapsed));
            // this calculates the progress as a fraction
            double progress = Math.min(1.0, secondsElapsed / 30.0);
            matchProgressBar.setProgress(progress);
        }));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
        timerTimeline.play(); // this starts the timer immediately
        dashboardTheme(); // this applies the css theme.
    }

    /**
     * this starts a background thread that listens for messages from the server
     * this uses the socketgameclient's receivemessage() method to block until a message is received
     * this passes a message to the handeservermessage() method on the ui thread
     */
    private void startListenerThread() {
        listenerThread = new Thread(() -> {
            try {
                String msg;
                // this read messages until the connection is closed or the thread is interrupted
                while ((msg = client.receiveMessage()) != null) {
                    // this checks if the thread has been flagged as interrupted and then exits the loop
                    if (Thread.interrupted()) {
                        System.out.println("[WaitingRoom DEBUG] listenerthread detected interrupt, exiting.");
                        break;
                    }
                    final String finalMsg = msg;
                    // this ensures that ui updates happen on the javafx application thread
                    Platform.runLater(() -> handleServerMessage(finalMsg));
                }
            } catch (IOException e) {
                // this updates the system message if an i/o error occurs.
                Platform.runLater(() -> systemMessage.setText("disconnected from server."));
            }
            System.out.println("[WaitingRoom DEBUG] listenerthread has ended.");
        });
        listenerThread.setDaemon(true); // this marks the thread as daemon so it doesn't prevent the app from closing
        listenerThread.start();
    }

    /**
     * this processes messages received from the server
     * this updates the ui and set the roles and transitions to the game scene depending on the message content
     *
     * @param message the message received from the server.
     */
    private void handleServerMessage(String message) {
        System.out.println("[WaitingRoom DEBUG] received message: " + message);

        if (message.contains("IS_PLAYER_ONE:")) {
            // this extracts the role information from the message
            int index = message.indexOf("IS_PLAYER_ONE:") + "IS_PLAYER_ONE:".length();
            String roleStr = message.substring(index).trim();
            // this determines if the role string starts with true
            boolean roleValue = roleStr.startsWith("true");
            amIPlayerOne = roleValue;
            System.out.println("[WaitingRoom DEBUG] parsed role: " + roleValue);
        } else if (message.startsWith("OPPONENT_NAME:")) {
            // this extracts the opponents name from the message.
            String oppName = message.substring("OPPONENT_NAME:".length()).trim();
            player2Name.setText(oppName);
            // this updates the system message to show the match information
            systemMessage.setText("matched with: " + oppName);
            System.out.println("[WaitingRoom DEBUG] opponent name set to: " + oppName);
        } else if (message.equals("START_GAME")) {
            // this updates the ui when the server sends a start game command
            systemMessage.setText("both players ready. launching game...");
            if (timerTimeline != null) {
                timerTimeline.stop(); // this stops the timer as the match is starting
            }
            // this send acknowledgement to the server that this client is closing the waiting room
            try {
                client.sendMessage("WAITING_ROOM_CLOSED");
                System.out.println("[WaitingRoom DEBUG] sent waiting_room_closed ack.");
            } catch (IOException ex) {
                System.err.println("[WaitingRoom DEBUG] failed to send waiting_room_closed ack.");
                ex.printStackTrace();
            }
            // this interrupts the listener thread to stop processing further waiting room messages
            if (listenerThread != null && listenerThread.isAlive()) {
                System.out.println("[WaitingRoom DEBUG] attempting to interrupt listener thread.");
                listenerThread.interrupt();
            }
            // this transitions to the game scene.
            loadGameScene();
        } else if (message.equals("OPPONENT_READY")) {
            // this updates the opponent status when they indicate they are ready
            player2ReadyStatus.setText("ready: yes");
            player2Status.setText("\u2705 loaded");
            player2StatusIndicator.setFill(Color.web("#a855f7"));
        } else if (message.startsWith("Welcome ")) {
            // this displays any welcome messages from the server
            systemMessage.setText(message);
        } else {
            // this logs any other messages
            System.out.println("[WaitingRoom DEBUG] unhandled message: " + message);
        }
    }

    /**
     * this loads the appropriate game scene once both players are ready
     * this selects the fxml file based on the game type then applies the theme and passes role information
     */
    private void loadGameScene() {
        try {
            String fxml;
            // this determines which game fxml to load
            if (gameType == GameType.TICTACTOE) {
                fxml = "/org/seng/gui/onlineTicTacToe.fxml";
            } else if (gameType == GameType.CONNECT4) {
                fxml = "/org/seng/gui/onlineConnect4.fxml";
            } else if (gameType == GameType.CHECKERS) {
                fxml = "/org/seng/gui/checkers-game.fxml";
            } else {
                throw new IOException("unsupported game type.");
            }

            // this loads the fxml file for the selected game scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), 700, 450);
            // this applies the basic styles to the new scene
            scene.getStylesheets().add(getClass().getResource("/org/seng/gui/basic-styles.css").toExternalForm());

            // this gets the current stage from the ready button and sets the scene to the new game scene
            Stage stage = (Stage) readyButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // this determines the players role from the server.
            boolean role;
            if (amIPlayerOne != null) {
                role = amIPlayerOne;
            } else {
                role = false;
            }
            System.out.println("[WaitingRoom DEBUG] loading game scene with amIPlayerOne = " + role);

            // this initializes the controller if the game type is tictactoe
            if (gameType == GameType.TICTACTOE) {
                OnlineTicTacToeController controller = loader.getController();
                controller.init(
                        client,
                        new org.seng.gamelogic.tictactoe.OnlineTicTacToeGame(
                                new org.seng.gamelogic.tictactoe.TicTacToeBoard(),
                                new org.seng.gamelogic.tictactoe.TicTacToePlayer[]{
                                        new org.seng.gamelogic.tictactoe.TicTacToePlayer(username, "", ""),
                                        new org.seng.gamelogic.tictactoe.TicTacToePlayer(player2Name.getText(), "", "")
                                },
                                1
                        ),
                        role
                );
                System.out.println("[WaitingRoom DEBUG] game scene loaded. role passed: " + role);
            }
            // this allowa additional game types to be handled in the same way
        } catch (IOException e) {
            // this displays an error message if loading the game scene fails
            systemMessage.setText("failed to load game scene.");
            e.printStackTrace();
        }
    }

    /**
     * this handles the event when the ready button is clicked
     * this notifies the server that the player is prepared to begin
     */
    @FXML
    public void onReadyClicked() {
        try {
            client.sendMessage("READY");  // this sends the ready message to the server
            player1ReadyStatus.setText("ready: yes");  // this updates the ui to show the player is ready
            systemMessage.setText("waiting for opponent to be ready...");
            System.out.println("[WaitingRoom DEBUG] sent READY");
        } catch (IOException e) {
            systemMessage.setText("failed to send ready message.");
            e.printStackTrace();
        }
    }

    /**
     * this handles the event when the leave button is clicked
     * this notifies the server that the player is leaving
     */
    @FXML
    public void onLeaveClicked() {
        try {
            if (client != null) {
                client.sendMessage("LEFT");  // this notifies the server that the player has left
                client.close();              // this closes the client connection
            }
            if (timerTimeline != null) {
                timerTimeline.stop();        // this stops the progress timer
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // this loads the games page fxml to return to the main games list
            URL fxmlUrl = getClass().getClassLoader().getResource("org/seng/gui/games-page.fxml");
            if (fxmlUrl == null) {
                System.err.println("\u26A0 games-page.fxml not found");
                throw new IOException("games-page.fxml not found.");
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("/org/seng/gui/styles/basic-styles.css").toExternalForm());
            Stage stage = (Stage) leaveButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            systemMessage.setText("\u26A0 failed to load games page.");
            e.printStackTrace();
        }
    }

    /**
     * this allows the leave button to go back to the dashboard
     * this loads a new scene from game-dashboard.fxml.
     */
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OMG Platform");
            stage.show();
            Stage currentStage = (Stage) leaveButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
