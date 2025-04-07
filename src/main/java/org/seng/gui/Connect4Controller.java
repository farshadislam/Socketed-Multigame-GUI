package org.seng.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class Connect4Controller {
    @FXML
    private FlowPane board;

    private static final int ROWS = 6;
    private static final int COLS = 7;

    private Button[][] boardButtons = new Button[ROWS][COLS];

    @FXML
    public void initialize() {
        int buttonIndex = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Button btn = (Button) board.getChildren().get(buttonIndex++);
                int finalCol = col;
                boardButtons[row][col] = btn;
                btn.setOnAction(e -> handleColumnClick(finalCol));
            }
        }
        clearChatHistory();
    }
    @FXML
    private void handleQuit() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Quit");
        confirmAlert.setHeaderText("Are you sure?");
        confirmAlert.setContentText("Do you want to quit and go back to the game dashboard?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            openToGameDashboard();
        }
    }

    private void openToGameDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene dashboardScene = new Scene(loader.load(), 900, 600);
            dashboardScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Game Dashboard");
            dashboardStage.setScene(dashboardScene);

            // Close current window
            Stage currentStage = (Stage) board.getScene().getWindow();
            currentStage.close();

            dashboardStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    void howToPlayDescription() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Help");
        alert.setHeaderText("How to Play");

        alert.setContentText(
                "1. Players take turns dropping pieces.\n\n"
                        + "2. Connect four in a row, column, or diagonal.\n\n"
                        + "3. First player to do so wins!\n\n"
                        + "4. If the board is full, it's a draw.\n");

        alert.getButtonTypes().add(ButtonType.OK);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("gameChat.css").toExternalForm());

        alert.showAndWait();
    }

    private void handleColumnClick(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            Button cell = boardButtons[row][col];
            if (cell.getStyle().isEmpty()) {
                cell.setStyle("-fx-background-color: #00F0FF;");
                break;
            }
        }
    }
    private void clearChatHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHAT_LOG_PATH))) {
            writer.write(""); // Clear the contents
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

