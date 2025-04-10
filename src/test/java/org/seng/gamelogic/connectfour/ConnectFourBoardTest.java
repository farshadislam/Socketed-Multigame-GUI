package org.seng.gamelogic.connectfour;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectFourBoardTest {

    private ConnectFourBoard board;
    private ConnectFourPlayer bluePlayer;
    private ConnectFourPlayer yellowPlayer;

    @BeforeEach
    public void setUp() {
        board = new ConnectFourBoard();
        bluePlayer = new ConnectFourPlayer("Blue", "1", "b");
        yellowPlayer = new ConnectFourPlayer("Yellow", "2", "y");
    }

    @Test
    public void testDropPieceIntoEmptyColumn() {
        assertTrue(board.dropPiece(0, bluePlayer));
        assertEquals(ConnectFourBoard.Chip.BLUE, board.getChip(5, 0));
    }

    @Test
    public void testColumnFull() {
        for (int i = 0; i < ConnectFourBoard.ROW_COUNT; i++) {
            assertTrue(board.dropPiece(1, bluePlayer));
        }
        assertTrue(board.columnFull(1));
        assertFalse(board.dropPiece(1, yellowPlayer));
    }

    @Test
    public void testInvalidColumnTooHigh() {
        assertFalse(board.isColumn(8));
        assertFalse(board.dropPiece(8, bluePlayer));
    }

    @Test
    public void testInvalidColumnNegative() {
        assertFalse(board.isColumn(-1));
        assertFalse(board.dropPiece(-1, bluePlayer));
    }

    @Test
    public void testSetAndGetChip() {
        board.setChip(2, 3, ConnectFourBoard.Chip.YELLOW);
        assertEquals(ConnectFourBoard.Chip.YELLOW, board.getChip(2, 3));
    }
}
