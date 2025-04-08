package org.seng.gamelogic.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.gamelogic.connectfour.ConnectFourBoard;
import org.seng.gamelogic.connectfour.ConnectFourPlayer;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CheckersGameTest {

    private CheckersBoard board;
    private CheckersPlayer[] players;
    private int gameID;
    private CheckersGame game;

    @BeforeEach
    void setUp() {
        board = new CheckersBoard();
        players = new CheckersPlayer[]{new CheckersPlayer("Player1", "a", "1"), new CheckersPlayer("Player2", "b", "2")};
        gameID = 1;
        game = new CheckersGame(board, players, gameID);
    }

    @Test
    void testRedValidTurn() {
        assertTrue(game.isValidTurn(5, 0), "Red should be able to select its piece at (5, 0)");
    }

    @Test
    void testBlackInvalidTurnOnRedTurn() {
        assertFalse(game.isValidTurn(2, 1), "Black shouldn't move on red's turn");
    }

    @Test
    void testRedWinCondition() throws Exception {
        CheckersBoard board = new CheckersBoard();
        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                if (board.getPieceAt(row, col) == CheckersBoard.Piece.BLACK ||
                        board.getPieceAt(row, col) == CheckersBoard.Piece.BLACK_KING) {
                    board.setPieceAt(row, col, CheckersBoard.Piece.EMPTY);
                }
            }
        }

        TestableCheckersGame redWinGame = new TestableCheckersGame(board, players, gameID, true);
        assertTrue(redWinGame.invokeCheckWinCondition(), "Red should win when no black pieces are left.");
    }

    @Test
    void testBlackWinCondition() throws Exception {
        CheckersBoard board = new CheckersBoard();
        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                if (board.getPieceAt(row, col) == CheckersBoard.Piece.RED ||
                        board.getPieceAt(row, col) == CheckersBoard.Piece.RED_KING) {
                    board.setPieceAt(row, col, CheckersBoard.Piece.EMPTY);
                }
            }
        }

        TestableCheckersGame blackWinGame = new TestableCheckersGame(board, players, gameID, false);
        assertTrue(blackWinGame.invokeCheckWinCondition(), "Black should win when no red pieces are left.");
    }

    // Subclass to inject board and call private methods
    static class TestableCheckersGame extends CheckersGame {
        public TestableCheckersGame(CheckersBoard board, CheckersPlayer[] players, int gameID, boolean isRedTurn) {
            super(board, players, gameID);
            try {
                var boardField = CheckersGame.class.getDeclaredField("board");
                var turnField = CheckersGame.class.getDeclaredField("isRedTurn");
                boardField.setAccessible(true);
                turnField.setAccessible(true);
                boardField.set(this, board);
                turnField.set(this, isRedTurn);
            } catch (Exception e) {
                throw new RuntimeException("Failed to inject test board", e);
            }
        }

        public boolean invokeCheckWinCondition() throws Exception {
            Method method = CheckersGame.class.getDeclaredMethod("checkWinCondition");
            method.setAccessible(true);
            return (boolean) method.invoke(this);
        }
    }
}
