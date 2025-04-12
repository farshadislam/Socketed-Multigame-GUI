package org.seng.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.IOException;
import org.seng.authentication.*;

public class HelloApplication extends Application {
    public static CredentialsDatabase database;
    public static LoginPage loginPage;
    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/org/seng/gui/fonts/Monoton-Regular.ttf"), 64);

        // Load FXML for welcome page
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 450);

        // Load style sheet
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
//        scene.getStylesheets().add(getClass().getResource("connectfourstyles.css").toExternalForm());
//        scene.getStylesheets().add(getClass().getResource("checkerstyles.css").toExternalForm());
//        scene.getStylesheets().add(getClass().getResource("tictactoestyles.css").toExternalForm());
//        scene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

        // Get the controller of the loaded FXML
        WelcomePageController controller = fxmlLoader.getController();

        // Initialize the LoginPage and pass it to the controller
        database = new CredentialsDatabase(); //initialize in static way
        database.loadDatabase("output.txt");
        loginPage = new LoginPage(database);
        // Set up the stage
        stage.setTitle("OMG Platform");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
