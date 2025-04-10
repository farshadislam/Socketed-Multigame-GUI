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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WaitingRoomController {

    // below are our ui components coming from the fxml file
    @FXML private Label gameTypeLabel;              // this shows which game mode is chosen
    @FXML private ProgressBar matchProgressBar;       // provides a visual countdown or progress of waiting time
    @FXML private Label player1Name, player2Name;     // these are labels for displaying player names in the waiting room
    @FXML private Label player1Status, player2Status; // these are labels to show if the players are "loaded" or not
    @FXML private Circle player1StatusIndicator, player2StatusIndicator; // these are little circles that change color based on status
    @FXML private Label player1ReadyStatus, player2ReadyStatus; // these are labels to indicate if players have clicked "ready"
    @FXML private Label matchTimer;                   // this is a label that shows how long we've been waiting like a timer
    @FXML private Button readyButton;                 // this button is the one that the player clicks to indicate they're ready
    @FXML private Label systemMessage;                // this is  label to show system messages like errors
    @FXML private Button leaveButton;                 // this button that lets the player leave the waiting room
    @FXML private ImageView player1Avatar, player2Avatar; // these are image views for showing player avatars

    // these are that variables that keep track of what's happening behind the scenes
    private SocketGameClient client;    // this is our connection to the game server so we can send/receive messages
    private String username;            // this is our player's username
    private GameType gameType;          // this is what game we're playing,
    private Timeline timerTimeline;     // this is a timeline to update our timer and progress bar
    private int secondsElapsed = 0;     // this counter is used to keep track of how many seconds have passed while waiting

    // these are extra variables for storing additional info about players
    private String localName = null;
    private String opponentName = null;
    private Boolean amIPlayerOne = null;

    // this method below initializes the waiting room, while collecting info thru its parameters  t
    public void init(String username, GameType gameType, SocketGameClient client) {
        // tese values are stroed from the prev scene for later usage  s
        this.username = username;  // this sets our username
        this.gameType = gameType;  // this sets what game we're playing
        this.client = client;      // this stores the server connection

        System.out.println("waitingroom init: " + username + ", type: " + gameType);
        // here we check if there's an authenticated player already stored in our dashboard
        org.seng.authentication.Player authPlayer = GameDashboardController.player;
        if (authPlayer != null && authPlayer.getUsername().equals(username)) {
            // if the player exists, we print out that we're using that data because it's already been authenticated
            System.out.println("using authenticated player data for " + username);
        }

        // this tries to load placeeholder avatar images for the users on the waiting screen
        try {
            // these are the placeholder images and paths
            Image player1PlaceholderImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/avatar-placeholder1.png"));
            Image player2PlaceholderImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/avatar-placeholder2.png"));

            // this sets the images into the image views
            player1Avatar.setImage(player1PlaceholderImage);
            player2Avatar.setImage(player2PlaceholderImage);

            // these create circular clips for the avattar
            Circle clip1 = new Circle(45, 45, 45);
            player1Avatar.setClip(clip1);            // this applies the clip to the image view for p1
            Circle clip2 = new Circle(45, 45, 45);
            player2Avatar.setClip(clip2);  // this applies the clip to the image view for p2

            // this then applies a drop shadow style on the avatars
            player1Avatar.setStyle("-fx-effect: dropshadow(three-pass-box, #a855f7, 8, 0.8, 0, 0);");
            player2Avatar.setStyle("-fx-effect: dropshadow(three-pass-box, #5e60ce, 8, 0.8, 0, 0);");

        } catch (Exception e) {
            // this is for if loading an image fails then print an error so we can fix it later
            System.err.println("failed to load placeholder avatar: " + e.getMessage());
            e.printStackTrace();
        }

        // this sets up our ui labels with the initial values
        gameTypeLabel.setText("game mode: " + gameType);  // this show the chosen game type
        player1Name.setText(username);                      // this shows our username in the first player's spot
        player1Status.setText("\u2705 loaded");             // this uses a check mark to indicate the player is loaded
        player1StatusIndicator.setFill(Color.web("#a855f7")); // this is the color for the status indicator

        // this starts the timer and progress bar for waiting length
        startTimerAndProgressBar();

        // this start listening for messages from the server on a new background thread
        listenForUpdates();

        // this sets what happens when the user clicks the leave button
        leaveButton.setOnAction(e -> goBack());
    }

    // this method applies the consistent style to the waiting room by using the specific css file
    private void dashboardTheme() {
        // this uses Platform.runLater to make sure the code runs on the ui thread
        Platform.runLater(() -> {
            Scene scene = readyButton.getScene(); // this gets the current scene from one of our buttons
            if (scene != null) {
                // this tries to load the css file for the waiting room theme
                URL css = getClass().getResource("/org/seng/gui/waiting-room.css");
                if (css != null) {
                    scene.getStylesheets().clear(); // this clears any existing styles
                    scene.getStylesheets().add(css.toExternalForm()); // this adds our style css
                } else {
                    // this is a warning message if we can't find the style
                    systemMessage.setText("\u26A0 style not found: waiting-room.css");
                }
            }
        });
    }

    // this method starts a timer that updates our match countdown and progress bar
    private void startTimerAndProgressBar() {
        // this creates a new timeline with a keyframe that happens every 1 second
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++;  // this increments the seconds counter
            // this updates the timer label so the user sees how long they've been waiting
            matchTimer.setText(String.format("00:%02d", secondsElapsed));
            // this calculates a progress value capped at 1.0 so we assume 30 seconds is full progress
            double progress = Math.min(1.0, secondsElapsed / 30.0);
            matchProgressBar.setProgress(progress);  // this updates the progress bar with the new progress value
        }));
        timerTimeline.setCycleCount(Timeline.INDEFINITE); // this makes the timer repeat forever until manually stopped
        timerTimeline.play(); // this starts the timer

        // this applies the theme to the UI
        dashboardTheme();
    }

    // this method starts a new thread to listen for messages from the server without blocking the ui
    private void listenForUpdates() {
        Thread listenerThread = new Thread(() -> {
            try {
                String msg;  // this variable is used to hold incoming messages
                // this loops continuously to read messages from the socket client
                while ((msg = client.receiveMessage()) != null) {
                    String finalMsg = msg; // this stores the message in a final variable for use in the lambda below
                    // this updates the ui on the application thread based on the message received
                    Platform.runLater(() -> handleServerMessage(finalMsg));
                }
            } catch (IOException e) {
                // this is for if we get an exception
                Platform.runLater(() -> systemMessage.setText("disconnected from server."));
            }
        });
        // this sets the thread so it doesn't prevent the app from closing
        listenerThread.setDaemon(true);
        listenerThread.start(); // start listening!
    }

    // this method handles any messages coming from the server and updates the ui accordingly
    private void handleServerMessage(String message) {
        // this is if the message is that the opponent is ready
        if (message.equals("OPPONENT_READY")) {
            player2ReadyStatus.setText("ready: yes");    // this updates the ready status label for the opponent
            player2Status.setText("\u2705 loaded");        // this marks the opponent as loaded
            player2StatusIndicator.setFill(Color.web("#a855f7")); // this sets the status indicator color for the opponent
        }
        // this is if the message tells us to start the game
        else if (message.equals("START_GAME")) {
            systemMessage.setText("both players ready. launching game..."); // this lets the user know the game will start
            if (timerTimeline != null) timerTimeline.stop(); // this stops the timer since we don't need to wait anymore
            loadGameScene();  // this loads the game scene (see below for new integration)
        }
        // this is if the message is a welcome message
        else if (message.startsWith("Welcome ")) {
            systemMessage.setText(message); // this displays the welcome msg
        }
        // this is if the message contains the opponent's name
        else if (message.startsWith("OPPONENT_NAME:")) {
            // this extracts the opponent's name from the message
            String opponentName = message.substring("OPPONENT_NAME:".length());
            player2Name.setText(opponentName); // this updates the ui label with the opponent's name
            systemMessage.setText("matched with: " + opponentName); //this  also show a message that we've been matched
        }
        // this message tells us whether the player is player one or not
        else if (message.startsWith("IS_PLAYER_ONE:")) {
            // this parses the value from the message will be "true" or "false"
            boolean isPlayerOne = message.substring("IS_PLAYER_ONE:".length()).equals("true");

            // this is for if they are not player one the positions are swapped so that the labels display correctly
            if (!isPlayerOne) {
                String myName = player1Name.getText(); // temporarily store the current name
                player1Name.setText(player2Name.getText());
                player2Name.setText(myName);

                String tempStatus = player1Status.getText();
                player1Status.setText(player2Status.getText());
                player2Status.setText(tempStatus);

                Color tempColor = (Color) player1StatusIndicator.getFill();
                player1StatusIndicator.setFill(player2StatusIndicator.getFill());
                player2StatusIndicator.setFill(tempColor);

                String tempReady = player1ReadyStatus.getText();
                player1ReadyStatus.setText(player2ReadyStatus.getText());
                player2ReadyStatus.setText(tempReady);
            }
        }
    }

    // this helper method is to update player labels (if needed)
    private void updatePlayerLabels() {
        if (Boolean.TRUE.equals(amIPlayerOne)) {
            // if we're player one then assign the local name to player1 and opponent name to player2
            player1Name.setText(localName);
            player2Name.setText(opponentName);
        } else {
            // if we're player two then swap the names
            player1Name.setText(opponentName);
            player2Name.setText(localName);
        }
    }

    // ***************************************************************
    // NEW INTEGRATION: Load the online game scene when both players are ready.
    // ***************************************************************
    private void loadGameScene() {
        try {
            // Decide which fxml file to load based on the game type.
            // (For example, if online Tic Tac Toe, load "tictactoe-online.fxml"; if Connect Four, load "connect4-online.fxml")
            String fxml = switch (gameType) {
                case CHECKERS -> "/org/seng/gui/checkers-game.fxml"; // For Checkers (unchanged, if applicable)
                case CONNECT4 -> "/org/seng/gui/connect4-online.fxml";   // Online Connect4 FXML file
                case TICTACTOE -> "/org/seng/gui/tictactoe-online.fxml";   // Online TicTacToe FXML file
            };

            // Load the fxml file and create a new scene from it
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), 700, 450);
            // Add the basic style so that the scene matches our design
            scene.getStylesheets().add(getClass().getResource("/org/seng/gui/styles/basic-styles.css").toExternalForm());

            // Get the current stage from one of the controls and set the new scene
            Stage stage = (Stage) readyButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // Now, initialize the online game controller.
            // We pass along the client (for network communications), a new game instance, and whether this player is player one.
            if (gameType == GameType.TICTACTOE) {
                // For example, obtain the online TicTacToe controller; adjust package/class names as needed.
                OnlineTicTacToeController controller = loader.getController();
                controller.init(client,
                        new org.seng.gamelogic.tictactoe.TicTacToeGame(
                                new org.seng.gamelogic.tictactoe.TicTacToeBoard(),
                                new org.seng.gamelogic.tictactoe.TicTacToePlayer[]{
                                        new org.seng.gamelogic.tictactoe.TicTacToePlayer(username, "", ""),
                                        new org.seng.gamelogic.tictactoe.TicTacToePlayer(player2Name.getText(), "", "")
                                },
                                1),
                        (amIPlayerOne != null ? amIPlayerOne : true)
                );
            } else if (gameType == GameType.CONNECT4) {
                // For online Connect4 similarly
                OnlineConnect4Controller controller = loader.getController();
                controller.init(client,
                        new org.seng.gamelogic.connectfour.ConnectFourGame(
                                new org.seng.gamelogic.connectfour.ConnectFourBoard(),
                                new org.seng.gamelogic.connectfour.ConnectFourPlayer[]{
                                        new org.seng.gamelogic.connectfour.ConnectFourPlayer(username, "", ""),
                                        new org.seng.gamelogic.connectfour.ConnectFourPlayer(player2Name.getText(), "", "")
                                },
                                1),
                        (amIPlayerOne != null ? amIPlayerOne : true)
                );
            }
        } catch (IOException e) {
            systemMessage.setText("failed to load game scene.");
            e.printStackTrace();
        }
    }
    // ***************************************************************

    // this method is called when the user clicks the ready button
    @FXML
    public void onReadyClicked() {
        try {
            // this sends the ready message to the server so it knows the player is ready to play
            client.sendMessage("READY");
            // this updates the ui to reflect that we are ready
            player1ReadyStatus.setText("ready: yes");
            systemMessage.setText("waiting for opponent to be ready...");
        } catch (IOException e) {
            // this prints an error if there is a failure
            systemMessage.setText("failed to send ready message.");
            e.printStackTrace();
        }
    }

    // this method is called when the user clicks the leave button to exit the waiting room
    @FXML
    public void onLeaveClicked() {
        try {
            // this sends a LEFT message so the server knows the user is leaving
            if (client != null) {
                client.sendMessage("LEFT");
                client.close(); // this closes the socket connection
            }
            // this stops the timer since the user is leaving the waiting room
            if (timerTimeline != null) timerTimeline.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // this loads the fxml for the games page so the user can see the list of available games
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
            stage.show(); // this shows the games page
        } catch (IOException e) {
            systemMessage.setText("\u26A0 failed to load games page");
            e.printStackTrace();
        }
    }

    // this method handles going back to the game dashboard from the waiting room
    private void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("omg platform"); // this sets the window title
            stage.show(); // this shows the dashboard

            // close the current waiting room window so it doesn't remain open in the background
            Stage currentStage = (Stage) leaveButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
