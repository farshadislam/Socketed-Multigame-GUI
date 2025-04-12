package org.seng.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.gamelogic.tictactoe.TicTacToeController;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class TicTacToeControllerTest extends ApplicationTest {

    private TicTacToeController controller;

    @Override
    public void start(Stage stage) {
        controller = new TicTacToeController();

        // Simulate @FXML injection manually
        controller.cell00 = new Button();
        controller.cell01 = new Button();
        controller.cell02 = new Button();
        controller.cell10 = new Button();
        controller.cell11 = new Button();
        controller.cell12 = new Button();
        controller.cell20 = new Button();
        controller.cell21 = new Button();
        controller.cell22 = new Button();
        controller.chatArea = new TextArea();
        controller.messageInput = new TextField();
        controller.statusLabel = new Label();

        controller.initialize();
    }

    @BeforeEach
    public void resetGameBeforeEach() {
        controller.resetGame();
    }

    @Test
    public void testSendMessage_AppendsToChatArea() {
        controller.messageInput.setText("Hello!");
        controller.sendMessage();
        assertTrue(controller.chatArea.getText().contains("You: Hello!"));
    }

    @Test
    public void testHandleCellClick_PlacesMark() {
        Button cell = controller.cell00;
        clickOn(cell);
        assertEquals("X", cell.getText());
    }

    @Test
    public void testResetGame_ClearsBoard() {
        controller.cell00.setText("X");
        controller.cell11.setText("O");
        controller.resetGame();

        assertEquals("", controller.cell00.getText());
        assertEquals("", controller.cell11.getText());
        assertEquals("Player X's turn", controller.statusLabel.getText());
    }

    @Test
    public void testWinningCondition_Displayed() {
        // X wins
        controller.cell00.fire(); // X
        controller.cell10.fire(); // O
        controller.cell01.fire(); // X
        controller.cell11.fire(); // O
        controller.cell02.fire(); // X wins

        assertTrue(controller.statusLabel.getText().contains("wins"));
    }

    @Test
    public void testDrawCondition_Displayed() {
        // Fill board with draw scenario
        controller.cell00.fire(); // X
        controller.cell01.fire(); // O
        controller.cell02.fire(); // X
        controller.cell10.fire(); // O
        controller.cell11.fire(); // X
        controller.cell12.fire(); // O
        controller.cell20.fire(); // X
        controller.cell22.fire(); // O
        controller.cell21.fire(); // X

        assertTrue(controller.statusLabel.getText().toLowerCase().contains("draw"));
    }
}
