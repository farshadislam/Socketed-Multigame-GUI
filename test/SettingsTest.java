import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.authentication.Settings;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {
    private Player player; // create player object for testing
    private Settings settings;

    @BeforeEach
    public void initializeFields(){
        player = new Player("user", "user@gmail.com", "passWORD");
        settings = new Settings(player, new CredentialsDatabase());
    }

    @Test
    public void testChangeUsername(){
        assertTrue(settings.changeUsername("superUser"));
        assertEquals("superUser", player.getUsername());
        assertFalse(settings.changeUsername(null));
    }

    @Test
    public void testChangeEmail(){
        assertTrue(settings.changeEmail("user12345@gmail.com"));
        assertEquals("user12345@gmail.com", player.getEmail());
        assertFalse(settings.changeEmail(null));
    }

    @Test
    public void testChangePassword(){
        assertTrue(settings.changePassword("passWORD", "SENG300"));
        assertEquals("SENG300", player.getPassword());
        assertFalse(settings.changePassword("password", "OMG"));
    }

}
