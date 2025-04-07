package org.seng.gamelogic.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckersMoveTest {
    private CheckersPlayer player1;
    private CheckersPlayer player2;

    @BeforeEach
    void setup() {
        player1 = new CheckersPlayer("rehan", 999999999, 'b', 99);
        player2 = new CheckersPlayer("rehan2", 110100100, 'r', 66);
    }

    @Test
    void testCheckersMove() {
        CheckersMove move = new CheckersMove(2, 3, 3, 4, player1);

        assertEquals(2, move.getRowStart());
        assertEquals(3, move.getColStart());
        assertEquals(3, move.getRowEnd());
        assertEquals(4, move.getColEnd());
        assertEquals(player1, move.getPlayer());
    }

    @Test
    void testSetters() {
        CheckersMove move = new CheckersMove(1, 1, 1, 1, player1);
        move.setRowStart(1);
        move.setColStart(2);
        move.setRowEnd(3);
        move.setColEnd(4);

        move.setPlayer(player2);

        assertEquals(1, move.getRowStart());
        assertEquals(2, move.getColStart());
        assertEquals(3, move.getRowEnd());
        assertEquals(4, move.getColEnd());
        assertEquals(player2, move.getPlayer());
    }

    @Test
    void testChekersMoveDetailsBlack() {
        CheckersMove move = new CheckersMove(3, 4, 4, 5, player1);
        String details = move.checkersMoveDetails();

        assertTrue(details.contains("Player rehan"));
        assertTrue(details.contains("black piece"));
        assertTrue(details.contains("from row 3 and column 4"));
        assertTrue(details.contains("to row 4 and column 5"));
    }

    @Test
    void testChekersMoveDetailsRed() {
        CheckersMove move = new CheckersMove(0, 0, 2, 2, player2);
        String details = move.checkersMoveDetails();

        assertTrue(details.contains("Player rehan2"));
        assertTrue(details.contains("red piece"));
        assertTrue(details.contains("from row 0 and column 0"));
        assertTrue(details.contains("to row 2 and column 2"));
    }
}
