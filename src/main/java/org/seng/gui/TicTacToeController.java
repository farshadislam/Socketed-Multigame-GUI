package org.seng.gui;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class TicTacToeController {

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    @FXML
    private Button button7;

    @FXML
    private Button button8;

    @FXML
    private Button button9;

    @FXML
    private Button inGameChatButton;


    @FXML
    public void initialize() {}

    @FXML
    private void openChat() {
        Stage chatStage = new Stage();
        chatStage.setTitle("In-Game Chat");

        TextArea chatArea = new TextArea();
        chatArea.setPromptText("Type your message...");
        chatArea.setPrefSize(300, 200);

        VBox layout = new VBox(10);
        layout.getChildren().add(chatArea);

        Scene scene = new Scene(layout, 320, 240);
        chatStage.setScene(scene);
        chatStage.show();
    }
}

