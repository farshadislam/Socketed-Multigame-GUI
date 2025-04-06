package org.seng.gamelogic.tictactoe;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.seng.gamelogic.tictactoe.*;

public class TicTacToeMoveTest {

    @Test
    public void testMoveCreation() {
        TicTacToeMove move = new TicTacToeMove(2, 1, 'X');
        assertEquals(1, move.getRow());
        assertEquals(2, move.getCol());
        assertEquals('X', move.getSymbol());
    }

    @Test
    public void testGetMoveDetails() {
        TicTacToeGame game = new TicTacToeGame();
        TicTacToeMove move = new TicTacToeMove(1, 1, 'X');
        String details = move.getMoveDetails(game);
        assertEquals("X", details);
    }
}