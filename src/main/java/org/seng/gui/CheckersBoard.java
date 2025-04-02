package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    public void initialize() {
        setupPieces();
    }
    private Button selectPiece = null;


    private void setupPieces() {
        Image redPiece = new Image(getClass().getResourceAsStream("/org/seng/gui/images/redpiece.png"));
        Image blackPiece = new Image(getClass().getResourceAsStream("/org/seng/gui/images/blackpiece.png"));

        // Place red pieces on the top 3 rows
        placePiece(a1, blackPiece); placePiece(c1, blackPiece); placePiece(e1, blackPiece); placePiece(g1, blackPiece);
        placePiece(b2, blackPiece); placePiece(d2, blackPiece); placePiece(f2, blackPiece); placePiece(h2, blackPiece);
        placePiece(a3, blackPiece); placePiece(c3, blackPiece); placePiece(e3, blackPiece); placePiece(g3, blackPiece);

        // Place black pieces on the bottom 3 rows
        placePiece(b6, redPiece); placePiece(d6, redPiece); placePiece(f6, redPiece); placePiece(h6, redPiece);
        placePiece(a7, redPiece); placePiece(c7, redPiece); placePiece(e7, redPiece); placePiece(g7, redPiece);
        placePiece(b8, redPiece); placePiece(d8, redPiece); placePiece(f8, redPiece); placePiece(h8, redPiece);
    }

    private void placePiece(Button button, Image pieceImage) {
        ImageView imageView = new ImageView(pieceImage);
        imageView.setFitWidth(33);  // Adjust size as needed
        imageView.setFitHeight(33);
        button.setGraphic(imageView);

        button.setOnAction(e -> selectedPiece(button)); // highlights the piece that is selected currently
    }

    private void selectedPiece(Button button){
        if (selectPiece != null) {
            selectPiece.setEffect(null);  // Remove highlight from previously selected piece
        }

        if (selectPiece == button) {
            // Deselect if the same button is clicked again
            selectPiece = null;
        } else {
            // Highlight the selected piece
            DropShadow highlight = new DropShadow();
            highlight.setColor(Color.GREENYELLOW);
            highlight.setRadius(40);
            button.setEffect(highlight);
            selectPiece = button;
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
}

