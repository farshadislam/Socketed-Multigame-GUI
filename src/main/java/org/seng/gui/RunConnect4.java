package org.seng.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.IOException;

public class RunConnect4 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/org/seng/gui/fonts/Monoton-Regular.ttf"), 64);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connect4-board.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 450);

        // Load style sheet
        scene.getStylesheets().add(getClass().getResource("connectfourstyles.css").toExternalForm());
        //scene.getStylesheets().add(getClass().getResource("checkerstyles.css").toExternalForm());
        //scene.getStylesheets().add(getClass().getResource("tictactoestyles.css").toExternalForm());

        stage.setTitle("OMG Platform");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}