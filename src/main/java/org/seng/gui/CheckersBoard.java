package org.seng.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckersBoard {

    @FXML
    private Button a1;

    @FXML
    private Button a2;

    @FXML
    private Button a3;

    @FXML
    private Button a4;

    @FXML
    private Button a5;

    @FXML
    private Button a6;

    @FXML
    private Button a7;

    @FXML
    private Button a8;

    @FXML
    private Button b1;

    @FXML
    private Button b2;

    @FXML
    private Button b3;

    @FXML
    private Button b4;

    @FXML
    private Button b5;

    @FXML
    private Button b6;

    @FXML
    private Button b7;

    @FXML
    private Button b8;

    @FXML
    private Button c1;

    @FXML
    private Button c2;

    @FXML
    private Button c3;

    @FXML
    private Button c4;

    @FXML
    private Button c5;

    @FXML
    private Button c6;

    @FXML
    private Button c7;

    @FXML
    private Button c8;

    @FXML
    private Button d1;

    @FXML
    private Button d2;

    @FXML
    private Button d3;

    @FXML
    private Button d4;

    @FXML
    private Button d5;

    @FXML
    private Button d6;

    @FXML
    private Button d7;

    @FXML
    private Button d8;

    @FXML
    private Button e1;

    @FXML
    private Button e2;

    @FXML
    private Button e3;

    @FXML
    private Button e4;

    @FXML
    private Button e5;

    @FXML
    private Button e6;

    @FXML
    private Button e7;

    @FXML
    private Button e8;

    @FXML
    private Button f1;

    @FXML
    private Button f2;

    @FXML
    private Button f3;

    @FXML
    private Button f4;

    @FXML
    private Button f5;

    @FXML
    private Button f6;

    @FXML
    private Button f7;

    @FXML
    private Button f8;

    @FXML
    private Button g1;

    @FXML
    private Button g2;

    @FXML
    private Button g3;

    @FXML
    private Button g4;

    @FXML
    private Button g5;

    @FXML
    private Button g6;

    @FXML
    private Button g7;

    @FXML
    private Button g8;

    @FXML
    private Button h1;

    @FXML
    private Button h2;

    @FXML
    private Button h3;

    @FXML
    private Button h4;

    @FXML
    private Button h5;

    @FXML
    private Button h6;

    @FXML
    private Button h7;

    @FXML
    private Button h8;

    @FXML
    private Button inGameChatButton;

    @FXML
    private MenuItem helpOption;

    private Button selectedPiece = null;
    private Image redPieceImage;
    private Image blackPieceImage;


    @FXML
    public void initialize() {
        // Load images
        redPieceImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/redpiece.png"));
        blackPieceImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/blackpiece.png"));

        setupPieces();
        selectionHandle();
        clearChatHistory();
    }

    private void setupPieces() {
        // Black pieces
        placePiece(a1, blackPieceImage); placePiece(c1, blackPieceImage);
        placePiece(e1, blackPieceImage); placePiece(g1, blackPieceImage);
        placePiece(b2, blackPieceImage); placePiece(d2, blackPieceImage);
        placePiece(f2, blackPieceImage); placePiece(h2, blackPieceImage);
        placePiece(a3, blackPieceImage); placePiece(c3, blackPieceImage);
        placePiece(e3, blackPieceImage); placePiece(g3, blackPieceImage);

        // Red pieces
        placePiece(b6, redPieceImage); placePiece(d6, redPieceImage);
        placePiece(f6, redPieceImage); placePiece(h6, redPieceImage);
        placePiece(a7, redPieceImage); placePiece(c7, redPieceImage);
        placePiece(e7, redPieceImage); placePiece(g7, redPieceImage);
        placePiece(b8, redPieceImage); placePiece(d8, redPieceImage);
        placePiece(f8, redPieceImage); placePiece(h8, redPieceImage);
    }

    private void selectionHandle() {
        Button[] allButtons = {a1, a2, a3, a4, a5, a6, a7, a8, b1, b2, b3, b4, b5, b6, b7, b8, c1, c2, c3, c4, c5, c6, c7, c8, d1, d2, d3, d4, d5, d6, d7, d8,
                e1, e2, e3, e4, e5, e6, e7, e8, f1, f2, f3, f4, f5, f6, f7, f8, g1, g2, g3, g4, g5, g6, g7, g8, h1, h2, h3, h4, h5, h6, h7, h8};

        for (Button button : allButtons) {
            button.setOnAction(e -> handleButtonClick(button));
        }
    }

    private void handleButtonClick(Button clickedButton) {
        if (clickedButton.getGraphic() != null) { // if the button clicked has a piece
            if (selectedPiece != null && selectedPiece != clickedButton) { // deselects current piece if it is different from clicked one
                deselectPiece();
            }
            if (selectedPiece == clickedButton) { // if the selected piece is the clicked again on clicked button it deselects it
                deselectPiece();
            } else {
                selectPiece(clickedButton);
            }
        }
        else {
            if (selectedPiece != null) { // if it is empty deselect the piece
                deselectPiece();
            }
        }
    }

    private void selectPiece(Button button) {
        selectedPiece = button;
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.GREENYELLOW);
        highlight.setRadius(40);
        button.setEffect(highlight);
    }

    private void deselectPiece() {
        if (selectedPiece != null) {
            selectedPiece.setEffect(null);
            selectedPiece = null;
        }
    }

    private void placePiece(Button button, Image pieceImage) {
        ImageView imageView = new ImageView(pieceImage);
        imageView.setFitWidth(33);
        imageView.setFitHeight(33);
        button.setGraphic(imageView);
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

    private void clearChatHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHAT_LOG_PATH))) {
            writer.write(""); // Clear the contents
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // how to play rules
    @FXML
    void howToPlayDescription(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE); // No default icon

        alert.setTitle("Help");
        alert.setHeaderText("How to Play");

        // Set the content
        alert.setContentText(
                "1. Players take turns moving their pieces diagonally forward.\n\n"
                        + "2. Regular pieces can only move forward on black squares.\n\n"
                        + "3. Capture an opponent's piece by jumping over it diagonally.\n\n"
                        + "4. If a piece reaches the last row, it becomes a King.\n\n"
                        + "5. Kings can move and capture both forward and backward.\n\n"
                        + "6. If a player has no valid moves left, they lose the game. \n"
        );

        alert.getButtonTypes().add(javafx.scene.control.ButtonType.OK);
        // Apply CSS file
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("gameChat.css").toExternalForm());

        dialogPane.setPrefWidth(400); // Set a preferred width

        alert.showAndWait();
    }
}

