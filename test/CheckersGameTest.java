package org.seng.gamelogic.checkers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckersGameTest {

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

