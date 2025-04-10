package org.seng.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import org.seng.gamelogic.tictactoe.TicTacToeBoard;
import org.seng.gamelogic.tictactoe.TicTacToeGame;
import org.seng.gamelogic.tictactoe.TicTacToePlayer;
import org.seng.networking.SocketGameClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OnlineTicTacToeController {

    @FXML private GridPane board;
    @FXML private Label turnLabel;
    @FXML private Button inGameChatButton;
    @FXML private MenuItem helpOption;

    private Button[][] buttonBoard;
    private static final int BOARD_SIZE = 3;
    private boolean myTurn = false; // set based on server (e.g., via "IS_PLAYER_ONE:..." message)
    private SocketGameClient client;
    private TicTacToeGame game;
    private final String CHAT_LOG_PATH = "chatlog_online.txt";
    private Stage chatStage = null;

    // This method is called right after the scene is loaded.
    public void init(SocketGameClient client, TicTacToeGame game, boolean myTurn) {
        this.client = client;
        this.game = game;
        this.myTurn = myTurn;
        createBoard();
        startNetworkListener();
        updateTurnLabel();
    }

    private void createBoard() {
        buttonBoard = new Button[BOARD_SIZE][BOARD_SIZE];
        board.getChildren().clear();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button btn = new Button();
                btn.setPrefSize(80, 80);
                final int r = row, c = col;
                btn.setOnAction(e -> handleMove(r, c, btn));
                buttonBoard[row][col] = btn;
                board.add(btn, col, row);
            }
        }
    }

    private void handleMove(int row, int col, Button btn) {
        if (!myTurn || !btn.getText().isEmpty()) {
            return; // Not my turn or cell already occupied.
        }
        // Update the local board
        String symbol = game.getCurrentMark().toString(); // "X" or "O"
        btn.setText(symbol);
        btn.setDisable(true);
        game.makeMove(row, col);
        updateTurnLabel();
        // Send the move to the server
        try {
            client.sendMessage("MOVE:TICTACTOE:" + row + "," + col);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        myTurn = false;
    }

    // Call this thread to continuously listen for server messages.
    private void startNetworkListener() {
        Thread listenerThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = client.receiveMessage()) != null) {
                    final String message = msg;
                    Platform.runLater(() -> processServerMessage(message));
                }
            } catch (IOException e) {
                Platform.runLater(() -> showError("Disconnected from server."));
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void processServerMessage(String message) {
        // Protocol example: "MOVE:TICTACTOE:row,col" for opponent moves.
        if (message.startsWith("MOVE:TICTACTOE:")) {
            String data = message.substring("MOVE:TICTACTOE:".length());
            String[] parts = data.split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            // Update the board with opponent move if not already set.
            Button btn = buttonBoard[row][col];
            if (btn.getText().isEmpty()) {
                String oppSymbol = (game.getCurrentMark().toString().equals("X")) ? "O" : "X";
                btn.setText(oppSymbol);
                btn.setDisable(true);
                game.makeMove(row, col);
            }
            myTurn = true;
            updateTurnLabel();
        } else if (message.equals("GAME_OVER:WIN")) {
            showEndScreen("WIN");
        } else if (message.equals("GAME_OVER:DRAW")) {
            showEndScreen("DRAW");
        }
        // Other messages can be handled here.
    }

    private void updateTurnLabel() {
        turnLabel.setText(myTurn ? "Your Turn" : "Opponent's Turn");
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void openChat() {
        if (chatStage != null && chatStage.isShowing()) {
            chatStage.toFront();
            return;
        }
        chatStage = new Stage();
        chatStage.setTitle("In-Game Chat");

        VBox chatBox = new VBox(10);
        chatBox.setPadding(new Insets(10));

        TextArea chatDisplay = new TextArea();
        chatDisplay.setEditable(false);
        chatDisplay.setWrapText(true);
        chatDisplay.setPrefHeight(200);
        chatDisplay.setText(loadChatHistory());

        TextField messageField = new TextField();
        messageField.setPromptText("Type your message...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String msg = messageField.getText().trim();
            if (!msg.isEmpty()) {
                String formatted = "You: " + msg + "\n";
                chatDisplay.appendText(formatted);
                saveMessage(formatted);
                messageField.clear();
            }
        });
        messageField.setOnAction(e -> sendButton.fire());
        chatBox.getChildren().addAll(chatDisplay, messageField, sendButton);

        Scene scene = new Scene(chatBox, 350, 300);
        chatStage.setScene(scene);
        chatStage.show();
    }

    private void saveMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHAT_LOG_PATH, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String loadChatHistory() {
        try {
            return Files.readString(Paths.get(CHAT_LOG_PATH));
        } catch (IOException ex) {
            return "";
        }
    }

    @FXML
    private void howToPlayDescription(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How To Play Tic Tac Toe");
        alert.setHeaderText(null);
        alert.setContentText("Players take turns selecting cells to place their mark.\n"
                + "The first player to align three marks (row, column, or diagonal) wins.\n"
                + "If the board is full without a winning alignment, the game is a draw.");
        alert.showAndWait();
    }

    @FXML
    private void handleQuit() {
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        Label message = new Label("Are you sure you want to quit? You will lose the game.");
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        yesButton.setOnAction(e -> {
            dialogStage.close();
            openToGameDashboard();
        });
        noButton.setOnAction(e -> dialogStage.close());
        HBox buttonBox = new HBox(10, yesButton, noButton);
        buttonBox.setAlignment(Pos.CENTER);
        VBox layout = new VBox(15, message, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout, 300, 150);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    private void openToGameDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene dashboardScene = new Scene(loader.load(), 900, 600);
            Stage dashboardStage = new Stage();
            dashboardStage.setScene(dashboardScene);
            dashboardStage.setTitle("Game Dashboard");
            dashboardStage.show();
            // Close current window
            ((Stage) board.getScene().getWindow()).close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Methods to show an end–game screen (winning/losing/draw) could be added here:
    private void showEndScreen(String result) {
        try {
            String fxmlFile;
            if ("WIN".equals(result))
                fxmlFile = "winningPage.fxml";
            else if ("DRAW".equals(result))
                fxmlFile = "tiePage.fxml";
            else
                fxmlFile = "losingPage.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load(), 700, 450);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Game Over");
            stage.show();
            ((Stage) board.getScene().getWindow()).close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
