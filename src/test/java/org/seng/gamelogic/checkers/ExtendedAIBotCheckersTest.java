package org.seng.gamelogic.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExtendedAIBotCheckersTest {

    private CheckersBoard board;
    private ExtendedAIBotCheckers bot;
    private CheckersPlayer[] players;
    private CheckersGame game;

    @BeforeEach
    public void setup() {
        board = new CheckersBoard();
        players = new CheckersPlayer[2];
        players[0] = new ExtendedAIBotCheckers("BotPlayer", 1, 'b', 1, null);
        players[1] = new CheckersPlayer("Opponent", 2, 'r', 1);
        game = new CheckersGame(board, players, 1);

        // Inject proper game instance into bot
        bot = (ExtendedAIBotCheckers) players[0];
    }

    @Test
    public void testNextMoveReturnsValidMove() {
        Object moveObj = bot.nextMove(board);
        assertNotNull(moveObj, "Bot should return a valid move object");

        int[] move = (int[]) moveObj;

        assertEquals(4, move.length, "Move should be an int array of length 4");
        assertTrue(board.isValidMove(move[0], move[1], move[2], move[3]), "Move returned by bot should be valid");
    }

    @Test
    public void testMakeMoveSuccess() {
        Object moveObj = bot.nextMove(board);
        assertNotNull(moveObj, "Bot should return a move");

        boolean result = bot.makeMove(board, moveObj);
        assertTrue(result, "Bot's move should be successful");
    }

    @Test
    public void testMakeMoveWithInvalidObjectTypes() {
        assertFalse(bot.makeMove(board, "not a move"), "Invalid move object should return false");
        assertFalse(bot.makeMove("not a board", new int[]{0, 0, 1, 1}), "Invalid board object should return false");
    }

    @Test
    public void testNextMoveNoAvailableMoves() {
        // Set up board with no black pieces
        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                board.setPieceAt(row, col, CheckersBoard.Piece.RED);
            }
        }

        Object move = bot.nextMove(board);
        assertNull(move, "Bot should return null if no valid moves");
    }
}
