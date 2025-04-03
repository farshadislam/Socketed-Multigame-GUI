import org.junit.jupiter.api.Test;

public class mainTest {

    private Player player; // create player object for testing

    @BeforeEach
    public void initializePlayer() {
        player = new Player("user", "user@gmail.com", "passWORD");
    }

    @Test
    public void testGeneral(){
        assertEquals("user", player.getUsername());
        assertEquals("user@gmail.com", player.getEmail());
        assertNotEquals("password", player.getPassword());

    }
}
