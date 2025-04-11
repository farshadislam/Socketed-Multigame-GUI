package org.seng.guiTest;

import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.gui.Connect4Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Connect4ControllerTest {

    private Connect4Controller controller;

    @BeforeEach
    void setupController() {
        controller.initialize();
    }

    @Test
    void handleColumnClickTest() {
        controller.isPlayerOneTurn = true;
        controller.handleColumnClick(new Button(), 2);
        assertTrue(controller.boardButtons[5][1].getStyle() == "-fx-background-color: #00F0FF;");
    }
}
