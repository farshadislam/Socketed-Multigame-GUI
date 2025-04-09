package org.seng.networking.test;

import org.junit.jupiter.api.Test;
import org.seng.networking.Leaderboard;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeaderboardTest {
    @Test
    void testAddPlayer() {
        Leaderboard leaderboard = new Leaderboard();
        Player player = new Player("Sean", "pass");
        player.setPlayerID(101);

        leaderboard.addPlayer(player);

        leaderboard.updateLeaderboardRank();
        assertTrue(leaderboard.getTopPlayers().equals("101"));
    }

    @Test
    void testLogGameResults() {
        Leaderboard leaderboard = new Leaderboard();
        Player winner = new Player("Sean", "pass");
        Player loser = new Player("Combs", "pass");
        winner.setPlayerID(1);
        loser.setPlayerID(2);

        leaderboard.addPlayer(winner);
        leaderboard.addPlayer(loser);

        leaderboard.logGameResults(winner, loser);

        leaderboard.updateLeaderboardRank();
        assertEquals("1", leaderboard.getTopPlayers());
    }

    @Test
    void testUpdatePlayer() {
        Leaderboard leaderboard = new Leaderboard();
        Player player = new Player("Carol", "pass");
        player.setPlayerID(3);
        player.incrementWins(); // 1 win
        player.incrementWins(); // 2 wins

        leaderboard.updatePlayer(player);

        leaderboard.updateLeaderboardRank();
        assertEquals("3", leaderboard.getTopPlayers());
    }

    @Test
    void testGetTopPlayersMultiple() {
        Leaderboard leaderboard = new Leaderboard();

        Player p1 = new Player("Eve", "pass");
        Player p2 = new Player("Frank", "pass");

        p1.setPlayerID(10);
        p2.setPlayerID(20);

        leaderboard.addPlayer(p1);
        leaderboard.addPlayer(p2);

        leaderboard.logGameResults(p2, p1); // Frank wins once
        leaderboard.logGameResults(p2, p1); // Frank wins again

        leaderboard.updateLeaderboardRank();
        assertEquals("20", leaderboard.getTopPlayers());
    }

    @Test
    void testRankingOrder() {
        Leaderboard leaderboard = new Leaderboard();
        Player p1 = new Player("A", "pass");
        Player p2 = new Player("B", "pass");
        Player p3 = new Player("C", "pass");

        p1.setPlayerID(1);
        p2.setPlayerID(2);
        p3.setPlayerID(3);

        leaderboard.addPlayer(p1);
        leaderboard.addPlayer(p2);
        leaderboard.addPlayer(p3);

        leaderboard.logGameResults(p1, p2); // p1: 1 win
        leaderboard.logGameResults(p3, p2); // p3: 1 win
        leaderboard.logGameResults(p3, p1); // p3: 2 wins

        leaderboard.updateLeaderboardRank();
        List<String> ranks = leaderboard.getRankedPlayers();

        assertEquals("3", ranks.get(0)); // 2 wins
        assertEquals("1", ranks.get(1)); // 1 win
        assertEquals("2", ranks.get(2)); // 0 wins
    }
}
