package org.seng.gamelogic.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicTacToeBoardTest {

    private TicTacToeBoard board;

    @BeforeEach
    public void setUp() {
        board = new TicTacToeBoard();
    }

    @Test
    public void testBoardInitialization() {
        for (int row = 0; row < TicTacToeBoard.SIZE; row++) {
            for (int col = 0; col < TicTacToeBoard.SIZE; col++) {
                assertEquals(TicTacToeBoard.Mark.EMPTY, board.getMark(row, col),
                        "Board should be initialized to EMPTY at position (" + row + "," + col + ")");
            }
        }
    }

    @Test
    public void testValidMoveTrue() {
        assertTrue(board.validMove(1, 1), "Move at (1,1) should be valid on empty board");
    }

    @Test
    public void testValidMoveFalse_OutOfBounds() {
        assertFalse(board.validMove(-1, 0), "Move at (-1,0) should be invalid");
        assertFalse(board.validMove(0, 3), "Move at (0,3) should be invalid");
    }

    @Test
    public void testValidMoveFalse_OccupiedCell() {
        board.setMark(0, 0, TicTacToeBoard.Mark.X);
        assertFalse(board.validMove(0, 0), "Move at (0,0) should be invalid if already occupied");
    }

    @Test
    public void testMakeMoveSuccess() {
        boolean result = board.makeMove(0, 0, TicTacToeBoard.Mark.X);
        assertTrue(result, "makeMove should return true for valid move");
        assertEquals(TicTacToeBoard.Mark.X, board.getMark(0, 0), "Mark at (0,0) should be X");
    }

    @Test
    public void testMakeMoveFailure() {
        board.setMark(1, 1, TicTacToeBoard.Mark.O);
        boolean result = board.makeMove(1, 1, TicTacToeBoard.Mark.X);
        assertFalse(result, "makeMove should return false for invalid move");
        assertEquals(TicTacToeBoard.Mark.O, board.getMark(1, 1), "Mark should remain O at (1,1)");
    }

    @Test
    public void testSetAndGetMark() {
        board.setMark(2, 2, TicTacToeBoard.Mark.O);
        assertEquals(TicTacToeBoard.Mark.O, board.getMark(2, 2), "getMark should return O at (2,2)");
    }

}
