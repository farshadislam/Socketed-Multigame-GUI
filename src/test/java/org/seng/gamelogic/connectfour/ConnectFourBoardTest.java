package org.seng.gamelogic.connectfour;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectFourBoardTest {

    private ConnectFourBoard board;
    private ConnectFourPlayer playerBlue;
    private ConnectFourPlayer playerYellow;

    @BeforeEach
    public void setUp() {
        board = new ConnectFourBoard();
        playerBlue = new ConnectFourPlayer("BluePlayer", 1, 'b', 1);   // id: 1, rank: 1, symbol: 'b' -> Chip.BLUE
        playerYellow = new ConnectFourPlayer("YellowPlayer", 2, 'y', 2); // id: 2, rank: 2, symbol: 'y' -> Chip.YELLOW
    }

    @Test
    public void testDropPieceInEmptyColumn() {
        boolean result = board.dropPiece(0, playerBlue);
        assertTrue(result, "Piece should be successfully dropped in column 0");
        assertEquals(ConnectFourBoard.Chip.BLUE, board.getChip(5, 0), "Bottom row should contain BLUE chip");
    }

    @Test
    public void testDropPieceInFullColumn() {
        // Fill up column 0
        for (int i = 0; i < ConnectFourBoard.ROW_COUNT; i++) {
            assertTrue(board.dropPiece(0, playerYellow), "Should be able to drop piece in column 0");
        }
        // Attempt one more
        assertFalse(board.dropPiece(0, playerYellow), "Should not be able to drop piece in full column");
    }

    @Test
    public void testInvalidColumnTooLow() {
        assertFalse(board.dropPiece(-1, playerBlue), "Should not be able to drop piece in invalid column (-1)");
    }

    @Test
    public void testInvalidColumnTooHigh() {
        assertFalse(board.dropPiece(ConnectFourBoard.COL_COUNT + 1, playerBlue), "Should not be able to drop piece in invalid column (beyond board)");
    }

    @Test
    public void testColumnFullCheck() {
        assertFalse(board.columnFull(0), "Column should initially not be full");

        for (int i = 0; i < ConnectFourBoard.ROW_COUNT; i++) {
            board.dropPiece(0, playerYellow);
        }

        assertTrue(board.columnFull(0), "Column should be full after 6 pieces");
    }

    @Test
    public void testResetBoardClearsGrid() {
        board.dropPiece(0, playerBlue);
        board.dropPiece(1, playerYellow);

        board.resetBoard();

        for (int row = 0; row < ConnectFourBoard.ROW_COUNT; row++) {
            for (int col = 0; col < ConnectFourBoard.COL_COUNT; col++) {
                assertEquals(ConnectFourBoard.Chip.EMPTY, board.getChip(row, col), "All cells should be EMPTY after reset");
            }
        }
    }

    @Test
    public void testSetAndGetChipManually() {
        board.setChip(3, 4, ConnectFourBoard.Chip.YELLOW);
        assertEquals(ConnectFourBoard.Chip.YELLOW, board.getChip(3, 4), "Manual chip placement should be retrievable");
    }
}