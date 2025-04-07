import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.Player;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player; // create player object for testing

    // initialize a player before each unit test
    @BeforeEach
    public void initializePlayer(){
        player = new Player("newUser", "newUser@gmail.com", "passWORD");
    }

    // all values match
    @Test
    public void testGeneral1(){
        assertEquals("newUser", player.getUsername());
        assertEquals("newUser@gmail.com", player.getEmail());
        assertEquals("passWORD", player.getPassword());
    }

    // wrong username
    @Test
    public void testGeneral2(){
        assertNotEquals("oldUser", player.getUsername());
        assertEquals("newUser@gmail.com", player.getEmail());
        assertEquals("passWORD", player.getPassword());
    }

    // wrong email
    @Test
    public void testGeneral3(){
        assertEquals("newUser", player.getUsername());
        assertNotEquals("oldUser@gmail.com", player.getEmail());
        assertEquals("passWORD", player.getPassword());
    }

    // wrong password
    @Test
    public void testGeneral4(){
        assertEquals("newUser", player.getUsername());
        assertEquals("newUser@gmail.com", player.getEmail());
        assertNotEquals("pass1234567", player.getPassword());
    }

    // empty username
    @Test
    public void testGeneral5(){
        Player p2 = new Player("", "newUser@gmail.com", "passWORD");
        assertNull(p2.getUsername());
    }

    // null username
    @Test
    public void testGeneral6(){
        Player p2 = new Player(null, "newUser@gmail.com", "passWORD");
        assertNull(p2.getUsername());
    }

    // whitespace username
    @Test
    public void testGeneral7(){
        Player p2 = new Player(" ", "newUser@gmail.com", "passWORD");
        assertNull(p2.getUsername());
    }

    // invalid password
    @Test
    public void testGeneral8(){
        Player p2 = new Player("newUser", "newUser@gmail.com", "buzz");
        assertNull(p2.getPassword());
    }

    // invalid email
    @Test
    public void testGeneral9(){
        Player p2 = new Player("newUser", "user@gmail.com", "buzz");
        assertNull(p2.getEmail());
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
        Player p3 = new Player(null, "oldUser@gmail.com", "password");
        assertNotEquals(player, p3);
    }

    // one username valid, other empty
    @Test
    public void testEquals5(){
        Player p3 = new Player("", "oldUser@gmail.com", "password");
        assertNotEquals(player, p3);
    }

    // one username valid, other whitespace
    @Test
    public void testEquals6(){
        Player p3 = new Player(" ", "oldUser@gmail.com", "password");
        assertNotEquals(player, p3);
    }

    // different classes being compared
    @Test
    public void testEquals7() {
        int num = 5;
        assertNotEquals(player, num);
    }

    // same player
    @Test
    public void testEquals8(){
        assertEquals(player, player);
    }
}

