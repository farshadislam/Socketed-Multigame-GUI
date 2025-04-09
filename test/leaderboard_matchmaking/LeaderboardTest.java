package leaderboard_matchmaking;

import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.leaderboard_matchmaking.Leaderboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderboardTest {

    private CredentialsDatabase db;

    @BeforeEach
    public void setUp() {
        db = new CredentialsDatabase();

        db.clearDatabase();

        Player p1 = new Player("Alpha", "alpha@example.com", "pw");
        p1.getCheckersStats().setWins(5);
        p1.getTicTacToeStats().setWins(3);
        p1.getConnect4Stats().setWins(7);

        Player p2 = new Player("Beta", "beta@example.com", "pw");
        p2.getCheckersStats().setWins(9);
        p2.getTicTacToeStats().setWins(2);
        p2.getConnect4Stats().setWins(1);

        Player p3 = new Player("Gamma", "gamma@example.com", "pw");
        p3.getCheckersStats().setWins(1);
        p3.getTicTacToeStats().setWins(9);
        p3.getConnect4Stats().setWins(5);

        db.addNewPlayer(p1.getUsername(), p1);
        db.addNewPlayer(p2.getUsername(), p2);
        db.addNewPlayer(p3.getUsername(), p3);
    }

    @Test
    public void testCheckersRankingOrder() {
        List<Player> ranked = Leaderboard.getRankedPlayersByGame(db, "Checkers");

        assertEquals("beta", ranked.get(0).getUsername());
        assertEquals("alpha", ranked.get(1).getUsername());
        assertEquals("gamma", ranked.get(2).getUsername());
    }

    @Test
    public void testTicTacToeRankingOrder() {
        List<Player> ranked = Leaderboard.getRankedPlayersByGame(db, "Tic Tac Toe");

        assertEquals("gamma", ranked.get(0).getUsername());
        assertEquals("alpha", ranked.get(1).getUsername());
        assertEquals("beta", ranked.get(2).getUsername());
    }

    @Test
    public void testConnect4RankingOrder() {
        List<Player> ranked = Leaderboard.getRankedPlayersByGame(db, "Connect 4");

        assertEquals("alpha", ranked.get(0).getUsername());
        assertEquals("gamma", ranked.get(1).getUsername());
        assertEquals("beta", ranked.get(2).getUsername());
    }

    @Test
    public void testInvalidGameTypeReturnsZeroWins() {
        List<Player> ranked = Leaderboard.getRankedPlayersByGame(db, "Chess");

        // Should return in original order since all have 0 wins in unknown game type
        assertEquals(3, ranked.size());
        assertTrue(ranked.contains(db.findPlayerByUsername("alpha")));
        assertTrue(ranked.contains(db.findPlayerByUsername("beta")));
        assertTrue(ranked.contains(db.findPlayerByUsername("gamma")));
    }

    @Test
    public void testTieHandlingInRanking() {
        Player p4 = new Player("Delta", "delta@example.com", "pw");
        p4.getCheckersStats().setWins(9); // Same as Beta

        db.addNewPlayer(p4.getUsername(), p4);

        List<Player> ranked = Leaderboard.getRankedPlayersByGame(db, "Checkers");

        assertEquals(4, ranked.size());
        assertTrue(
                (ranked.get(0).getUsername().equals("beta") && ranked.get(1).getUsername().equals("delta")) ||
                        (ranked.get(0).getUsername().equals("delta") && ranked.get(1).getUsername().equals("beta"))
        );
    }

    @Test
    public void testCaseInsensitiveGameInput() {
        List<Player> lowerCase = Leaderboard.getRankedPlayersByGame(db, "checkers");
        List<Player> properCase = Leaderboard.getRankedPlayersByGame(db, "Checkers");

        assertEquals(lowerCase, properCase);
    }

    @Test
    public void testNullGameTypeReturnsOriginalOrder() {
        List<Player> ranked = Leaderboard.getRankedPlayersByGame(db, null);

        assertEquals(3, ranked.size());
        assertTrue(ranked.contains(db.findPlayerByUsername("alpha")));
        assertTrue(ranked.contains(db.findPlayerByUsername("beta")));
        assertTrue(ranked.contains(db.findPlayerByUsername("gamma")));
    }

    @Test
    public void testNoPlayersInDatabase() {
        CredentialsDatabase emptyDB = new CredentialsDatabase();
        emptyDB.clearDatabase(); //We need to add this because currently the database file has players so on start it wont be truly empty

        List<Player> ranked = Leaderboard.getRankedPlayersByGame(emptyDB, "Checkers");

        assertNotNull(ranked);
        assertEquals(0, ranked.size());
    }

    @Test
    public void testUpdatePlayerWin() {

        // Step 1: Set up a mock database with a player
        CredentialsDatabase db = new CredentialsDatabase();
        db.clearDatabase(); //We need to add this because currently the database file has players so on start it wont be truly empty


        // Create a new player with initial game stats
        Player p1 = new Player("Alpha", "alpha@example.com", "password");
        p1.getCheckersStats().setWins(3);  // Player has 3 wins in Checkers
        p1.getCheckersStats().setGamesPlayed(5);  // Player has played 5 games


        // Add player to the database
        db.addNewPlayer(p1.getUsername(), p1);


        // Step 2: Call updatePlayerWin method to simulate a win in Checkers
        Leaderboard.updatePlayerWin(db, "Alpha", "checkers");


        // Step 3: Retrieve the updated player stats from the database
        Player updatedPlayer = db.findPlayerByUsername("Alpha");


        // Step 4: Assert that the player's win count has been updated correctly
        assertNotNull(updatedPlayer, "Player should exist in the database");
        assertEquals(4, updatedPlayer.getCheckersStats().get_wins(), "Player's wins in Checkers should be incremented to 4");
        assertEquals(6, updatedPlayer.getCheckersStats().getGamesPlayed(), "Player's games played in Checkers should be incremented to 6");


        // Step 5: Check the leaderboard to ensure the player is ranked correctly
        List<Player> rankedPlayers = Leaderboard.getRankedPlayersByGame(db, "checkers");


        // Assert that the player is still ranked based on their wins
        assertTrue(rankedPlayers.indexOf(updatedPlayer) >= 0, "Player should be in the ranked list");
        assertEquals(p1.getUsername(), rankedPlayers.get(0).getUsername(), "Player Alpha should be ranked at the top");
    }

    @Test
    public void testUpdatePlayerWinForNonExistentPlayer() {
        // Attempting to update a player who doesn't exist in the database
        CredentialsDatabase db = new CredentialsDatabase();
        db.clearDatabase(); //We need to add this because currently the database file has players so on start it wont be truly empty
        Leaderboard.updatePlayerWin(db, "NonExistentPlayer", "checkers");

        // Since the player doesn't exist, no change should happen, and the leaderboard should be empty.
        List<Player> ranked = Leaderboard.getRankedPlayersByGame(db, "checkers");
        assertEquals(0, ranked.size(), "The leaderboard should be empty.");
    }

    @Test
    public void testUpdatePlayerWinForMultipleGames() {
        CredentialsDatabase db = new CredentialsDatabase();

        Player p1 = new Player("Alpha", "alpha@example.com", "password");
        p1.getCheckersStats().setWins(3);  // Player has 3 wins in Checkers
        p1.getTicTacToeStats().setWins(2); // Player has 2 wins in Tic Tac Toe
        p1.getConnect4Stats().setWins(1);  // Player has 1 win in Connect 4

        db.addNewPlayer(p1.getUsername(), p1);

        // Update wins for different game types
        Leaderboard.updatePlayerWin(db, "Alpha", "checkers");
        Leaderboard.updatePlayerWin(db, "Alpha", "tic tac toe");
        Leaderboard.updatePlayerWin(db, "Alpha", "connect 4");

        // Retrieve the updated player stats
        Player updatedPlayer = db.findPlayerByUsername("Alpha");

        // Assert that the player's wins in each game type have been updated correctly
        assertEquals(4, updatedPlayer.getCheckersStats().get_wins(), "Player's wins in Checkers should be incremented to 4");
        assertEquals(3, updatedPlayer.getTicTacToeStats().get_wins(), "Player's wins in Tic Tac Toe should be incremented to 3");
        assertEquals(2, updatedPlayer.getConnect4Stats().get_wins(), "Player's wins in Connect 4 should be incremented to 2");
    }

    @Test
    public void testUpdatePlayerWinWithInvalidGameType() {
        CredentialsDatabase db = new CredentialsDatabase();

        Player p1 = new Player("Alpha", "alpha@example.com", "password");
        p1.getCheckersStats().setWins(3);
        db.addNewPlayer(p1.getUsername(), p1);

        // Try to update a non-existent game type
        Leaderboard.updatePlayerWin(db, "Alpha", "nonexistentgame");

        // Retrieve the player and check their stats remain unchanged
        Player updatedPlayer = db.findPlayerByUsername("Alpha");

        assertNotNull(updatedPlayer, "Player should exist in the database");
        assertEquals(3, updatedPlayer.getCheckersStats().get_wins(), "Player's wins should remain unchanged.");
    }


    @Test
    public void testGetTopNPlayers() {
        // Get the top 1 player by their wins in "checkers"
        List<Player> top1Player = Leaderboard.getTopNPlayers(db, "checkers", 1);


        // Verify the top 1 player is correct
        assertEquals(1, top1Player.size(), "There should be 1 player in the top list.");
        assertEquals("beta", top1Player.get(0).getUsername(), "The top player should be Beta.");
    }

    @Test
    public void testGetTopNPlayersForSpecificGame() {
        List<Player> top2Players = Leaderboard.getTopNPlayers(db, "checkers", 2);

        // Verify that the top 2 players are returned in correct order (based on their wins)
        assertEquals(2, top2Players.size(), "There should be 2 players in the top list.");
        assertEquals("beta", top2Players.get(0).getUsername(), "The top player should be Beta.");
        assertEquals("alpha", top2Players.get(1).getUsername(), "The second player should be Alpha.");
    }

    @Test
    public void testGetTopNPlayersWithNGreaterThanTotalPlayers() {
        // Request top 5 players when there are only 3 players
        List<Player> top5Players = Leaderboard.getTopNPlayers(db, "checkers", 5);

        // Verify that the list contains all players (max players available)
        assertEquals(3, top5Players.size(), "There should be only 3 players in the top list.");
    }



}
