package org.seng.gamelogic.connectfour;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConnectFourPlayerTest {

    private ConnectFourPlayer player;

    @BeforeEach
    void setUp() {
        player = new ConnectFourPlayer("Player1", "login@gmail.com", "loginpassword");
    }

    @Test
    void testConstructor() {
        // Access the username field directly if there's no getter
        assertEquals("Player1", player.getUsername());  // Use player.username if it's accessible directly
        assertEquals("login@gmail.com", player.getEmail());          // Same for playerID if it's accessible
        assertEquals("loginpassword", player.getPassword());
    }
    @Test
    void testReadyStart() {
        assertTrue(player.readyStart());
    }

    @Test
    void testSetSymbol() {
        player.setSymbol('y');
        assertEquals('y', player.getSymbol());
    }

    @Test
    void testGetSymbol() {
        assertEquals('b', player.getSymbol());
    }
}