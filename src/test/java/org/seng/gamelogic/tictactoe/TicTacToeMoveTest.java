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
        TicTacToeBoard board = new TicTacToeBoard();
        TicTacToePlayer[] players = new TicTacToePlayer[]{new TicTacToePlayer("Player1", 1, 'b', 1), new TicTacToePlayer("Player2", 2, 'y', 1)};
        TicTacToeGame game = new TicTacToeGame(board, players, 3);
        TicTacToeMove move = new TicTacToeMove(1, 1, 'X');
        String details = move.getMoveDetails(game);
        assertEquals("X", details);
    }
}