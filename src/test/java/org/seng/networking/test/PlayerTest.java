
import main.java.org.seng.networking.leaderboard_matchmaking.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.networking.Player;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player1;
    private Player player2;
    private static final String TEST_USERNAME_1 = "player1";
    private static final String TEST_PASSWORD_1 = "password123";
    private static final String TEST_USERNAME_2 = "player2";
    private static final String TEST_PASSWORD_2 = "password456";

    @BeforeEach
    void setUp() {
        player1 = new Player(TEST_USERNAME_1, TEST_PASSWORD_1);
        player2 = new Player(TEST_USERNAME_2, TEST_PASSWORD_2);
    }

    @Test
    void testLoginSuccess() {
        assertTrue(player1.login(TEST_PASSWORD_1));
        assertTrue(player1.isLoggedIn());
    }

    @Test
    void testLoginFailure() {
        assertFalse(player1.login("wrongpassword"));
        assertFalse(player1.isLoggedIn());
    }

    @Test
    void testLogout() {
        player1.login(TEST_PASSWORD_1);
        player1.logout();
        assertFalse(player1.isLoggedIn());
        assertNull(player1.getCurrentMatch());
    }

    @Test
    void testJoinMatch() {
        player1.login(TEST_PASSWORD_1);
        player2.login(TEST_PASSWORD_2);
        player1.joinMatch(player2, GameType.TICTACTOE);

        assertNotNull(player1.getCurrentMatch());
        assertNotNull(player2.getCurrentMatch());
        assertEquals(player1.getCurrentMatch(), player2.getCurrentMatch());
    }

    @Test
    void testLeaveMatch() {
        player1.login(TEST_PASSWORD_1);
        player2.login(TEST_PASSWORD_2);
        player1.joinMatch(player2, GameType.TICTACTOE);

        player1.leaveMatch();
        assertNull(player1.getCurrentMatch());
        assertNull(player2.getCurrentMatch());
    }

    @Test
    void testDisconnect() {
        player1.login(TEST_PASSWORD_1);
        player1.disconnect();
        assertFalse(player1.isLoggedIn());
        assertNull(player1.getCurrentMatch());
    }

    @Test
    void testSettersAndGetters() {
        player1.setPlayerID(101);
        player1.setSymbol('X');
        player1.setRank(1500);

        assertEquals(101, player1.getPlayerID());
        assertEquals('X', player1.getSymbol());
        assertEquals(1500, player1.getRank());
    }

    @Test
    void testIncrementWinsAndLosses() {
        player1.incrementWins();
        player1.incrementLosses();

        assertEquals(1, player1.getWins());
        assertEquals(1, player1.getLosses());
    }

    @Test
    void testEquals() {
        Player player3 = new Player(TEST_USERNAME_1, TEST_PASSWORD_1);
        assertEquals(player1, player3);
        assertNotEquals(player1, player2);
    }
}
