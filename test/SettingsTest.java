import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.authentication.Settings;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {
    private Player player; // create player object for testing
    private Settings settings;

    // initialize the player and settings before each test
    @BeforeEach
    public void initializeFields(){
        player = new Player("newUser", "newUser@gmail.com", "passWORD");
        settings = new Settings(player, new CredentialsDatabase());
    }

    // valid username
    @Test
    public void testChangeUsername1(){
        assertTrue(settings.changeUsername("superUser"));
        assertEquals("superUser", player.getUsername());
    }

    // invalid username
    @Test
    public void testChangeUsername2(){
        assertFalse(settings.changeUsername("boo"));
    }

    // username already exists
    @Test
    public void testChangeUsername3(){
        Player p2 = new Player("java123", "java123@gmail.com", "pass12345");
        assertFalse(settings.changeUsername("java123"));
    }

    // null username

    // valid email
    @Test
    public void testChangeEmail(){
        assertTrue(settings.changeEmail("user12345@gmail.com"));
        assertEquals("user12345@gmail.com", player.getEmail());
        assertFalse(settings.changeEmail(null));
    }

    // invalid email

    // null email



    // valid password
    @Test
    public void testChangePassword(){
        assertTrue(settings.changePassword("passWORD", "SENG300!"));
        assertEquals("SENG300!", player.getPassword());
        assertFalse(settings.changePassword("password", "password1234567"));
    }

    // invalid password

    // null password

    // player deleted

    // player not deleted (wrong password)

    // logout success

}
