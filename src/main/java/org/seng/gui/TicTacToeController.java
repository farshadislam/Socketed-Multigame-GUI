package org.seng.gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.seng.gamelogic.tictactoe.TicTacToeBoard;
import org.seng.gamelogic.tictactoe.TicTacToeGame;
import org.seng.gamelogic.tictactoe.TicTacToeGame;
import org.seng.gamelogic.tictactoe.TicTacToePlayer;
import org.seng.gamelogic.tictactoe.TicTacToePlayer;
import javafx.stage.StageStyle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class TicTacToeController {

    private TicTacToeGame game;
    private TicTacToePlayer localPlayer;
    private TicTacToePlayer remotePlayer;
    private boolean isAIMode = false;
    private boolean isOnlineMode = false;

    public int[] buttonLocation;

    private Map<Button, int[]> buttonPositionMap = new HashMap<>();

    @FXML private Button button11, button12, button13;
    @FXML private Button button21, button22, button23;
    @FXML private Button button31, button32, button33;

    @FXML private Button inGameChatButton;
    @FXML private MenuItem helpOption;
    @FXML private Label turnLabel;
    @FXML private FlowPane board;


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

    private StringBuilder chatHistory = new StringBuilder();

    @FXML
    private void openChat() {
        Stage chatStage = new Stage();
        chatStage.setTitle("In-Game Chat");

        VBox chatBox = new VBox(10);
        chatBox.setPadding(new javafx.geometry.Insets(10));
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
        chatStage.show();


    }

    @FXML
    void howToPlayDescription(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE);

        alert.setTitle("Help");
        alert.setHeaderText("How to Play");

        alert.setContentText(
                "1. Players take turns selecting a spot for their X or O.\n\n"
                        + "2. Try to get three XXX or OOO all in the same row, column, or across. \n\n"
                        + "3. First player to do so wins!\n\n"
                        + "4. If the board is full, it's a draw.\n");

        alert.getButtonTypes().add(javafx.scene.control.ButtonType.OK);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("gameChat.css").toExternalForm());

        alert.showAndWait();
    }
    private boolean isPlayerOneTurn = true;
    private void handleMove(Button button) {
        if (button.getText().isEmpty()) {
            int[] location = buttonPositionMap.get(button);
            int row = location[0];
            int col = location[1];

            boolean moveMade = game.makeMove(row, col);

            if (moveMade) {
                String symbol = game.getCurrentMark() == TicTacToeBoard.Mark.O ? "X" : "O"; // show previous mark
                button.setText(symbol);

                // Check game status
                String status = game.getStatus();
                if (status.endsWith("Wins")) {
                    turnLabel.setText("Game Over! " + status);
                    disableAllButtons();
                } else if (status.equals("Draw")) {
                    turnLabel.setText("It's a Draw!");
                    disableAllButtons();
                } else {
                    turnLabel.setText(game.getCurrentMark() == TicTacToeBoard.Mark.X ? "Player 1's Turn" : "Player 2's Turn");
                }
            }
            isPlayerOneTurn = !isPlayerOneTurn;
        }
    }
    private void disableAllButtons() {
        for (Button b : buttonPositionMap.keySet()) {
            b.setDisable(true);
        }
    }
}


