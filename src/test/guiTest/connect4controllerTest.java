package guiTest;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.gui.CheckersBoard;
import org.seng.gui.Connect4Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class connect4controllerTest {
    @Test
    public void testInGameChat1() {
        CheckersBoard board = new CheckersBoard();
        String testMessage = "hello!";

        board.saveMessage(testMessage);

        String string = board.loadChatHistory();
        assertTrue(string.contains(testMessage));
    }



}

