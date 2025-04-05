import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.*;

import static org.junit.jupiter.api.Assertions.*;

public class HomePageTest {
    private Player player;
    private CredentialsDatabase database;
    private Settings accountSettings;
    private HomePage homePage;

    @BeforeEach
    public void setUp(){
        player = new Player("testUser", "test@example.com", "password123");
        database = new CredentialsDatabase();
        homePage = new HomePage(player, database);
    }

    @Test
    public void testViewOtherPlayerRecords(){
        Player other = new Player("otherUser", "other@example.com", "password345");
        PlayerStats stats = homePage.viewOtherPlayerRecords(other);
        assertNotNull(stats);
        assertEquals("otherUser", other.getUsername());
        assertEquals("other@example.com", other.getEmail());
        assertEquals("password345", other.getPassword());
    }

    @Test
    public void testViewSettings(){
        Settings settings = homePage.viewSettings();
        assertNotNull(settings);
        assertEquals(player, settings.getPlayer());
    }


}
