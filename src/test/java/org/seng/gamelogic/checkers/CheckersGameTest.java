package org.seng.gamelogic.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckersGameTest {

    private CheckersGame game;

    @BeforeEach
    void setUp() {
        game = new CheckersGame();
    }

    @Test
    void testRedValidTurn() {
        // Assume red piece at (5, 0) in starting position
        assertTrue(game.isValidTurn(5, 0), "Red should be able to select its piece at (5, 0)");
    }

    @Test
    void testBlackInvalidTurnOnRedTurn() {
        // Try to move black's piece when it's red's turn
        assertFalse(game.isValidTurn(2, 1), "Black piece at (2, 1) shouldn't be selectable on red's turn");
    }

    @Test
    void testRedWinCondition() {
        // Remove all black pieces manually
        CheckersBoard board = new CheckersBoard();
        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                CheckersBoard.Piece piece = board.getPieceAt(row, col);
                if (piece == CheckersBoard.Piece.BLACK || piece == CheckersBoard.Piece.BLACK_KING) {
                    board.setPieceAt(row, col, CheckersBoard.Piece.EMPTY);
                }
            }
        }

        // Inject this board into the game
        var redWinGame = new CheckersGameWithBoard(board, true);
        assertTrue(redWinGame.checkWinCondition(), "Red should win if no black pieces are left.");
    }

    @Test
    void testBlackWinCondition() {
        // Remove all red pieces manually
        CheckersBoard board = new CheckersBoard();
        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                CheckersBoard.Piece piece = board.getPieceAt(row, col);
                if (piece == CheckersBoard.Piece.RED || piece == CheckersBoard.Piece.RED_KING) {
                    board.setPieceAt(row, col, CheckersBoard.Piece.EMPTY);
                }
            }
        }

        var blackWinGame = new CheckersGameWithBoard(board, false);
        assertTrue(blackWinGame.checkWinCondition(), "Black should win if no red pieces are left.");
    }
}