package guiTest;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import org.seng.gui.ProfilePageController;
import org.seng.gui.TicTacToeController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class tictactoeTest {
    @Test
    public void testInGameChat1() {
        TicTacToeController board = new TicTacToeController();
        String testMessage = "hello!";

        board.saveMessage(testMessage);

        String string = board.loadChatHistory();
        assertTrue(string.contains(testMessage));
    }

    @Test
    public void testInGameChatClear() {
        TicTacToeController board = new TicTacToeController();
        board.saveMessage("checking1234");

        board.clearChatHistory();

        String string = board.loadChatHistory();
        assertTrue(string.isEmpty());

    }


//    @Test
//    void testInitializeRunsSafely() throws Exception {
//        TicTacToeController controller = new TicTacToeController();
//
//        // Inject all 9 buttons
//        for (int i = 1; i <= 9; i++) {
//            Field buttonField = TicTacToeController.class.getDeclaredField("button" + i);
//            buttonField.setAccessible(true);
//            buttonField.set(controller, new Button());
//        }
//
//        assertDoesNotThrow(controller::initialize);
//    }
}


