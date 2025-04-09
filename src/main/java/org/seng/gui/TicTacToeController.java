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
import javafx.stage.StageStyle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TicTacToeController {

    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;
    @FXML private Button button5;
    @FXML private Button button6;
    @FXML private Button button7;
    @FXML private Button button8;
    @FXML private Button button9;
    @FXML private Button inGameChatButton;
    @FXML private MenuItem helpOption;
    @FXML private Label turnLabel;
    @FXML private FlowPane board;

    @FXML public void initialize() {
        clearChatHistory();
        button1.setOnAction(e -> handleMove(button1));
        button2.setOnAction(e -> handleMove(button2));
        button3.setOnAction(e -> handleMove(button3));
        button4.setOnAction(e -> handleMove(button4));
        button5.setOnAction(e -> handleMove(button5));
        button6.setOnAction(e -> handleMove(button6));
        button7.setOnAction(e -> handleMove(button7));
        button8.setOnAction(e -> handleMove(button8));
        button9.setOnAction(e -> handleMove(button9));
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
            if (isPlayerOneTurn) {
                button.setText("X");
                turnLabel.setText("Player 2's Turn");
            } else {
                button.setText("O");
                turnLabel.setText("Player 1's Turn");
            }
            isPlayerOneTurn = !isPlayerOneTurn;
        }
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
//    @FXML
//    private void handleButtonClick(ActionEvent event) {
//        Button clickedButton = (Button) event.getSource();
//        if (clickedButton.getText().isEmpty()) {
//            clickedButton.setText("X");
//        }
//    }
}


