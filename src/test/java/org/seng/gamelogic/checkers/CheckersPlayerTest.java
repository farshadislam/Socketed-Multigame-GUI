package org.seng.gamelogic.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestCheckersBoard extends CheckersBoard {
    public boolean makeMoveCalled = false;
    public int fromRow, fromCol, toRow, toCol;

    @Override
    public boolean makeMove(int rowStart, int colStart, int rowEnd, int colEnd) {
        makeMoveCalled = true;
        fromRow = rowStart;
        fromCol = colStart;
        toRow = rowEnd;
        toCol = colEnd;
        return true;
    }
}

public class CheckersPlayerTest {
    private CheckersPlayer player;
    private TestCheckersBoard board;
    private CheckersMove move;

    @BeforeEach
    void setUp() {
        player = new CheckersPlayer("rehan", 999999999, 'b', 99);
        board = new TestCheckersBoard();
        move = new CheckersMove(1, 1, 2, 2, player);
    }

    @Test
    void testCheckersPLayers() {
        assertEquals("rehan", player.getName());
        assertEquals(999999999, player.getPlayerID());
        assertEquals('b', player.getSymbol());
        assertEquals(99, player.getRank());
    }

    @Test
    void testReadyStartReturnsTrue() {
        assertTrue(player.readyStart());
    }

    @Test
    void testMakeMove() {
        boolean result = player.makeMove(board, move);

        assertTrue(result);
        assertTrue(board.makeMoveCalled);
        assertEquals(1, board.fromRow);
        assertEquals(1, board.fromCol);
        assertEquals(2, board.toRow);
        assertEquals(2, board.toCol);
    }
}
