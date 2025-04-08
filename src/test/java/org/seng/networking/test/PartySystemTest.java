
import org.seng.networking.leaderboard_matchmaking.GameType;
import org.junit.jupiter.api.*;
import org.seng.networking.NetworkingManager;
import org.seng.networking.PartySystem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartySystemTest {

    private PartySystem party;

    @BeforeEach
    void setup() {
        party = new PartySystem("Host", GameType.CHECKERS);
        NetworkingManager.getInstance().connectPlayer("Host");
    }

    @Test
    void testAddPlayer_success() {
        NetworkingManager.getInstance().connectPlayer("User1");
        assertTrue(party.addPlayer("User1"));
        assertTrue(party.getPartyMembers().contains("User1"));
    }

    @Test
    void testAddPlayer_fail() {
        NetworkingManager.getInstance().connectPlayer("User2");

    }

    @Test
    void testAddPlayer_duplicate() {
        party.addPlayer("Host"); // Already added in constructor
        assertFalse(party.addPlayer("Host"));
    }

    @Test
    void testRemovePlayer_success() {
        NetworkingManager.getInstance().connectPlayer("User2");
        party.addPlayer("User2");
        assertTrue(party.removePlayer("User2"));
        assertFalse(party.getPartyMembers().contains("User2"));
    }

    @Test
    void testRemovePlayer_hostCannotBeRemoved() {
        assertFalse(party.removePlayer("Host"));
    }

    @Test
    void testDisbandParty() {
        party.disbandParty();
        assertTrue(party.getPartyMembers().isEmpty());
    }

    @Test
    void testStartGame_success() {
        NetworkingManager.getInstance().connectPlayer("Player2");
        party.addPlayer("Player2");
        assertTrue(party.startGame());
        assertTrue(party.isInGame());
    }

    @Test
    void testStartGame_notEnoughPlayers() {
        assertFalse(party.startGame());
    }

    @Test
    void testEndGame() {
        NetworkingManager.getInstance().connectPlayer("Player3");
        party.addPlayer("Player3");
        party.startGame();
        party.endGame();
        assertFalse(party.isInGame());
    }

    @Test
    void testGetPartyMembers() {
        List<String> members = party.getPartyMembers();
        assertEquals(1, members.size());
        assertEquals("Host", members.get(0));
    }

    @Test
    void testGetHostUsername() {
        assertEquals("Host", party.getHostUsername());
    }

    @Test
    void testGetGameType() {
        assertEquals(GameType.CHECKERS, party.getGameType());
    }
}
