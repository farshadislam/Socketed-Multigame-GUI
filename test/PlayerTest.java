import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.Player;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player; // create player object for testing

    // initialize a player before each unit test
    @BeforeEach
    public void initializePlayer(){
        player = new Player("newUser", "Newuser@gmail.com", "passWORD");
    }

    // all values match
    @Test
    public void testGeneral1(){
        assertEquals("newUser", player.getUsername());
        assertEquals("newUser@gmail.com", player.getEmail());
        assertEquals("passWORD", player.getPassword());
    }

    // all values do not match
    @Test
    public void testGeneral2(){
        assertEquals("newUser", player.getUsername());
        assertEquals("newUser@gmail.com", player.getEmail());
        assertNotEquals("pass", player.getPassword());
    }

    // same username, different emails and passwords
    @Test
    public void testEquals1(){
        Player p2 = new Player("newUser", "java123@gmail.com", "password123");
        assertNotEquals(player, p2);
    }

    // same username, same emails, different passwords
    @Test
    public void testEquals2(){
        Player p2 = new Player("newUser", "newUser@gmail.com", "password123");
        assertEquals(player, p2);
    }

    // same username, same emails and passwords
    @Test
    public void testEquals3(){
        Player p2 = new Player("newUser", "newUser@gmail.com", "passWORD");
        assertEquals(player, p2);
    }

    // one username valid, other null
    @Test
    public void testEquals4(){
        Player p3 = new Player(null, null, null);
        assertNotEquals(player, p3);
    }

    // different classes being compared
    @Test
    public void testEquals5() {
        int num = 5;
        assertNotEquals(player, num);
    }

    // same player
    @Test
    public void testEquals6(){
        assertEquals(player, player);
    }
}

