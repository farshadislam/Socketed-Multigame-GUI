package org.seng.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.seng.networking.SocketGameClient;


import java.io.IOException;

public class GameChatController {


    @FXML
    private Button closeChatButton;
    private Stage chatStage;
    @FXML
    private ScrollPane chatScrollPane;

    @FXML private VBox chatBox;
    @FXML private TextField messageField;
    @FXML private Button sendButton;

    private SocketGameClient client;
    private String username;

    private Thread listenerThread;

    public void init(SocketGameClient client, String username) {
        this.client = client;
        this.username = username;

        listenerThread = new Thread(() -> {
            try {
                String message;
                while ((message = client.receiveMessage()) != null) {
                    final String msg = message;

                    if (msg.startsWith("CHAT:")) {
                        String actual = msg.substring("CHAT:".length());
                        Platform.runLater(() -> addMessage(actual, false));
                    }
                }
            } catch (IOException e) {
                Platform.runLater(() -> addMessage("⚠ Disconnected from server.", false));
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void handleIncomingChat(String msg) {
        Platform.runLater(() -> addMessage(msg, false));
    }

    @FXML
    public void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            String msg = "CHAT:" + username + ": " + text;
            try {
                client.sendMessage(msg);
                addMessage(username + ": " + text, true);
                messageField.clear();
            } catch (IOException e) {
                addMessage("⚠ Failed to send message.", false);
            }
        }
    }


    private void addMessage(String msg, boolean isLocal) {
        Text text = new Text(msg);
        text.setStyle("-fx-fill: white; -fx-font-size: 14px;");

        TextFlow bubble = new TextFlow(text);
        bubble.setStyle("-fx-background-color: " +
                (isLocal ? "#a855f7" : "#9f86c0") +
                "; -fx-background-radius: 20; -fx-padding: 10; -fx-max-width: 350;");

        chatBox.getChildren().add(bubble);
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }
    @FXML
    public void onCloseChatClicked() {
        try {
            if (client != null) {
                client.sendMessage("CLOSE_CHAT");
            }
            handleCloseChat();
        } catch (IOException e) {
            System.err.println("[Chat] Failed to send CLOSE_CHAT.");
            e.printStackTrace();
        }
    }

    public void handleCloseChat() {
        if (listenerThread != null && listenerThread.isAlive()) {
            listenerThread.interrupt();
            System.out.println("[Chat] listener thread interrupted.");
        }
        Platform.runLater(() -> {
            Stage stage = (Stage) closeChatButton.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        });
    }




}
