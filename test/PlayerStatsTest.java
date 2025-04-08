import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.Player;
import org.seng.authentication.PlayerStats;
import org.seng.leaderboard.Rank;

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
        assertSame(Rank.BRONZE, playerStats.getRankForConnect4());
    }

    @Test
    public void testGetWinsForTicTacToe(){
        assertEquals(0, playerStats.getWinsForTicTacToe());
    }

    @Test
    public void testGetLossesForTicTacToe(){
        assertEquals(0, playerStats.getLossesForTicTacToe());
    }

    @Test
    public void testGetTiesForTicTacToe(){
        assertEquals(0, playerStats.getTiesForTicTacToe());
    }

    @Test
    public void testGetRankForTicTacToe(){
        assertSame(Rank.BRONZE, playerStats.getRankForTicTacToe());
    }

    @Test
    public void testGetWinsForCheckers(){
        assertEquals(0, playerStats.getWinsForChecker());
    }

    @Test
    public void testGetLossesForCheckers(){
        assertEquals(0, playerStats.getLossesForChecker());
    }

    @Test
    public void testGetTiesForCheckers(){
        assertEquals(0, playerStats.getTiesForChecker());
    }

    @Test
    public void testGetRankForCheckers(){
        assertSame(Rank.BRONZE, playerStats.getRankForChecker());
    }
}

