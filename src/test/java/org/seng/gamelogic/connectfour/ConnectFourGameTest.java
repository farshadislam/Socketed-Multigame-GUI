package org.seng.gamelogic.connectfour;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.gamelogic.Player;

import static org.junit.jupiter.api.Assertions.*;

class ConnectFourGameTest {

    private ConnectFourBoard board;
    private ConnectFourPlayer[] players;
    private ConnectFourGame game;

    @BeforeEach
    void setUp() {
        board = new ConnectFourBoard();
        players = new ConnectFourPlayer[]{new ConnectFourPlayer("Player1", 1, 'b', 1), new ConnectFourPlayer("Player2", 2, 'y', 1)};
        game = new ConnectFourGame(board, players, 1);
    }

    @Test
    void testConstructor() {
        assertNotNull(game.board);
        assertNotNull(game.players);
        assertEquals("In Progress", game.status);
        assertEquals(players[0], game.currentPlayer);
    }

    @Test
    void testSendMessage() {
        game.sendMessage("Hello");
        assertTrue(game.getChatLog().contains("Hello"));
    }

    @Test
    void testMakeMove() {
        assertTrue(game.makeMove(3)); // assuming column 3 is valid
    }

    @Test
    void testSwitchTurn() {
        Player initialPlayer = game.currentPlayer;
        game.switchTurn();
        assertNotEquals(initialPlayer, game.currentPlayer);
    }

    @Test
    void testCheckWinner() {
        // Assuming board setup logic is in place, write tests for checking a winner
        // Use mock data or prepare a specific board configuration where there is a winner
        assertTrue(game.checkWinner(ConnectFourBoard.Chip.BLUE));
        assertFalse(game.checkWinner(ConnectFourBoard.Chip.EMPTY));
    }
}