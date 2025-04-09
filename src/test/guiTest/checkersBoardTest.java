package guiTest;

import org.junit.jupiter.api.Test;
import org.seng.gui.CheckersBoard;

import static org.junit.jupiter.api.Assertions.*;

public class checkersBoardTest {

    @Test
    public void testInGameChat1() {
        CheckersBoard board = new CheckersBoard();
        String testMessage = "hello!";

        board.saveMessage(testMessage);

        String fileContent = board.loadChatHistory();
        assertTrue(fileContent.contains(testMessage));
    }






}
