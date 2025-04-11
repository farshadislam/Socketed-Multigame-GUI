package org.seng.gamelogicTest.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.seng.gamelogicTest.tictactoe.TicTacToeBoard.Mark;

public class TicTacToeBoardTest {

    private TicTacToeBoard board;

    @BeforeEach
    public void setup() {
        board = new TicTacToeBoard();
    }

    @Test
    public void testBoardInitialization() {
        for (int r = 0; r < TicTacToeBoard.SIZE; r++) {
            for (int c = 0; c < TicTacToeBoard.SIZE; c++) {
                assertEquals(Mark.EMPTY, board.getMark(r, c));
            }
        }
    }

    @Test
    public void testValidMove() {
        assertTrue(board.validMove(1, 1));
        board.setMark(1, 1, Mark.X);
        assertFalse(board.validMove(1, 1));
    }

    @Test
    public void testMakeMove() {
        assertTrue(board.makeMove(0, 0, Mark.X));
        assertEquals(Mark.X, board.getMark(0, 0));
        assertFalse(board.makeMove(0, 0, Mark.O)); // already taken
    }

    @Test
    public void testResetBoard() {
        board.setMark(2, 2, Mark.O);
        board.resetBoard();
        assertEquals(Mark.EMPTY, board.getMark(2, 2));
    }
}