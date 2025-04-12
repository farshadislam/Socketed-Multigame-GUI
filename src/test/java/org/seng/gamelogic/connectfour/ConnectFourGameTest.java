package org.seng.gamelogic.connectfour;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectFourGameTest {

    private ConnectFourBoard board;
    private ConnectFourPlayer[] players;
    private ConnectFourGame game;

    @BeforeEach
    public void setUp() {
        board = new ConnectFourBoard();
        players = new ConnectFourPlayer[]{
                new ConnectFourPlayer("Player1", "1", "b"),
                new ConnectFourPlayer("Player2", "2", "y")
        };
        game = new ConnectFourGame(board, players, 42);
        game.currentPlayer = players[0];
    }

    @Test
    public void testMakeMoveValidColumn() {
        boolean result = game.makeMove(0);
        assertTrue(result);
        assertEquals(ConnectFourBoard.Chip.BLUE, board.getChip(5, 0));
    }

    @Test
    public void testExitGameChangesStatus() {
        game.exitGame();
        assertEquals("Exiting Game", game.status);
    }
}
