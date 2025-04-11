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

    // this designates fxml elements for displaying game mode, progress and statuses.
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
    @FXML private Button chatButton;
    private GameChatController chatController;  // this stores a reference to the opened chat controller


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

    private boolean iAmReady = false;
    private boolean opponentIsReady = false;


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
                        String cleanMsg = msg.trim();
                        Platform.runLater(() -> handleServerMessage(cleanMsg));
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
        System.out.println("[WaitingRoom DEBUG] received message: " + message);  // this logs the incoming server message

        if (message.contains("IS_PLAYER_ONE:")) {
            // this processes the role assignment message
            int index = message.indexOf("IS_PLAYER_ONE:") + "IS_PLAYER_ONE:".length();
            String rolestring = message.substring(index).trim();  // this extracts the substring which indicates the player role
            boolean rolevalue = rolestring.startsWith("true");  // this interprets the role string as a boolean
            amIPlayerOne = rolevalue;  // this saves the role assignment
            System.out.println("[WaitingRoom DEBUG] parsed role: " + rolevalue);

        } else if (message.startsWith("OPPONENT_NAME:")) {
            // this processes the opponents name
            String opponentname = message.substring("OPPONENT_NAME:".length()).trim();
            player2Name.setText(opponentname);  // this sets the ui element for the opponents name
            systemMessage.setText("matched with: " + opponentname);  // this informs the user about the opponent matchup
            System.out.println("[WaitingRoom DEBUG] opponent name set to: " + opponentname);

        } else if (message.equals("START_GAME")) {
            // this condiiton is to prepare the matchup
            systemMessage.setText("both players ready. launching game...");
            if (timerTimeline != null) timerTimeline.stop();  // this stops the waiting timer
            try {
                client.sendMessage("WAITING_ROOM_CLOSED");  // this informs the server that waiting room is closed
                System.out.println("[WaitingRoom DEBUG] sent waiting_room_closed ack.");
            } catch (IOException ex) {
                System.err.println("[WaitingRoom DEBUG] failed to send waiting_room_closed ack.");
                ex.printStackTrace();
            }

            try {
                Thread.sleep(200); // this sets a delay to flush the messages
            } catch (InterruptedException e) {
                System.out.println("[WaitingRoom DEBUG] interrupted while waiting before scene switch.");
            }

            if (listenerThread != null && listenerThread.isAlive()) {
                System.out.println("[WaitingRoom DEBUG] attempting to interrupt listener thread.");
                listenerThread.interrupt();  // this interrupts the listener thread
            }

            loadGameScene();  // this transitions to the actual game scene

        } else if (message.equals("OPPONENT_READY")) {
            // Handle opponent readiness.
            opponentIsReady = true;
            player2ReadyStatus.setText("ready: yes");  // this updates the opponents ready status on the UI
            player2Status.setText("\u2705 loaded");      // this marks the status as loaded
            player2StatusIndicator.setFill(Color.web("#a855f7"));  // this sets a visual indicator for the opponent
            checkIfBothReady();  // this checks if both players are ready

        } else if (message.equals("CLOSE_CHAT")) {
            // this closes the chat window
            System.out.println("[WaitingRoom DEBUG] received CLOSE_CHAT");
            if (chatController != null) {
                chatController.handleCloseChat();  // this triggers the chat controller to close the chat
                chatController = null;  // this releases the reference
            }

            // this resets the readiness after closing chat
            iAmReady = false;
            opponentIsReady = false;
            player1ReadyStatus.setText("ready: no");  // this resets the readiness status for the current player
            player2ReadyStatus.setText("ready: no");  // this resets the readiness status for opponent
            systemMessage.setText("Chat closed. Re-ready to launch the game.");

        } else if (message.startsWith("Welcome ")) {
            systemMessage.setText(message);

        } else if (message.contains("CHAT:")) {
            int chatIndex = message.indexOf("CHAT:");
            if (chatIndex != -1 && chatIndex + 5 < message.length()) {
                String actual = message.substring(chatIndex + 5).trim(); // this strips out CHAT to get the actual content of the message
                System.out.println("[WaitingRoom DEBUG] chat received: " + actual);
                if (chatController != null) {
                    chatController.handleIncomingChat(actual);  // this passes the chat to the chat controller
                } else {
                    System.out.println("[WaitingRoom DEBUG] chatController is null — chat window not open.");
                }
            }

        } else {
            // Catch-all for unhandled messages.
            System.out.println("[WaitingRoom DEBUG] unhandled message: " + message);
        }
    }


    /**
     * this loads the appropriate game scene once both players are ready
     * this selects the fxml file based on the game type then applies the theme and passes role information
     */
    private void loadGameScene() {
        // this makes sure it doesnt advance if no one has arrived
        if (amIPlayerOne == null) {
            System.out.println("[WaitingRoom DEBUG] role not yet assigned, deferring loadGameScene...");
            Platform.runLater(this::loadGameScene);  // this schedules an attempt later on the UI thread.
            return;  // this exits until it is finished
        }

        // this doesn't proceed until the scene is ready
        if (readyButton.getScene() == null || readyButton.getScene().getWindow() == null) {
//            System.out.println("[WaitingRoom DEBUG] scene/window not ready, retrying loadGameScene...");
            Platform.runLater(this::loadGameScene);  // this attempts to load the scene again
            return;  // this exit until the scene is fully ready
        }

        try {
            String fxml;
            // this determines the FXML file based on game type
            if (gameType == GameType.TICTACTOE) {
                fxml = "/org/seng/gui/onlineTicTacToe.fxml";
            } else if (gameType == GameType.CONNECT4) {
                fxml = "/org/seng/gui/onlineConnect4.fxml";
            } else if (gameType == GameType.CHECKERS) {
                fxml = "/org/seng/gui/checkers-game.fxml";
            } else {
                throw new IOException("unsupported game type.");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));  // this prepares the loader fort the FXML file
            Scene scene = new Scene(loader.load(), 700, 450);  // this creates a new scene with the specified dimensions
            scene.getStylesheets().add(getClass().getResource("/org/seng/gui/basic-styles.css").toExternalForm());
            Stage stage = (Stage) readyButton.getScene().getWindow();  // this retrieves the safety window
            stage.setScene(scene);  // this sets the new game scene
            stage.show();

            // this sends games_scene_ready
            try {
                client.sendMessage("GAME_SCENE_READY");  // this notifies the server that the game scene is now active
                System.out.println("[WaitingRoom DEBUG] sent GAME_SCENE_READY");
            } catch (IOException e) {
                System.err.println("[WaitingRoom DEBUG] failed to send GAME_SCENE_READY");
                e.printStackTrace();
            }

            boolean role = amIPlayerOne;
            System.out.println("[WaitingRoom DEBUG] loading game scene with amIPlayerOne = " + role);

            // this initalizes the controller for tictactoe
            if (gameType == GameType.TICTACTOE) {
                // this initializes it with the game logic and player data
                OnlineTicTacToeController controller = loader.getController();
                controller.init(
                        client,  // this passes the network client
                        new org.seng.gamelogic.tictactoe.OnlineTicTacToeGame(
                                new org.seng.gamelogic.tictactoe.TicTacToeBoard(), // this sets uo a new board instance
                                new org.seng.gamelogic.tictactoe.TicTacToePlayer[]{
                                        new org.seng.gamelogic.tictactoe.TicTacToePlayer(username, "", ""),  // this creates the current player
                                        new org.seng.gamelogic.tictactoe.TicTacToePlayer(player2Name.getText(), "", "")  // this creates the opponent player
                                },
                                1  // Starting move or player index.
                        ),
                        role  // Pass the role (true if player one).
                );
                System.out.println("[WaitingRoom DEBUG] game scene loaded. role passed: " + role);
            }
            // you can add CONNECT4 and CHECKERS init here later
        } catch (IOException e) {
            systemMessage.setText("failed to load game scene.");  // Inform the user of an error.
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
            iAmReady = true;  // this marks the player as ready
            client.sendMessage("READY");  // this notifies the server the player is ready
            player1ReadyStatus.setText("ready: yes");  // this updates the ui
            systemMessage.setText("waiting for opponent to be ready...");  // this informs on the player they are waiting for the opponent
            System.out.println("[WaitingRoom DEBUG] sent READY");

            checkIfBothReady(); // this checks to see if the game can start
        } catch (IOException e) {
            systemMessage.setText("failed to send ready message.");  // this updates the UI if message sending fails
            e.printStackTrace();
        }
    }

    private void checkIfBothReady() {
        if (iAmReady && opponentIsReady) {
            systemMessage.setText("both players ready. launching game...");  // this notifies that both sides are set
            if (timerTimeline != null) timerTimeline.stop();  // this stops the waiting timer.

            // this is to tell server this client is leaving the waiting room
            try {
                client.sendMessage("WAITING_ROOM_CLOSED");  // this sends a message indicating the waiting room is done
                System.out.println("[WaitingRoom DEBUG] sent waiting_room_closed ack.");
            } catch (IOException ex) {
                System.err.println("[WaitingRoom DEBUG] failed to send waiting_room_closed ack.");
                ex.printStackTrace();
            }

            // this sets a buffer before the scene transition using a separate thread to ensure both can get in game
            new Thread(() -> {
                try {
                    Thread.sleep(200);  // this sets a pause to make sure messages are flushed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (listenerThread != null && listenerThread.isAlive()) {
                    System.out.println("[WaitingRoom DEBUG] attempting to interrupt listener thread.");
                    listenerThread.interrupt();  // this interrupts the listener thread
                }

                // this ensures the scene switch is done on the JavaFX thread
                Platform.runLater(() -> loadGameScene());
            }).start();
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
                client.sendMessage("LEFT");  // this notifies the server that the player has left.
                client.close();              // this closes the client connection.
            }
            if (timerTimeline != null) {
                timerTimeline.stop();        // this stops the progress timer.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // this loads the games page fxml to return to the main games list.
            URL fxmlUrl = getClass().getClassLoader().getResource("org/seng/gui/games-page.fxml");
            if (fxmlUrl == null) {
                System.err.println("\u26A0 games-page.fxml not found");
                throw new IOException("games-page.fxml not found.");  // this throws an error if the file is missing
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load(), 700, 450);  // this creates a new scene for the games page
            scene.getStylesheets().add(getClass().getResource("/org/seng/gui/styles/basic-styles.css").toExternalForm());
            Stage stage = (Stage) leaveButton.getScene().getWindow();  // this gets the current stage
            stage.setScene(scene);  // this switches to the games page scene
            stage.show();  // this displays the updated stage
        } catch (IOException e) {
            systemMessage.setText("\u26A0 failed to load games page.");  // this informs the user if loading fails
            e.printStackTrace();
        }
    }

    @FXML
    public void onOpenChatClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/seng/gui/game-chat.fxml"));
            Scene scene = new Scene(loader.load(), 500, 400);  // this creates a scene for the chat window

            chatController = loader.getController(); // this stores reference to the chat controller
            chatController.init(client, username);   // this passes SocketGameClient and username for the chat setup

            Stage chatStage = new Stage();  // this creates a new seperate window for the chat
            chatStage.setTitle("Game Chat");  // this is the title
            chatStage.setScene(scene);  // this sets the chat scene
            chatStage.show();  // this displays the chat window

        } catch (IOException e) {
            System.err.println("Failed to open chat window.");
            e.printStackTrace();
        }
    }

    /**
     * this allows the leave button to go back to the dashboard
     * this loads a new scene from game-dashboard.fxml.
     */
    private void goBack() {
        try {
            // this loads the dashboard FXML to return the user to the main dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 700, 450);  // this creates a new scene for the dashboard
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = new Stage();  // this creates a new stage for the dashboard window
            stage.setScene(scene);  // this sets the new scene
            stage.setTitle("OMG Platform");  // this sets the window title
            stage.show();  // this displays the dashboard window
            Stage currentStage = (Stage) leaveButton.getScene().getWindow();  // this gets the current waiting room window
            currentStage.close();  // this closes the waiting room window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
