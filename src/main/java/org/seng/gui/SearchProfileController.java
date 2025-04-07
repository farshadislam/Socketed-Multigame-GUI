package org.seng.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.util.ArrayList;
import java.util.List;

public class SearchProfileController {

    @FXML private TextField searchField;
    @FXML private ListView<String> resultsList;
    @FXML private Button viewProfileButton;

    private final List<String> allProfiles = new ArrayList<String>() {{
        add("Alice");
        add("Bob");
        add("Charlie");
    }};

    @FXML
    public void initialize() {
        resultsList.setItems(FXCollections.observableArrayList());
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.trim().isEmpty()) {
                resultsList.setItems(FXCollections.observableArrayList());
            } else {
                List<String> matches = new ArrayList<>();
                for (String name : allProfiles) {
                    if (name.toLowerCase().contains(newText.toLowerCase().trim())) {
                        matches.add(name);
                    }
                }
                resultsList.setItems(FXCollections.observableArrayList(matches));
            }
        });

        viewProfileButton.setOnAction(e -> openProfile());
    }

    private void openProfile() {
        String selected = resultsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("friend-profile.fxml"));
                Parent root = loader.load();
                FriendProfileController controller = loader.getController();
                controller.setProfileData(selected, "Last Online: 2 hours ago", 3, 2, 0);
                Scene scene = new Scene(root, 700, 450);
                scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                Stage stage = new Stage();
                stage.setTitle(selected + "'s Profile");
                stage.setScene(scene);
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
