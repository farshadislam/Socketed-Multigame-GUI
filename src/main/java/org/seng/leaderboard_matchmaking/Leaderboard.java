package org.seng.leaderboard_matchmaking;

import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;

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

    /**
     * Updates the win statistics for a player in a specified game.
     *
     * This method finds a player by their unique player ID (username) and updates the win count for the
     * specified game. The game type must be one of the following: "checkers", "tic tac toe", or "connect 4".
     * If the game type is not recognized, an error message is printed. If the player is not found in the database,
     * another error message is printed.
     *
     * @param db The CredentialsDatabase object that stores all player data.
     * @param playerID The unique identifier (username) of the player whose win is being updated.
     * @param gameType The type of game in which the player won. It must be one of: "checkers", "tic tac toe", or "connect 4".
     */
    public static void updatePlayerWin(CredentialsDatabase db, String playerID, String gameType) {
        // Retrieve the player from the database using their unique playerID (username)
        Player player = db.findPlayerByUsername(playerID);

        // If the player is not found in the database, print an error message and exit the method
        if (player == null) {
            System.out.println("Player not found.");
            return;
        }

        // Record the win for the player based on the specified game type
        switch (gameType.toLowerCase()) {
            case "checkers":
                // Update the Checkers game statistics for the player
                player.getCheckersStats().win();
                break;
            case "tic tac toe":
                // Update the Tic Tac Toe game statistics for the player
                player.getTicTacToeStats().win();
                break;
            case "connect 4":
                // Update the Connect 4 game statistics for the player
                player.getConnect4Stats().win();
                break;
            default:
                // If an unknown game type is provided, print an error message and exit the method
                System.out.println("Unknown game type.");
                return;
        }

        // Retrieve the list of players ranked by the specified game type after updating the player's win
        List<Player> rankedPlayers = getRankedPlayersByGame(db, gameType);

    }


    /**
     * Retrieves the top N players ranked by their performance in a specified game.
     *
     * This method fetches the ranked players for the given game type (e.g., "checkers", "tic tac toe", "connect 4")
     * and returns a list containing the top N players. If there are fewer than N players in the ranking,
     * it returns a list containing all available players.
     *
     * @param db The CredentialsDatabase object that stores all player data.
     * @param gameType The type of game for which the top players are to be retrieved. It can be "checkers", "tic tac toe", or "connect 4".
     * @param topN The number of top players to return. This will be the maximum number of players to include in the list.
     * @return A list of the top N players ranked by their performance in the specified game.
     *         If there are fewer than N players, it returns all players in the ranked list.
     */
    public static List<Player> getTopNPlayers(CredentialsDatabase db, String gameType, int topN) {
        // Retrieve the ranked list of players for the specified game type
        List<Player> rankedPlayers = Leaderboard.getRankedPlayersByGame(db, gameType);

        // Ensure that we do not exceed the number of available players in the ranking
        int endIndex = Math.min(topN, rankedPlayers.size());

        // Return a sublist containing the top N players, up to the number of available players
        return rankedPlayers.subList(0, endIndex);
    }

}
