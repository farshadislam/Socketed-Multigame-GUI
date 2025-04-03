import org.junit.jupiter.api.Test;

public class mainTest {

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
        assertnotEquals("user123@hotmail.com", player.getEmail());
        assertFalse(player.updateEmail("player@gmail.com", "user567@gmail.com"));
    }

    @Test
    public void testUpdatePassword(){
        assertTrue(player.updatePassword("passWORD", "SENG300"));
        assertEquals("SENG300", player.getPassword());
        assertnotEquals("LeBron", player.getPassword());
        assertFalse(player.updatePassword("12345", "gameTime"));
    }

    @Test
    public void testUpdateUsername(){
        assertTrue(player.updateUsername("user", "superUser"));
        assertEquals("superUser", player.getUsername());
        assertnotEquals("player", player.getUsername());
        assertFalse(player.updateUsername("name", "Avenger"));
    }

    @Test
    public void testEquals(){
        Player p2 = new Player("user");
        Player p3 = new player("Java");
        assertNotEquals(player, p3);
        assertEquals(player, p2);
    }
}
