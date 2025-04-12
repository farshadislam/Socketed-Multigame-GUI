package org.seng.gamelogic.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicTacToePlayerTest {

    private TicTacToePlayer player;

    @BeforeEach
    public void setUp() {
        player = new TicTacToePlayer("Alice", "alice@example.com", "securePassword123");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("Alice", player.getUsername());
        assertEquals("alice@example.com", player.getEmail());
        assertEquals("securePassword123", player.getPassword());
    }

    @Test
    public void testReadyStart() {
        assertTrue(player.readyStart(), "Player should be ready to start the game");
    }

}
