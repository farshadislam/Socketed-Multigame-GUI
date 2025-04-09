package org.seng.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.seng.gamelogic.tictactoe.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TicTacToeController {

    private TicTacToeGame game;
    private boolean isAIMode = false;
    private boolean isOnlineMode = false;
    private Stage chatStage = null;

    private boolean isPlayerXTurn = true;

    private int valForAlternation = 0;

    private Map<Button, int[]> buttonPositionMap = new HashMap<>();

    @FXML
    private Button button11, button12, button13;
    @FXML
    private Button button21, button22, button23;
    @FXML
    private Button button31, button32, button33;

    @FXML
    private Button inGameChatButton;
    @FXML
    private MenuItem helpOption;
    @FXML
    private Label turnLabel;
    @FXML
    private FlowPane board;

    @FXML
    public void initialize() {
        clearChatHistory();

//        button11.setOnAction(e -> handleMove(button11));
//        button12.setOnAction(e -> handleMove(button12));
//        button13.setOnAction(e -> handleMove(button13));
//        button21.setOnAction(e -> handleMove(button21));
//        button22.setOnAction(e -> handleMove(button22));
//        button23.setOnAction(e -> handleMove(button23));
//        button31.setOnAction(e -> handleMove(button31));
//        button32.setOnAction(e -> handleMove(button32));
//        button33.setOnAction(e -> handleMove(button33));
//
//        buttonPositionMap.put(button11, new int[]{0, 0});
//        buttonPositionMap.put(button12, new int[]{0, 1});
//        buttonPositionMap.put(button13, new int[]{0, 2});
//        buttonPositionMap.put(button21, new int[]{1, 0});
//        buttonPositionMap.put(button22, new int[]{1, 1});
//        buttonPositionMap.put(button23, new int[]{1, 2});
//        buttonPositionMap.put(button31, new int[]{2, 0});
//        buttonPositionMap.put(button32, new int[]{2, 1});
//        buttonPositionMap.put(button33, new int[]{2, 2});

//        String details = nextTurn();
//        String[] currentState = details.split(":");
//        currentState[0]
//
//        for (int i = 0; i < 9; i++) {
//
//        }
        button11.setOnAction(e -> handleMove(0, 0, button11));  // First row, first column
        button12.setOnAction(e -> handleMove(0, 1, button12));  // First row, second column
        button13.setOnAction(e -> handleMove(0, 2, button13));  // First row, third column

        button21.setOnAction(e -> handleMove(1, 0, button21));  // Second row, first column
        button22.setOnAction(e -> handleMove(1, 1, button22));  // Second row, second column
        button23.setOnAction(e -> handleMove(1, 2, button23));  // Second row, third column

        button31.setOnAction(e -> handleMove(2, 0, button31));  // Third row, first column
        button32.setOnAction(e -> handleMove(2, 1, button32));  // Third row, second column
        button33.setOnAction(e -> handleMove(2, 2, button33));  // Third row, third column

        Button[] row1 = {button11, button12, button13};
        Button[] row2 = {button21, button22, button23};
        Button[] row3 = {button31, button32, button33};

        Button[][] board = {row1, row2, row3};
    }

    private String nextTurn() {
        return "";
    }

    private final String CHAT_LOG_PATH = "chatlog.txt";

    private void saveMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHAT_LOG_PATH, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadChatHistory() {
        try {
            return Files.readString(Paths.get(CHAT_LOG_PATH));
        } catch (IOException e) {
            return "";
        }
    }

    private void clearChatHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHAT_LOG_PATH))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMove(int row, int col, Button button) {
        valForAlternation++;
        // Check if the button has already been clicked (i.e., it already has a symbol)
        if (!button.getText().isEmpty()) {
            return;  // If the button is already clicked, do nothing
        }

        // Place the symbol on the button
        if (valForAlternation % 2 == 0) {
            button.setText("O");
        } else {
            button.setText("x");
        }
//        String symbol = isPlayerXTurn ? "X" : "O";  // Toggle between X and O
//        button.setText(symbol);

        // Disable the button to prevent re-clicking
        button.setDisable(true);

        // Make the move on the game board
        boolean moveMade = game.makeMove(row, col);

        if (moveMade) {
            // Update the game status
            String status = game.getStatus();
            if (status.endsWith("Wins")) {
                turnLabel.setText("Game Over! " + status);
                System.out.print(turnLabel);
                disableAllButtons();  // Disable all buttons when the game ends
            } else if (status.equals("Draw")) {
                turnLabel.setText("It's a Draw!");
                System.out.print(turnLabel);
                disableAllButtons();  // Disable all buttons in case of a draw
            } else {
                turnLabel.setText(game.getCurrentMark() == TicTacToeBoard.Mark.X ? "Player 1's Turn" : "Player 2's Turn");
                System.out.print(turnLabel);
            }

            // Switch to the other player's turn
            togglePlayerTurn();
        }

//        initNum++;
//        if (initNum == 1){
//            clearChatHistory();
//
//            // Setup player data
//            localPlayer = new TicTacToePlayer("usernameOne", "emailOne",  "passwordOne");
//            remotePlayer = new TicTacToePlayer("usernameTwo", "emailTwo",  "passwordTwo");
//
//            TicTacToeBoard board = new TicTacToeBoard();
//            game = new TicTacToeGame(board, new TicTacToePlayer[]{localPlayer, remotePlayer}, 1);
//            game.initializePlayerSymbols();
//        }

//        if (!button.getText().isEmpty()) return;
//
//        boolean moveMade = game.makeMove(row, col);
//
//        if (moveMade) {
//            String symbol = game.getCurrentMark() == TicTacToeBoard.Mark.X ? "O" : "X";
//            button.setText(symbol);
//
//            String status = game.getStatus();
//            if (status.endsWith("Wins")) {
//                turnLabel.setText("Game Over! " + status);
//                disableAllButtons();
//            } else if (status.equals("Draw")) {
//                turnLabel.setText("It's a Draw!");
//                disableAllButtons();
//            } else {
//                turnLabel.setText(game.getCurrentMark() == TicTacToeBoard.Mark.X ? "Player 1's Turn" : "Player 2's Turn");
//            }
//
//            if (game.AIBot != null && game.getCurrentMark() == game.getCurrentMark()) {
//                game.AIBot.makeMove(game.getBoard(), game);
//                updateBoardButtons();
//                String newStatus = game.getStatus();
//                if (newStatus.endsWith("Wins") || newStatus.equals("Draw")) {
//                    turnLabel.setText("Game Over! " + newStatus);
//                    disableAllButtons();
//                }
//            }
//        }
    }

    // Function to toggle player turns (just a sample, adjust based on your existing logic)
    private void togglePlayerTurn() {
        isPlayerXTurn = !isPlayerXTurn;
    }

    // Function to check if it's Player X's turn (adjust based on your existing logic)
    private boolean isPlayerXTurn() {
        return isPlayerXTurn;
    }


    private void updateBoardButtons() {
        for (Map.Entry<Button, int[]> entry : buttonPositionMap.entrySet()) {
            Button button = entry.getKey();
            int[] pos = entry.getValue();
            TicTacToeBoard.Mark mark = game.getBoard().getMark(pos[0], pos[1]);

            if (mark == TicTacToeBoard.Mark.X) {
                button.setText("X");
            } else if (mark == TicTacToeBoard.Mark.O) {
                button.setText("O");
            } else {
                button.setText("");
            }
        }
    }

    private void disableAllButtons() {
        for (Button b : buttonPositionMap.keySet()) {
            b.setDisable(true);
        }
    }

    @FXML
    private void openChat() {
        // Close existing window if it's open
        if (chatStage != null && chatStage.isShowing()) {
            chatStage.close();
        }

        // Create a new window
        chatStage = new Stage();
        chatStage.setTitle("In-Game Chat");

        VBox chatBox = new VBox(10);
        chatBox.setPadding(new Insets(10));
        chatBox.getStyleClass().add("chat-window");

        TextArea chatDisplay = new TextArea();
        chatDisplay.setEditable(false);
        chatDisplay.setWrapText(true);
        chatDisplay.setPrefHeight(200);
        chatDisplay.getStyleClass().add("chat-display");
        chatDisplay.setText(loadChatHistory());

        TextField messageField = new TextField();
        messageField.setPromptText("Type your message...");
        messageField.getStyleClass().add("chat-input");

        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("chat-send-button");

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
        scene.getStylesheets().add(getClass().getResource("gameChat.css").toExternalForm());
        chatStage.setScene(scene);

        // Reset the reference when closed
        chatStage.setOnHidden(e -> chatStage = null);

        chatStage.show();
    }


    @FXML
    void howToPlayDescription(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Help");
        alert.setHeaderText("How to Play");

        alert.setContentText(
                "1. Players take turns selecting a spot for their X or O.\n\n" +
                        "2. Try to get three XXX or OOO all in the same row, column, or across. \n\n" +
                        "3. First player to do so wins!\n\n" +
                        "4. If the board is full, it's a draw.\n");

        alert.getButtonTypes().add(ButtonType.OK);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("gameChat.css").toExternalForm());

        alert.showAndWait();
    }

    @FXML
    private void handleQuit() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Quit");
        confirmAlert.setHeaderText("Are you sure you want to quit?");
        confirmAlert.setContentText("Any unsaved progress will be lost.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
                    Scene dashboardScene = new Scene(loader.load());
                    dashboardScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

                    // Get current stage from any node in the current scene (like the board)
                    Stage currentStage = (Stage) board.getScene().getWindow();
                    currentStage.setScene(dashboardScene);
                    currentStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}




//    @FXML
//    private void handleButtonClick(ActionEvent event) {
//        Button clickedButton = (Button) event.getSource();
//        if (clickedButton.getText().isEmpty()) {
//            if (isPlayerXTurn) {
//                clickedButton.setText("X");
//            } else {
//                clickedButton.setText("O");
//            }
//            isPlayerXTurn = !isPlayerXTurn; // Toggle turn
//        }
//    }


