package org.seng.gamelogic.connectfour;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConnectFourMoveTest {

    private ConnectFourPlayer player;
    private ConnectFourMove move;

    @BeforeEach
    void setUp() {
        player = new ConnectFourPlayer("Player1", 1, 'b', 1);
        move = new ConnectFourMove(player, 3, 2);
    }

    @Test
    void testConstructor() {
        assertEquals(3, move.column);
        assertEquals(2, move.row);
        assertEquals(player, move.player);
    }

    @Test
    void testGetMoveDetails() {
        String details = move.getMoveDetails(player);
        assertTrue(details.contains("Player1"));
        assertTrue(details.contains("column 3"));
    }
}