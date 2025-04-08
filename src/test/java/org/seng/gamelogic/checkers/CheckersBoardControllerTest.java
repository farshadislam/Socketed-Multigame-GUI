package org.seng.gamelogic.checkers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckersBoardControllerTest {

    @Test
    public void testInitialBoardSetup() {
        CheckersBoard board = new CheckersBoard();

        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    if (row < 3) {
                        assertEquals(CheckersBoard.Piece.BLACK, board.getPieceAt(row, col));
                    } else if (row > 4) {
                        assertEquals(CheckersBoard.Piece.RED, board.getPieceAt(row, col));
                    } else {
                        assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(row, col));
                    }
                } else {
                    assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(row, col));
                }
            }
        }
    }

    @Test
    public void testSimpleValidRedMove() {
        CheckersBoard board = new CheckersBoard();

        // Set up: place red piece in open area
        board.setPieceAt(4, 3, CheckersBoard.Piece.RED);
        board.setPieceAt(5, 4, CheckersBoard.Piece.EMPTY);

        assertTrue(board.isValidMove(4, 3, 5, 4));
        assertTrue(board.makeMove(4, 3, 5, 4));
        assertEquals(CheckersBoard.Piece.RED, board.getPieceAt(5, 4));
        assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(4, 3));
    }

    @Test
    public void testInvalidMoveOntoOccupiedSquare() {
        CheckersBoard board = new CheckersBoard();
        board.setPieceAt(4, 3, CheckersBoard.Piece.RED);
        board.setPieceAt(5, 4, CheckersBoard.Piece.BLACK); // destination is not empty

        assertFalse(board.isValidMove(4, 3, 5, 4));
        assertFalse(board.makeMove(4, 3, 5, 4));
    }

    @Test
    public void testValidRedJumpOverBlack() {
        CheckersBoard board = new CheckersBoard();

        board.setPieceAt(2, 3, CheckersBoard.Piece.RED);
        board.setPieceAt(3, 4, CheckersBoard.Piece.BLACK);
        board.setPieceAt(4, 5, CheckersBoard.Piece.EMPTY);

        assertTrue(board.isValidMove(2, 3, 4, 5));
        assertTrue(board.makeMove(2, 3, 4, 5));
        assertEquals(CheckersBoard.Piece.RED, board.getPieceAt(4, 5));
        assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(3, 4)); // Captured
    }

    @Test
    public void testInvalidJumpOverOwnPiece() {
        CheckersBoard board = new CheckersBoard();

        board.setPieceAt(2, 3, CheckersBoard.Piece.RED);
        board.setPieceAt(3, 4, CheckersBoard.Piece.RED); // same color
        board.setPieceAt(4, 5, CheckersBoard.Piece.EMPTY);

        assertFalse(board.isValidMove(2, 3, 4, 5));
        assertFalse(board.makeMove(2, 3, 4, 5));
    }

    @Test
    public void testKingCanMoveBackward() {
        CheckersBoard board = new CheckersBoard();

        board.setPieceAt(3, 2, CheckersBoard.Piece.RED_KING);
        board.setPieceAt(2, 1, CheckersBoard.Piece.EMPTY);

        assertTrue(board.isValidMove(3, 2, 2, 1));
        assertTrue(board.makeMove(3, 2, 2, 1));
    }

    @Test
    public void testPromotionToRedKing() {
        CheckersBoard board = new CheckersBoard();

        board.setPieceAt(1, 2, CheckersBoard.Piece.RED);
        board.setPieceAt(0, 3, CheckersBoard.Piece.EMPTY);

        board.makeMove(1, 2, 0, 3);
        assertEquals(CheckersBoard.Piece.RED_KING, board.getPieceAt(0, 3));
    }

    @Test
    public void testPromotionToBlackKing() {
        CheckersBoard board = new CheckersBoard();

        board.setPieceAt(6, 1, CheckersBoard.Piece.BLACK);
        board.setPieceAt(7, 2, CheckersBoard.Piece.EMPTY);

        board.makeMove(6, 1, 7, 2);
        assertEquals(CheckersBoard.Piece.BLACK_KING, board.getPieceAt(7, 2));
    }

    @Test
    public void testInvalidMoveOutOfBounds() {
        CheckersBoard board = new CheckersBoard();
        board.setPieceAt(0, 1, CheckersBoard.Piece.RED);

        assertFalse(board.isValidMove(0, 1, -1, 2));
        assertFalse(board.makeMove(0, 1, -1, 2));
    }
}
