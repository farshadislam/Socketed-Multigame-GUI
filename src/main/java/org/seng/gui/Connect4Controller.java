package org.seng.gui;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class Connect4Controller {
    @FXML private FlowPane board;
    @FXML private Label player1Label;
    @FXML private Label player2Label;

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private ScaleTransition player1Pulse;
    private ScaleTransition player2Pulse;
    private final String CHAT_LOG_PATH = "chatlog.txt";
    private boolean isPlayerOneTurn = true;
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
        updatePlayerTurnIndicator();
    }
    @FXML
    private void handleQuit() {
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.setTitle("Confirm Quit");

        Label message = new Label("                      Are you sure?\nQuitting the game will result in a loss.");
        message.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            dialogStage.close();
            openToGameDashboard();
        });
        noButton.setOnAction(e -> dialogStage.close());

        HBox buttons = new HBox(10, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, message, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("quit-background"); // ⭐ Add style class

        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(getClass().getResource("connectfourstyles.css").toExternalForm()); // ⭐ Load your CSS

        dialogStage.setScene(scene);

        Stage currentStage = (Stage) board.getScene().getWindow(); // 'board' is your main pane
        dialogStage.initOwner(currentStage);

        dialogStage.setX(currentStage.getX() + currentStage.getWidth() / 2 - 150); // 150 = half of popup width
        dialogStage.setY(currentStage.getY() + currentStage.getHeight() / 2 - 100);  // 75 = half of popup height

        dialogStage.show();
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
                if (isPlayerOneTurn) {
                    cell.setStyle("-fx-background-color: #00F0FF;"); // Cyan
                } else {
                    cell.setStyle("-fx-background-color: #da77f2;"); // Yellow
                }
                isPlayerOneTurn = !isPlayerOneTurn; // Switch turns
                updatePlayerTurnIndicator();
                break;
            }
        }
    }

    private void updatePlayerTurnIndicator() {
        // Reset styles
        player1Label.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        player2Label.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Stop any ongoing pulse animations
        stopPulse(player1Pulse);
        stopPulse(player2Pulse);
        player1Label.setScaleX(1);
        player1Label.setScaleY(1);
        player2Label.setScaleX(1);
        player2Label.setScaleY(1);

        // Set style + pulse for current player's turn
        if (isPlayerOneTurn) {
            player1Label.setStyle("-fx-font-size: 24px; -fx-text-fill: #00F0FF;");
            player1Pulse = applyPulseAnimation(player1Label);
        } else {
            player2Label.setStyle("-fx-font-size: 24px; -fx-text-fill: #da77f2;");
            player2Pulse = applyPulseAnimation(player2Label);
        }
    }

    private ScaleTransition applyPulseAnimation(Label label) {
        ScaleTransition st = new ScaleTransition(Duration.millis(800), label);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setCycleCount(ScaleTransition.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
        return st; // return so we can track & stop it later
    }

    private void stopPulse(ScaleTransition st) {
        if (st != null) {
            st.stop();
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

