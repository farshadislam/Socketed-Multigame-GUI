package org.seng.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.seng.gamelogicTest.tictactoe.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TicTacToeController {

    private TicTacToeGame game;
    private Stage chatStage = null;

    private boolean isPlayerXTurn = true;

    private int valForAlternation = 0;

    private Map<Button, int[]> buttonPositionMap = new HashMap<>();

    private Button[][] buttonBoard;

    private static final int BOARD_SIZE = 3;

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
    private GridPane board;

    private boolean AIBot;

    private Timeline timeline;

    public void setAIBot(boolean AIBot) {
        this.AIBot = AIBot;
    }

    @FXML
    public void initialize() {
        clearChatHistory();

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

        buttonBoard = new Button[][]{row1, row2, row3};
        isPlayerXTurn = true;
        turnLabel.setText("Player 1's Turn");
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
        if (timeline != null) {
            timeline.stop();
        }
        valForAlternation++;
        // Check if the button has already been clicked
        if (!button.getText().isEmpty()) {
            return;  // If the button is already clicked, do nothing
        }

        // Place the symbol on the button
        if (valForAlternation % 2 == 0) {
            button.setText("O");
            button.setStyle("-fx-font-size: 36px; -fx-text-fill: red;");
            button.setDisable(true);
            if (checkWinner(row, col)) {
                checkWin(button);
            }
            else if (boardFull()) {
                checkTie(button);
            }
        } else {
            button.setText("X");
            button.setStyle("-fx-font-size: 36px; -fx-text-fill: deepskyblue;");
            button.setDisable(true);
            if (checkWinner(row, col)) {
                checkWin(button);
            }
            else if (boardFull()) {
                checkTie(button);
            }
        }
        togglePlayerTurn(button);

        // for when AI mode is active
        if (!isPlayerXTurn && AIBot) {
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
                Platform.runLater(this::makeAIMove);
            }).start();
        }
    }

    private void checkWin(Button sourceButton) {
        try {
            FXMLLoader fxmlLoader;
            Scene scene;
            if (AIBot) {
                if (isPlayerXTurn) {
                    fxmlLoader = new FXMLLoader(getClass().getResource("winningPage.fxml"));
                } else {
                    fxmlLoader = new FXMLLoader(getClass().getResource("losingPage.fxml"));
                }
            } else {
                fxmlLoader = new FXMLLoader(getClass().getResource("winningPage.fxml"));
            }

            scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("checkerstyles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OMG Platform");
            stage.show();

            Stage currentStage = (Stage) sourceButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void checkTie(Button tieButton){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tiePage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("checkerstyles.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OMG Platform");
            stage.show();

            Stage currentStage = (Stage) tieButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void togglePlayerTurn(Button button) {
        isPlayerXTurn = !isPlayerXTurn;
        if (isPlayerXTurn) {
            turnLabel.setText("Player 1's Turn");
        } else {
            turnLabel.setText("Player 2's Turn");
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(10),
                event -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("losingPage.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 700, 450);
                        scene.getStylesheets().add(getClass().getResource("checkerstyles.css").toExternalForm());

                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("OMG Platform");
                        stage.show();

                        Stage currentStage = (Stage) button.getScene().getWindow();
                        currentStage.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
        ));
        timeline.setCycleCount(1);
        timeline.play();

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
        layout.getStyleClass().add("quit-background"); //

        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(getClass().getResource("connectfourstyles.css").toExternalForm()); //

        dialogStage.setScene(scene);

        Stage currentStage = (Stage) board.getScene().getWindow(); // 'board' is your main pane
        dialogStage.initOwner(currentStage);

        dialogStage.setX(currentStage.getX() + currentStage.getWidth() / 2 - 150); // 150 = half of popup width
        dialogStage.setY(currentStage.getY() + currentStage.getHeight() / 2 - 100);  // 75 = half of popup height

        dialogStage.show();
    }

    public boolean checkWinner(int row, int col) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            // Check rows and columns for a win
            Button chip = buttonBoard[row][col];
            if ((buttonBoard[i][0].getStyle() == chip.getStyle() && buttonBoard[i][1].getStyle() == chip.getStyle() && buttonBoard[i][2].getStyle() == chip.getStyle()) ||
                    (buttonBoard[0][i].getStyle() == chip.getStyle() && buttonBoard[1][i].getStyle() == chip.getStyle() && buttonBoard[2][i].getStyle() == chip.getStyle())) {
                return true;
            }
        }
        Button chip = buttonBoard[row][col];
        // Check diagonals for a win
        return (buttonBoard[0][0].getStyle() == chip.getStyle() && buttonBoard[1][1].getStyle() == chip.getStyle() && buttonBoard[2][2].getStyle() == chip.getStyle()) ||
                (buttonBoard[0][2].getStyle() == chip.getStyle() && buttonBoard[1][1].getStyle() == chip.getStyle() && buttonBoard[2][0].getStyle() == chip.getStyle());
    }

    public boolean boardFull() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                String string = buttonBoard[row][col].getStyle();
                if (!string.equals("-fx-font-size: 36px; -fx-text-fill: red;") && !string.equals("-fx-font-size: 36px; -fx-text-fill: deepskyblue;")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void makeAIMove() {
        int[] move = findNextMove();
        if (move != null) {
            Button aiButton = buttonBoard[move[0]][move[1]];
            handleMove(move[0], move[1], aiButton);
        }
    }

    private int[] findNextMove() {
        List<int[]> emptySpots = new ArrayList<>();
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (buttonBoard[r][c].getText().isEmpty()) {
                    emptySpots.add(new int[]{r, c});
                }
            }
        }

        if (emptySpots.isEmpty()) {
            return null;
        }

        return emptySpots.get((int)(Math.random() * emptySpots.size()));
    }
}
