package org.seng.gamelogic.connectfour;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedAIBotConnectFourTest {

    private ConnectFourGame game;
    private ConnectFourBoard board;
    private ExtendedAIBotConnectFour aiBot;
    private ConnectFourPlayer player1;
    private ConnectFourPlayer player2;

    @BeforeEach
    void setUp() {
        // Initialize players
        player1 = new ConnectFourPlayer("Player1", 1, 'b', 0);
        player2 = new ConnectFourPlayer("Player2", 2, 'y', 1);

        // Set up the board and the game
        board = new ConnectFourBoard();
        game = new ConnectFourGame(board, new ConnectFourPlayer[]{player1, player2}, 1);

        // Initialize AI bot with player2 (yellow) symbol
        aiBot = new ExtendedAIBotConnectFour('y', game, board);
    }

    @Test
    void testMakeMove_Success() {
        // Create a valid move for the AI bot
        ConnectFourMove move = new ConnectFourMove(player2, 2, -1);

        // Attempt to make the move and assert success
        boolean result = aiBot.makeMove(board, move);
        assertTrue(result, "Move should be successful");
    }

    @Test
    void testMakeMove_BoardMismatch() {
        // Create a different board to test mismatch
        ConnectFourBoard differentBoard = new ConnectFourBoard();
        ConnectFourMove move = new ConnectFourMove(player2, 2, -1);

        boolean result = aiBot.makeMove(differentBoard, move);
        assertFalse(result, "Move should fail due to board mismatch");
    }

    @Test
    void testMakeMove_InvalidMove() {
        // Passing a move where player doesn't match AI bot
        ConnectFourMove invalidMove = new ConnectFourMove(player1, 2, -1);

        boolean result = aiBot.makeMove(board, invalidMove);
        assertFalse(result, "Move should fail due to invalid player");
    }

    @Test
    void testFindNextMove() {
        // Set some columns as not full (you can adjust according to your board setup)
        board.setChip(0, 0, ConnectFourBoard.Chip.YELLOW);
        board.setChip(0, 1, ConnectFourBoard.Chip.EMPTY);
        board.setChip(0, 2, ConnectFourBoard.Chip.EMPTY);

        Integer nextMove = aiBot.findNextMove(board);
        assertTrue(nextMove == 1 || nextMove == 2, "AI should choose an available column");
    }

    @Test
    void testFindNextMove_NoAvailableMoves() {
        // Set all columns as full
        for (int i = 0; i < ConnectFourBoard.COL_COUNT; i++) {
            for (int j = 0; j < ConnectFourBoard.ROW_COUNT; j++) {
                board.setChip(i, j, ConnectFourBoard.Chip.YELLOW);
            }
        }

        Integer nextMove = aiBot.findNextMove(board);
        assertNull(nextMove, "AI should return null when no valid moves are available");
    }

    @Test
    void testNextMove_AITurn() {
        // Manually set the current player to AI bot
        game.currentPlayer = aiBot;

        ConnectFourMove move = aiBot.nextMove(board);
        assertNotNull(move, "AI should generate a move when it's its turn");
    }

    @Test
    void testNextMove_OtherPlayersTurn() {
        // Manually set the current player to another player
        game.currentPlayer = player1;

        ConnectFourMove move = aiBot.nextMove(board);
        assertNull(move, "AI should not generate a move if it's not its turn");
    }

    @Test
    void testCharToChip() {
        assertEquals(ConnectFourBoard.Chip.BLUE, ExtendedAIBotConnectFour.charToChip('b'));
        assertEquals(ConnectFourBoard.Chip.YELLOW, ExtendedAIBotConnectFour.charToChip('y'));
        assertEquals(ConnectFourBoard.Chip.EMPTY, ExtendedAIBotConnectFour.charToChip(' '));
    }

    @Test
    void testChipToChar() {
        assertEquals('b', ExtendedAIBotConnectFour.chipToChar(ConnectFourBoard.Chip.BLUE));
        assertEquals('y', ExtendedAIBotConnectFour.chipToChar(ConnectFourBoard.Chip.YELLOW));
        assertEquals(' ', ExtendedAIBotConnectFour.chipToChar(ConnectFourBoard.Chip.EMPTY));
    }

    @Test
    void testSetSymbol() {
        aiBot.setSymbol('y');
        assertEquals('y', aiBot.getSymbol(), "Symbol should be updated to 'y'");
    }

    @Test
    void testGetSymbol() {
        assertEquals('y', aiBot.getSymbol(), "Symbol should be 'y' initially");
    }
}
