import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.*;

import static org.junit.jupiter.api.Assertions.*;

public class HomePageTest {
    private Player player;
    private CredentialsDatabase database;
    private HomePage homePage;

    @BeforeEach
    public void setUp(){
        player = new Player("testUser", "test@example.com", "password123");
        database = new CredentialsDatabase();
        homePage = new HomePage(player, database);
    }

    @Test
    public void testViewYourOwnRecords(){
        PlayerStats playerStats = homePage.viewYourOwnRecords();
        assertNotNull(playerStats);
        assertEquals(0, playerStats.getTotalWins());
        assertEquals(0, playerStats.getTotalLosses());
        assertEquals(0, playerStats.getTotalLosses());
        assertEquals(0, playerStats.getRankForChecker());
        assertEquals(0, playerStats.getRankForConnect4());
        assertEquals(0, playerStats.getRankForTicTacToe());
    }

    @Test
    public void testViewOtherPlayerRecords(){
        Player otherPlayer = new Player("otherUser", "other@example.com", "password345");
        PlayerStats playerStats = homePage.viewOtherPlayerRecords(otherPlayer);
        assertNotNull(playerStats);
        assertEquals("otherUser", otherPlayer.getUsername());
        assertEquals(0, playerStats.getTotalWins());
        assertEquals(0, playerStats.getTotalLosses());
        assertEquals(0, playerStats.getTotalTies());
        assertEquals(0, playerStats.getRankForChecker());
        assertEquals(0, playerStats.getRankForConnect4());
        assertEquals(0, playerStats.getRankForTicTacToe());
    }

    @Test
    public void testViewSettings(){
        Settings settings = homePage.viewSettings();
        assertNotNull(settings);
        assertEquals(player, settings.getPlayer());
    }

    @Test
    public void testGetPlayer(){
        Player returnPlayer = homePage.getPlayer();
        assertNotNull(returnPlayer);
        assertEquals("testUser", returnPlayer.getUsername());
        assertEquals("test@example.com", returnPlayer.getEmail());
        assertEquals("password123", returnPlayer.getPassword());
    }
}
