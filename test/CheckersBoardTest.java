package org.seng.gamelogic.checkers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckersBoardTest {

    @Test
    public void testInitialBoardSetup() {
        CheckersBoard board = new CheckersBoard();

        // Top 3 rows: BLACK pieces on dark squares
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    assertEquals(CheckersBoard.Piece.BLACK, board.getPieceAt(row, col),
                            "Expected BLACK at (" + row + "," + col + ")");
                } else {
                    assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(row, col),
                            "Expected EMPTY at (" + row + "," + col + ")");
                }
            }
        }

        // Rows 3 and 4: should be EMPTY
        for (int row = 3; row <= 4; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(row, col),
                        "Expected EMPTY at (" + row + "," + col + ")");
            }
        }

        // Bottom 3 rows: RED pieces on dark squares
        for (int row = 5; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    assertEquals(CheckersBoard.Piece.RED, board.getPieceAt(row, col),
                            "Expected RED at (" + row + "," + col + ")");
                } else {
                    assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(row, col),
                            "Expected EMPTY at (" + row + "," + col + ")");
                }
            }
        }
    }
    @Test
    public void testSetAndGetPiece() {
        CheckersBoard board = new CheckersBoard();
        board.setPieceAt(4, 3, CheckersBoard.Piece.RED_KING);
        assertEquals(CheckersBoard.Piece.RED_KING, board.getPieceAt(4, 3));
    }

    @Test
    public void testSimpleMove() {
        CheckersBoard board = new CheckersBoard();
        board.setPieceAt(4, 3, CheckersBoard.Piece.RED);
        board.setPieceAt(5, 4, CheckersBoard.Piece.EMPTY);

        boolean moved = board.makeMove(4, 3, 5, 4);
        assertTrue(moved, "Move should be allowed (note: isValidMove is currently incomplete)");
        assertEquals(CheckersBoard.Piece.RED, board.getPieceAt(5, 4));
        assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(4, 3));
    }

    @Test
    public void testInvalidMoveToOccupiedSquare() {
        CheckersBoard board = new CheckersBoard();
        board.setPieceAt(4, 3, CheckersBoard.Piece.RED);
        board.setPieceAt(5, 4, CheckersBoard.Piece.BLACK); // occupied

        boolean moved = board.makeMove(4, 3, 5, 4);
        assertFalse(moved, "Move should not be allowed to occupied square");
        assertEquals(CheckersBoard.Piece.RED, board.getPieceAt(4, 3));
        assertEquals(CheckersBoard.Piece.BLACK, board.getPieceAt(5, 4));
    }

    @Test
    public void testKingPromotion() {
        CheckersBoard board = new CheckersBoard();
        board.setPieceAt(1, 2, CheckersBoard.Piece.RED);
        board.setPieceAt(0, 3, CheckersBoard.Piece.EMPTY);

        board.makeMove(1, 2, 0, 3); // RED should become RED_KING

        assertEquals(CheckersBoard.Piece.RED_KING, board.getPieceAt(0, 3));
    }

    @Test
    public void testCaptureMove() {
        CheckersBoard board = new CheckersBoard();
        board.setPieceAt(2, 3, CheckersBoard.Piece.RED);
        board.setPieceAt(3, 4, CheckersBoard.Piece.BLACK);
        board.setPieceAt(4, 5, CheckersBoard.Piece.EMPTY);

        boolean moved = board.makeMove(2, 3, 4, 5);
        assertTrue(moved, "Capture should be allowed");
        assertEquals(CheckersBoard.Piece.RED, board.getPieceAt(4, 5));
        assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(2, 3));
        assertEquals(CheckersBoard.Piece.EMPTY, board.getPieceAt(3, 4), "Captured piece should be removed");
    }
}

