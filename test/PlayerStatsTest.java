import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.Player;
import org.seng.authentication.PlayerStats;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerStatsTest {
    private Player player;
    private PlayerStats playerStats;

    @Test
    public void testExample() {
        assertTrue(true);
    }

    @BeforeEach
    public void setUp() {
        player = new Player("testUser", "test@Example.com", "password");
        playerStats = new PlayerStats(player);
    }

    @Test
    public void testGetWinsForConnect4(){
        assertEquals(0, playerStats.getWinsForConnect4());
    }

    @Test
    public void testGetLossesForConnect4(){
        assertEquals(0, playerStats.getLossesForConnect4());
    }

    @Test
    public void testGetTiesForConnect4(){
        assertEquals(0, playerStats.getTiesForConnect4());
    }

    @Test
    public void testGetRankForConnect4(){
        assertNull(playerStats.getRankForConnect4());
    }

    @Test void testGetWinsForTicTacToe(){
        assertEquals(0, playerStats.getWinsForTicTacToe());
    }

    @Test void testGetLossesForTicTacToe(){
        assertEquals(0, playerStats.getLossesForTicTacToe());
    }

    @Test void testGetTiesForTicTacToe(){
        assertEquals(0, playerStats.getTiesForTicTacToe());
    }

    @Test void testGetRankForTicTacToe(){
        assertNull(playerStats.getRankForTicTacToe());
    }
}
