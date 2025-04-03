import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.Player;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player; // create player object for testing

    @BeforeEach
    public void initializePlayer(){
        player = new Player("user", "user@gmail.com", "passWORD");
    }

    @Test
    public void testGeneral(){
        assertEquals("user", player.getUsername());
        assertEquals("user@gmail.com", player.getEmail());
        assertNotEquals("password", player.getPassword());
    }

    @Test
    public void testUpdateEmail(){
        assertTrue(player.updateEmail("user@gmail.com", "user123@gmail.com"));
        assertEquals("user123@gmail.com", player.getEmail());
        assertNotEquals("user123@hotmail.com", player.getEmail());
        assertFalse(player.updateEmail("player@gmail.com", "user567@gmail.com"));
    }

    @Test
    public void testUpdatePassword(){
        assertTrue(player.updatePassword("passWORD", "SENG300"));
        assertEquals("SENG300", player.getPassword());
        assertNotEquals("LeBron", player.getPassword());
        assertFalse(player.updatePassword("12345", "gameTime"));
    }

    @Test
    public void testUpdateUsername(){
        assertTrue(player.updateUsername("user", "superUser"));
        assertEquals("superUser", player.getUsername());
        assertNotEquals("player", player.getUsername());
        assertFalse(player.updateUsername("name", "Avenger"));
    }

    @Test
    public void testEquals(){
        Player p2 = new Player("user", null, null);
        Player p3 = new Player("Java", null, null);
        assertNotEquals(player, p3);
        assertEquals(player, p2);
    }
}

