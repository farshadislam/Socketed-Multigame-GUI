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
        players = new ConnectFourPlayer[]{new ConnectFourPlayer("Player1", "test@gmail.com", "tester"), new ConnectFourPlayer("Player2", "practice@gmail.com", "practice")};
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
        ConnectFourPlayer initialPlayer = game.currentPlayer;
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

    @Test
    void testBoardFullOne() {
        assertFalse(game.boardFull());
    }

    @Test
    void testBoardFullTwo() {
        for (int row = 0; row < 6; row ++){
            for (int col = 0; col < 7; col ++){
                board.setChip(row, col, ConnectFourBoard.Chip.YELLOW);
            }
        }
        assertTrue(game.boardFull());
    }

    @Test
    void testMoveOne() {
        game.currentPlayer = players[0];
        game.makeMove(3);
        assertSame(board.getChip(6, 3), ConnectFourBoard.Chip.BLUE);
    }

    @Test
    void testMoveTwo() {
        game.currentPlayer = players[0];
        assertTrue(game.makeMove(3));
    }

    @Test
    void testMoveThree() {
        game.currentPlayer = players[0];
        assertFalse(game.makeMove(7));
    }

    @Test
    void testMoveFour() {
        game.currentPlayer = players[1];
        game.makeMove(3);
        assertSame(board.getChip(6, 3), ConnectFourBoard.Chip.YELLOW);
    }

    @Test
    void testSetStatus() {
        game.setStatus(players[0], 2);
        assertEquals(game.status, "Player Player1 has dropped a piece in column 2");
    }
}