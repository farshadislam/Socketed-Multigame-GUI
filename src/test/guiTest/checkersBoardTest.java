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

        String string = board.loadChatHistory();
        assertTrue(string.contains(testMessage));
    }

    @Test
    public void testInGameChatClear() {
        CheckersBoard board = new CheckersBoard();
        board.saveMessage("checking1234");

        board.clearChatHistory();

        String string = board.loadChatHistory();
        assertTrue(string.isEmpty());

    }



}
