package leaderboard_matchmaking;

import org.seng.leaderboard_matchmaking.*;
import org.seng.authentication.Player;
import org.seng.leaderboard_matchmaking.matchmaking.Queue;
import org.seng.leaderboard_matchmaking.matchmaking.QueueMatchMaker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class Last5MatchesTest {

    @Test
    public void testInitialHistoryEmpty() {
        Last5Matches history = new Last5Matches();
        assertEquals(0, history.getMatchHistory().size());
    }

    @Test
    public void testSingleMatchAdded() {
        Last5Matches history = new Last5Matches();
        Player p = new Player("Alice", "a@email.com", "pass");
        history.update(GameType.CONNECT4, p);
        assertEquals(1, history.getMatchHistory().size());
        assertEquals(p, history.getMatchAt(0).get(1));
    }

    @Test
    public void testFiveMatchesExact() {
        Last5Matches history = new Last5Matches();
        for (int i = 0; i < 5; i++) {
            history.update(GameType.CHECKERS, new Player("P" + i, "e" + i, "pw"));
        }
        assertEquals(5, history.getMatchHistory().size());
    }

    @Test
    public void testOldestMatchRemovedOnSixth() {
        Last5Matches history = new Last5Matches();
        for (int i = 0; i < 6; i++) {
            history.update(GameType.TICTACTOE, new Player("P" + i, "e" + i, "pw"));
        }
        List<List<Object>> matches = history.getMatchHistory();
        assertEquals(5, matches.size());
        assertEquals("p1", ((Player) matches.get(0).get(1)).getUsername());
    }

    @Test
    public void testGetMatchAtValidIndex() {
        Last5Matches history = new Last5Matches();
        Player p = new Player("Bob", "bob@test.com", "123");
        history.update(GameType.CONNECT4, p);
        List<Object> match = history.getMatchAt(0);
        assertEquals(GameType.CONNECT4, match.get(0));
        assertEquals(p, match.get(1));
    }

    @Test
    public void testGetMatchAtInvalidIndexThrows() {
        Last5Matches history = new Last5Matches();
        assertThrows(IndexOutOfBoundsException.class, () -> history.getMatchAt(0));
    }

    @Test
    public void testClearHistory() {
        Last5Matches history = new Last5Matches();
        history.update(GameType.CONNECT4, new Player("A", "a@x.com", "pw"));
        history.clearHistory();
        assertEquals(0, history.getMatchHistory().size());
    }

    @Test
    public void testPlayerLast5MatchesIntegration() {
        Player p1 = new Player("Alice", "a@test.com", "pass");
        Player p2 = new Player("Bob", "b@test.com", "pass");

        p1.getLast5MatchesObject().update(GameType.TICTACTOE, p2);

        List<List<Object>> history = p1.getLast5MatchesObject().getMatchHistory();
        assertEquals(1, history.size());
        assertEquals(p2, history.get(0).get(1));
        assertEquals(GameType.TICTACTOE, history.get(0).get(0));
    }


    @Test
    public void testMatchmakingLoopUpdatesLast5Matches() throws InterruptedException {
        QueueMatchMaker matcher = new QueueMatchMaker();
        Player p1 = new Player("p1", "p1@test.com", "pass");
        Player p2 = new Player("p2", "p2@test.com", "pass");

        // Set same rank
        p1.getTicTacToeStats().setRank(Rank.BRONZE);
        p2.getTicTacToeStats().setRank(Rank.BRONZE);

        // Manually enqueue them into same pair
        Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(1); // Pair 0 for rank 1
        pair[0].enqueue(p1);
        pair[1].enqueue(p2);

        matcher.startMatchmakingLoop();
        Thread.sleep(5500); // Give matchmaking loop a chance to run once

        List<List<Object>> p1Matches = p1.getLast5MatchesObject().getMatchHistory();
        List<List<Object>> p2Matches = p2.getLast5MatchesObject().getMatchHistory();

        assertEquals(1, p1Matches.size());
        assertEquals(1, p2Matches.size());
        assertEquals(p2, p1Matches.get(0).get(1));
        assertEquals(p1, p2Matches.get(0).get(1));
    }


}
