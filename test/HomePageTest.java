import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.seng.leaderboard_matchmaking.Rank.BRONZE;


public class HomePageTest {
    private Player player;
    private CredentialsDatabase database;
    private HomePage homePage;

    @BeforeEach
    public void setUp(){
        player = new Player("testuser", "test@example.com", "password123");
        database = new CredentialsDatabase();
        homePage = new HomePage(player, database);
    }

    @Test
    public void testViewYourOwnRecords(){
        Player playerRecords = homePage.viewYourOwnRecords();
        assertNotNull(playerRecords);
        assertEquals(0, playerRecords.getTotalWins());
        assertEquals(0, playerRecords.getTotalLosses());
        assertEquals(0, playerRecords.getTotalLosses());
        assertSame(BRONZE, playerRecords.getCheckersStats().getRank());
        assertSame(BRONZE, playerRecords.getConnect4Stats().getRank());
        assertSame(BRONZE, playerRecords.getTicTacToeStats().getRank());
    }

    @Test
    public void testViewOtherPlayerRecords(){
        Player otherPlayer = new Player("otheruser", "other@example.com", "password345");
        Player playerRecords = homePage.viewOtherPlayerRecords(otherPlayer);
        assertNotNull(playerRecords);
        assertEquals("otheruser", otherPlayer.getUsername());
        assertEquals(0, player.getTotalWins());
        assertEquals(0, player.getTotalLosses());
        assertEquals(0, player.getTotalTies());
        assertSame(BRONZE, playerRecords.getCheckersStats().getRank());
        assertSame(BRONZE, playerRecords.getConnect4Stats().getRank());
        assertSame(BRONZE, playerRecords.getTicTacToeStats().getRank());
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
        assertEquals("testuser", returnPlayer.getUsername());
        assertEquals("test@example.com", returnPlayer.getEmail());
        assertEquals("password123", returnPlayer.getPassword());
    }
}
