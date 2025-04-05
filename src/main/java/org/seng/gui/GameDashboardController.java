package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.seng.authentication.*;

public class GameDashboardController {

    @FXML
    private Label dashboardLabel;
    private HomePage homePage;

    @FXML
    public void initialize() {
    }
    public void setHomePage(HomePage homePage){
        this.homePage = homePage;
    }
}
