package org.seng.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import org.seng.gamelogic.connectfour.ConnectFourBoard;
import org.seng.gamelogic.connectfour.ConnectFourGame;
import org.seng.gamelogic.connectfour.ConnectFourPlayer;
import org.seng.networking.SocketGameClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OnlineConnect4Controller {

    @FXML private FlowPane board; // Should hold the board buttons
    @FXML private Label player1Label, player2Label;
    @FXML private Button inGameChatButton;
    @FXML private MenuItem helpOption;

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private Button[][] boardButtons;
    private boolean myTurn = false;
    private SocketGameClient client;
    private ConnectFourGame game;
    private final String CHAT_LOG_PATH = "chatlog_connect4_online.txt";

    // Called after FXML is loaded
    public void init(SocketGameClient client, ConnectFourGame game, boolean myTurn) {
        this.client = client;
        this.game = game;
        this.myTurn = myTurn;
        createBoard();
        startNetworkListener();
        updatePlayerTurnIndicator();
    }

    private void createBoard() {
        boardButtons = new Button[ROWS][COLS];
        board.getChildren().clear();
        // Assuming the FlowPane is used to layout buttons in order.
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Button btn = new Button();
                btn.setPrefSize(50, 50);
                final int r = row, c = col;
                btn.setOnAction(e -> handleColumnClick(c));
                boardButtons[row][col] = btn;
                board.getChildren().add(btn);
            }
        }
    }

    // When a column is clicked, drop the piece in that column
    private void handleColumnClick(int col) {
        if (!myTurn) return;
        // Find the lowest empty row
        for (int row = ROWS - 1; row >= 0; row--) {
            Button cell = boardButtons[row][col];
            if (cell.getText().isEmpty()) {
                // Update locally
                String symbol = (game.currentPlayer.getSymbol() == 'b') ? "Blue" : "Yellow";
                cell.setText(symbol);
                cell.setDisable(true);
                game.makeMove(col);
                updatePlayerTurnIndicator();
                // Send move to server in format: MOVE:CONNECT4:col
                try {
                    client.sendMessage("MOVE:CONNECT4:" + col);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                myTurn = false;
                break;
            }
        }
    }

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
        // Protocol: "MOVE:CONNECT4:col" for opponent moves.
        if (message.startsWith("MOVE:CONNECT4:")) {
            String data = message.substring("MOVE:CONNECT4:".length());
            int col = Integer.parseInt(data);
            // Update board with opponent’s move
            for (int row = ROWS - 1; row >= 0; row--) {
                Button cell = boardButtons[row][col];
                if (cell.getText().isEmpty()) {
                    String symbol = (game.currentPlayer.getSymbol() == 'b') ? "Yellow" : "Blue";
                    cell.setText(symbol);
                    cell.setDisable(true);
                    game.makeMove(col);
                    break;
                }
            }
            myTurn = true;
            updatePlayerTurnIndicator();
        } else if (message.equals("GAME_OVER:WIN")) {
            showEndScreen("WIN");
        } else if (message.equals("GAME_OVER:DRAW")) {
            showEndScreen("DRAW");
        }
    }

    private void updatePlayerTurnIndicator() {
        // For simplicity, assume labels show turn. You can add animations as needed.
        player1Label.setText(myTurn ? "Your Turn" : "Opponent's Turn");
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void openChat() {
        Stage chatStage = new Stage();
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
        alert.setTitle("How To Play Connect Four");
        alert.setHeaderText(null);
        alert.setContentText("Drop your chip in a column by clicking the column.\n"
                + "The first player to connect four chips horizontally, vertically, or diagonally wins.\n"
                + "If the board fills with no winner, it's a draw.");
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
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        VBox layout = new VBox(15, message, buttonBox);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
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
            ((Stage) board.getScene().getWindow()).close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

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
