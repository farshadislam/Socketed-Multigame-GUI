package org.seng.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("   Hello there! Welcome aboard to the OMG Platform - your gateway to endless fun! \n" +
                "                            Whether you're here to play alone or with friends, we got you! \n" +
                "                               And remember there's only one rule: that is to HAVE FUN! Be sure to follow the rules and be a good team player!   ");
    }
}