package main.java.org.seng.leaderboard_matchmaking;

import main.java.org.seng.authentication.CredentialsDatabase;
import main.java.org.seng.authentication.Player;

import java.util.*;

/**
 * Leaderboard class provides functionality to rank players based on
 * their number of wins in a specific game.
 */
public class Leaderboard {

    /**
     * Returns a list of players ranked in descending order of their wins
     * for a given game type.
     *
     * @param db       The credentials database containing all registered players.
     * @param gameType The name of the game (e.g., "Checkers", "Tic Tac Toe", "Connect 4").
     * @return A list of players sorted by their win count in the specified game.
     */
    public static List<Player> getRankedPlayersByGame(CredentialsDatabase db, String gameType) {
        // Get a list of all players from the database
        List<Player> sortedPlayers = new ArrayList<>(db.getPlayerCredentials().values());

        // Sort players in descending order based on their number of wins in the specified game
        sortedPlayers.sort((p1, p2) -> {
            int p1Wins = getWinsForGame(p1, gameType);
            int p2Wins = getWinsForGame(p2, gameType);
            return Integer.compare(p2Wins, p1Wins); // Higher wins first
        });

        return sortedPlayers;
    }

    /**
     * Retrieves the number of wins a player has in the specified game.
     * If the game type is unknown or null, returns 0.
     *
     * @param player   The player whose wins are being queried.
     * @param gameType The name of the game.
     * @return The number of wins for the specified game, or 0 if the game type is invalid.
     */
    private static int getWinsForGame(Player player, String gameType) {
        // Handle null input gracefully
        if (gameType == null) return 0;

        // Return win count based on game type using a switch expression
        return switch (gameType.toLowerCase()) {
            case "checkers" -> player.getCheckersStats().get_wins();
            case "tic tac toe" -> player.getTicTacToeStats().get_wins();
            case "connect 4" -> player.getConnect4Stats().get_wins();
            default -> 0; // Unknown game type
        };
    }

    public static void updatePlayerWin(CredentialsDatabase db, String playerID, String gameType) {
        // Retrieve the player from the database
        Player player = db.findPlayerByUsername(playerID);
        if (player == null) {
            System.out.println("Player not found.");
            return;
        }


        // Record the win for the player
        switch (gameType.toLowerCase()) {
            case "checkers":
                player.getCheckersStats().win();
                break;
            case "tic tac toe":
                player.getTicTacToeStats().win();
                break;
            case "connect 4":
                player.getConnect4Stats().win();
                break;
            default:
                System.out.println("Unknown game type.");
                return;
        }


        List<Player> rankedPlayers = getRankedPlayersByGame(db, gameType);
    }

    public static List<Player> getTopNPlayers(CredentialsDatabase db, String gameType, int topN) {
        // Retrieve the ranked list of players for the specified game type
        List<Player> rankedPlayers = Leaderboard.getRankedPlayersByGame(db, gameType);


        // Ensure that we do not exceed the number of available players
        int endIndex = Math.min(topN, rankedPlayers.size());


        // Return a sublist containing the top N players
        return rankedPlayers.subList(0, endIndex);
    }


}
