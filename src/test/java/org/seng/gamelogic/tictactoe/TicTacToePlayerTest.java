package org.seng.gamelogic.tictactoe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicTacToePlayerTest {

    @Test
    public void testSymbolSetAndGet() {
        TicTacToePlayer player = new TicTacToePlayer("Test", 1, 'O', 0);
        assertEquals('O', player.getSymbol());
        player.setSymbol('X');
        assertEquals('X', player.getSymbol());
    }

    @Test
    public void testMakeMove() {
        TicTacToeBoard board = new TicTacToeBoard();
        TicTacToePlayer[] players = new TicTacToePlayer[]{new TicTacToePlayer("Player1", 1, 'b', 1), new TicTacToePlayer("Player2", 2, 'y', 1)};
        TicTacToeGame game = new TicTacToeGame(board, players, 4);
        TicTacToeMove move = new TicTacToeMove(0, 0, 'X');
        TicTacToePlayer player = new TicTacToePlayer("Test", 1, 'X', 0);

        boolean success = player.makeMove(game, move);
        assertTrue(success);
    }

    @Test
    public void testReadyStart() {
        TicTacToePlayer player = new TicTacToePlayer("Test", 1, 'X', 0);
        assertTrue(player.readyStart());
    }
}